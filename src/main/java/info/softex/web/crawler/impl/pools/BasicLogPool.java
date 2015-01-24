package info.softex.web.crawler.impl.pools;

import static info.softex.web.crawler.utils.FileUtils.closeWriter;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @since version 1.0,	03/18/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class BasicLogPool implements LogPool {
	
	protected BufferedWriter logSuccessWriter;
	protected BufferedWriter logErrorWriter;
	protected BufferedWriter logDebugWriter;
	
	//protected BufferedWriter logImageErrorWriter;
	protected BufferedWriter logImageDebugWriter;

	public BasicLogPool() throws IOException {
	}
	
	public BasicLogPool(File logSuccessFile, File logErrorFile, File logDebugFile) throws IOException {
		this.logSuccessWriter = FileUtils.createWriter(logSuccessFile);
		setErrorLogFile(logErrorFile);
		setDebugLogFile(logDebugFile);
	}
	
	@Override
	public void close() throws IOException {
		closeWriter(logSuccessWriter);
		closeWriter(logErrorWriter);
		closeWriter(logDebugWriter);
		closeWriter(logImageDebugWriter);
	}
	
	@Override
	public void setSuccessLogFile(File inFile) throws IOException {
		logSuccessWriter = FileUtils.createWriter(inFile);
	}

	@Override
	public void setErrorLogFile(File inFile) throws IOException {
		logErrorWriter = FileUtils.createWriter(inFile);
	}
	
	@Override
	public void setDebugLogFile(File inFile) throws IOException {
		logDebugWriter = FileUtils.createWriter(inFile);
	}
	
	@Override
	public void setImageDebugLogFile(File inFile) throws IOException {
		logImageDebugWriter = FileUtils.createWriter(inFile);
	}
	
	@Override
	public void logError(String message) throws IOException {
		logErrorWriter.write(message + "\r\n");
		logErrorWriter.flush();
	}
	
	@Override
	public void logSuccess(String message) throws IOException {
		logSuccessWriter.write(message + "\r\n");
		logSuccessWriter.flush();
	}
	
	@Override
	public void logDebug(String message) throws IOException {
		logDebugWriter.write(message + "\r\n");
		logDebugWriter.flush();
	}

	@Override
	public void logImageDebug(String message) throws IOException {
		logImageDebugWriter.write(message + "\r\n");
		logImageDebugWriter.flush();
	}

}
