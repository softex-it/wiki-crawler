package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.api.WriterPool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 2.0,	01/21/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public abstract class AbstractJob implements JobRunnable {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected final WriterPool writerPool;
	
	protected final LogPool logPool;
	
	protected final HashMap<String, String> wordFileMap = new HashMap<String, String>();
	
	protected final Set<String> imagesRemovedSet = new HashSet<String>(); 
	
	protected int itemsProcessed = 0;
	
	protected int linksLinked = 0;
	protected int linksRemoved = 0;
	protected int linksTotal = 0;
	protected int linksJump = 0;
	protected int linksExternal = 0;
	
	protected int soundsLinked = 0;
	protected int soundsRemoved = 0;
	protected int soundsTotal = 0;
	
	protected int imagesLinked = 0;
	protected int imagesRemoved = 0;
	protected int imagesTotal = 0;
	
	public AbstractJob(LogPool inLogPool, WriterPool inWriterPool) throws IOException {
		this.logPool = inLogPool;
		this.writerPool = inWriterPool;
	}
	
	@Override
	public void injectData(Object... data) throws Exception {
		
		File[] htmlFiles = (File[]) data[0];

		log.info("Started creating word mapper");
		for (int i = 0; i < htmlFiles.length; i++) {
			wordFileMap.put(htmlFiles[i].getName().toLowerCase(), htmlFiles[i].getName());
		}
		log.info("Finished creating word mapper");
		
	}
	
	@Override
	public void finish() throws IOException {
		
		writerPool.close();
		
		log.info("Job is finished");
		log.info(
			"Links | Total/Jump/External: {}/{}/{}; Linked: {}; Removed: {}", 
			linksTotal, linksJump, linksExternal, linksLinked, linksRemoved
		);
		log.info(
			"Images | Total: {}; Linked: {}; Removed: {}", 
			imagesTotal, imagesLinked, imagesRemoved
		);
		log.info(
			"Sounds | Total: {}; Linked: {}; Removed: {}", 
			soundsTotal, soundsLinked, soundsRemoved
		);
	}
	
	protected String getWordFileMappedName(String inFileName) throws Exception {
		return wordFileMap.get(inFileName.toLowerCase());
	}
	
	protected void writeOutput1(String output) throws IOException {
		writerPool.writeOutput1(output + "\r\n");
	}
	
	protected void writeOutput2(String output) throws IOException {
		writerPool.writeOutput2(output + "\r\n");
	}

}
