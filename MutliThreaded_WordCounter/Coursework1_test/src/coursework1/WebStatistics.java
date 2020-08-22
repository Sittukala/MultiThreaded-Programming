package coursework1;

import java.util.Vector;

/**
 * @author Yi Hong
 * CO3090/CO7090 assignment 1 
 *     
 * Interface WebStatistics     
 */


public interface WebStatistics {
		 
	 /**
		 * find (keyword,URL) method should  
		 * 
		 *    (1) mar URL 'visited' by adding it to the "visited" list
		 *    (2) if a given URL contains the k achosen keyword 
		 *        then the you should 
		 *           (a) add this URL to result set and 
		 *           (b) record how many times the keyword occurs on this page.
		 *    If the keyword does not occur then skip (a) and (b)
		 *        
		 *  This method should skip URLs that have been visited by other threads 
		 *  
		 * @param keyword  a string for case-insensitive keyword search
		 * @param URL      starting from this seed page
		 * @throws InterruptedException  
		 */
	
	public void find(String word, String URL) 
			   throws InterruptedException; 

		/**
		 * printStatistics()  :  used to print results   
		 *                            
		 *               
		 * Sample output:
		 *  
		 *
		   Word: 'Software' appears
		     	     
			2 time(s) on http://www..../page1.html
			2 time(s) on http://www..../page1.1.html
			3 time(s) on http://www..../page1.2.html
			....
	
			Total: 11 pages
		 	'Software' appears 43 time(s)  
		 	
		 (results may vary depending on your crawling strategy)
		 *               
		 * @throws InterruptedException
		 * @return   
		 *   
		 */
	public void printStatistics() 
			 throws KeywordNotProvidedException, 
			 	    IllegalURLException;


}
