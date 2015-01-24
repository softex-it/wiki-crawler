package info.softex.web.crawler.api;

import java.io.IOException;

/**
 * 
 * @since version 2.0,	01/21/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public interface WriterPool {
	
	public void writeOutput1(String message) throws IOException;
	public void writeOutput2(String message) throws IOException;
	
	public String getHtmlFolderPath();
	public String getMediaFolderPath();
	
	public void close() throws IOException;
	
}
