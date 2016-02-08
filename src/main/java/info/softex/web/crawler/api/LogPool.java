package info.softex.web.crawler.api;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @since version 1.0,		03/23/2014
 * 
 * @modified version 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public interface LogPool {
	
	public final static String UTF8 = "UTF-8";
	
	public void logSuccess(String message) throws IOException;
	public void logError(String message) throws IOException;
	public void logDebug(String message) throws IOException;
	public void logImageDebug(String message) throws IOException;

	public LogPool successFile(String inPath) throws IOException;
	public LogPool successFile(File inFile) throws IOException;
	public LogPool errorFile(String inPath) throws IOException;
	public LogPool errorFile(File inFile) throws IOException;
	public LogPool debugFile(String inPath) throws IOException;
	public LogPool debugFile(File inFile) throws IOException;
	public LogPool imageDebugFile(File inFile) throws IOException;
	
	public void close() throws IOException;

}
