package coursework1;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WebAnalyser implements Runnable, WebStatistics {
	static int testcounter = 0;
	public static int MAX_THREAD_NUM = 20;
	public static int MAX_PAGE_COUNT = 200;
	public static int MAX_WORDS_COUNT = 4000;
	public static int TIME_OUT = 10000;
	static int capacity = 1000;
	public static String keyword = "amazon";
	static ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD_NUM);
	public Lock lock = new ReentrantLock();
	String threadname1;

	public WebAnalyser(String name) {
		this.threadname1 = name;
	}

	public static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(capacity);
	public static ArrayBlockingQueue<String> queuecopy = new ArrayBlockingQueue<String>(capacity);
	public static Vector<String> visited = new Vector<String>();
	Vector hyperlinks = new Vector();
	public static ConcurrentHashMap<String, Integer> results = new ConcurrentHashMap<String, Integer>();
	static int counter_final = 0;

	@Override
	public void find(String word, String URL) throws InterruptedException {
		// TODO Please complete this method
		ArrayList<String> urls;
		queue.remove(URL);
		if (visited.contains(URL.toString()) == false) {
			visited.add(URL);
		}
		synchronized (lock) {

			String content = Helper.getContentFromURL(visited.lastElement().toString());
			int count = Helper.countNumberOfOccurrences(word, content);
			if (count > 0) {
				results.put(visited.lastElement().toString(), count);

				counter_final = counter_final + count;

				urls = Helper.getHyperlinksFromContent(visited.lastElement().toString(), content);

				testcounter++;

				for (String url : urls) {
					if (!queuecopy.contains(url)) {

						if (queue.size() < capacity && queuecopy.size() < capacity) {
							queue.put(url);
							queuecopy.put(url);

						}
					}
				}
				if (visited.size() >= MAX_PAGE_COUNT || counter_final > MAX_WORDS_COUNT) {

					printStatistics();

				}

			}
		}
	}

	@Override
	public void printStatistics() {
		System.out.println("condition met");
		// TODO Please complete this method
		Set<Map.Entry<String, Integer>> entrySet = results.entrySet();
		Iterator<Map.Entry<String, Integer>> it = entrySet.iterator();

		while (it.hasNext()) {
			
			Map.Entry<String, Integer> entrymap = it.next();
			String s = entrymap.getKey();
			Integer i = entrymap.getValue();

			System.out.println(i + "times on " + s + "url");
		}

		
	}

	@Override
	public void run() {
		
		while (!queue.isEmpty()) {
			try {

				find(keyword, queue.take());

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		// TODO complete this method
		String weburl = "https://aws.amazon.com/";
		WebAnalyser webobj = new WebAnalyser("t_name1");
		try {
			queue.put(weburl);
			queuecopy.put(weburl);
			if (queue.size() == 1) {
				webobj.find(keyword, weburl);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		for (int i = 0; i <= 100; i++) {
			Runnable runnableobj = new WebAnalyser("threadname_t" + i);

			executor.submit(runnableobj);

		}
		executor.shutdown();
		try {
			executor.awaitTermination(20000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// executor.shutdown();
		System.out.println("in main");

	}

}
