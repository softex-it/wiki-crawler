package info.softex.web.crawler.impl.pools;

import info.softex.web.crawler.api.WriterPool;
import info.softex.web.crawler.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * Basic implementation of WriterPool.
 * 
 * @since version 2.1,	01/21/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class BasicWriterPool implements WriterPool {
	
	protected BufferedWriter writer1;
	protected BufferedWriter writer2;
	protected BufferedWriter writer3;
	protected BufferedWriter writer4;
	
	protected String contentDir;
	protected String mediaDir;
	
	private BasicWriterPool() {
	}

	@Override
	public void writeOutput1(String message) throws IOException {
		writer1.write(message);
		writer1.flush();
	}

	@Override
	public void writeOutput2(String message) throws IOException {
		writer2.write(message);
		writer2.flush();
	}
	
	@Override
	public void writeOutput3(String message) throws IOException {
		writer3.write(message);
		writer3.flush();
	}
	
	@Override
	public void writeOutput4(String message) throws IOException {
		writer4.write(message);
		writer4.flush();
	}
	
	@Override
	public void writeContentFile(String fileName, String content) throws IOException {
		FileUtils.string2File(contentDir + File.separator + fileName, content);
	}
	
	@Override
	public void writeMediaFile(String fileName, String content) throws IOException {
		FileUtils.string2File(mediaDir + File.separator + fileName, content);
	}
	
	@Override
	public String getContentDir() {
		return contentDir;
	}
	
	public String getMediaDir() {
		return mediaDir;
	}
	
	@Override
	public void close() throws IOException {
		if (writer1 != null) {
			writer1.flush();
			writer1.close();
		}
		if (writer2 != null) {
			writer2.flush();
			writer2.close();
		}
		if (writer3 != null) {
			writer3.flush();
			writer3.close();
		}
		if (writer4 != null) {
			writer4.flush();
			writer4.close();
		}
	}
	
	public BasicWriterPool outputFile1(String inPath) throws IOException {
		writer1 = createWriter(inPath);
		return this;
	}
	
	public BasicWriterPool outputFile2(String inPath) throws IOException {
		writer2 = createWriter(inPath);
		return this;
	}
	
	public BasicWriterPool outputFile3(String inPath) throws IOException {
		writer3 = createWriter(inPath);
		return this;
	}
	
	public BasicWriterPool outputFile4(String inPath) throws IOException {
		writer4 = createWriter(inPath);
		return this;
	}
	
	public BasicWriterPool outputContentDir(String inPath) {
		contentDir = createDirectory(inPath);
		return this;
	}
	
	public BasicWriterPool outputMediaDir(String inPath) {		
		mediaDir = createDirectory(inPath);
		return this;
	}
	
	public static BasicWriterPool create() {
		return new BasicWriterPool();
	}
	
	private static BufferedWriter createWriter(String inPath) throws IOException {
		if (inPath == null || inPath.isEmpty()) {
			throw new IllegalArgumentException("File path can't be blank");
		}
		return FileUtils.createWriter(new File(inPath));
	}

	protected static String createDirectory(String inPath) {
		if (inPath == null || inPath.isEmpty()) {
			throw new IllegalArgumentException("Directory path can't be blank");
		} else if (inPath.endsWith(File.separator)) {
			throw new IllegalArgumentException("Directory path must not end with file separator");
		}
		new File(inPath).mkdirs();
		return inPath;
	}
	
}
