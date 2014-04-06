package info.softex.web.crawler.api;

/**
 * 
 * @since version 1.0,	03/17/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public interface JobRunnable {
	
	public static final String UTF8 = "UTF-8";
	
	public void injectData(Object ... data) throws Exception;
	
	public boolean processItem(JobData jobData) throws Exception;
	
	public void finish() throws Exception;

}
