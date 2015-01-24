package info.softex.web.crawler.impl.pools;

import info.softex.web.crawler.api.WriterPool;
import info.softex.web.crawler.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @since version 2.0,	01/21/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class BasicWriterPool implements WriterPool {
	
	protected BufferedWriter outWriter1;
	protected BufferedWriter outWriter2;
	
	protected String outHtmlPath;
	protected String outMediaPath;
	
	private BasicWriterPool() {
	}

	@Override
	public void writeOutput1(String message) throws IOException {
		outWriter1.write(message);
		outWriter1.flush();
	}

	@Override
	public void writeOutput2(String message) throws IOException {
		outWriter2.write(message);
		outWriter2.flush();
	}
	
	@Override
	public String getHtmlFolderPath() {
		return outHtmlPath;
	}
	
	public String getMediaFolderPath() {
		return outMediaPath;
	}
	
	@Override
	public void close() throws IOException {
		if (outWriter1 != null) {
			outWriter1.flush();
			outWriter1.close();
		}
		if (outWriter2 != null) {
			outWriter2.flush();
			outWriter2.close();
		}
	}
	
	public BasicWriterPool outputFile1(String inPath1) throws IOException {
		if (inPath1 == null || inPath1.isEmpty()) {
			throw new IllegalArgumentException("File Path 1 can't be empty");
		}
		outWriter1 = FileUtils.createWriter(new File(inPath1));
		return this;
	}
	
	public BasicWriterPool outputFile2(String inPath2) throws IOException {
		if (inPath2 == null || inPath2.isEmpty()) {
			throw new IllegalArgumentException("File Path 2 can't be empty");
		}
		outWriter2 = FileUtils.createWriter(new File(inPath2));
		return this;
	}
	
	public BasicWriterPool outputHtmlPath(String inPath) throws IOException {
		if (inPath == null || inPath.isEmpty()) {
			throw new IllegalArgumentException("HTML path can't be empty");
		}
		outHtmlPath = inPath;
		
		new File(inPath).mkdirs();
		
		return this;
	}
	
	public BasicWriterPool outputMediaPath(String inPath) throws IOException {
		if (inPath == null || inPath.isEmpty()) {
			throw new IllegalArgumentException("HTML path can't be empty");
		}
		outMediaPath = inPath;
		
		new File(inPath).mkdirs();
		
		return this;
	}
	
	public static BasicWriterPool create() {
		return new BasicWriterPool();
	}

}
