package info.softex.web.crawler.api;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @since version 1.0,	03/23/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public interface LogPool {
	
	public void logSuccess(String message) throws IOException;
	public void logError(String message) throws IOException;
	public void logDebug(String message) throws IOException;
	public void logImageDebug(String message) throws IOException;
	
	public void setSuccessLogFile(File inFile) throws IOException;
	public void setErrorLogFile(File inFile) throws IOException;
	public void setDebugLogFile(File inFile) throws IOException;
	public void setImageDebugLogFile(File inFile) throws IOException;
	
	public void close() throws IOException;

}
