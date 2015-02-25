package info.softex.web.crawler.api;

import java.io.IOException;

/**
 * The class provides interface for universal writer pool.
 * Writers can be loosely used per discretion of a job or not 
 * used if they are not need.
 * 
 * @since version 2.1,	01/21/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public interface WriterPool {
	
	public void writeOutput1(String message) throws IOException;
	public void writeOutput2(String message) throws IOException;
	public void writeOutput3(String message) throws IOException;
	public void writeOutput4(String message) throws IOException;
	
	public void writeContentFile(String fileName, String content) throws IOException;
	public void writeMediaFile(String fileName, String content) throws IOException;
	
	public String getContentDir();
	public String getMediaDir();
	
	public void close() throws IOException;
	
}
