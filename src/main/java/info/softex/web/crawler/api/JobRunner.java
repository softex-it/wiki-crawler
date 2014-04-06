package info.softex.web.crawler.api;

/**
 * 
 * @since version 1.0,	03/17/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public interface JobRunner {
	
	public final static String UTF8 = "UTF-8";

	public void run(JobRunnable job) throws Exception;
	
}
