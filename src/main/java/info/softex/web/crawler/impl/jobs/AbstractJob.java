package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.DataInjector;
import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.api.WriterPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 2.0,		01/21/2015
 * 
 * @modified version 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public abstract class AbstractJob implements JobRunnable {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected final WriterPool writerPool;
	protected final LogPool logPool;
	
	protected final HashMap<DataInjector.DataKey, Object> injectedData = new HashMap<>();
	
	protected final Set<String> imagesRemovedSet = new LinkedHashSet<String>(); 
	
	protected int itemsProcessed = 0;
	
	protected Map<String, String> lcFiles;
	protected Map<String, String> lcWords;
	protected Set<String> matches;
	
	public AbstractJob(LogPool inLogPool, WriterPool inWriterPool) throws IOException {
		this.logPool = inLogPool;
		this.writerPool = inWriterPool;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public JobRunnable injectData(DataInjector dataInjector) throws Exception {
		
		log.info("Injecting data with {}", dataInjector.getClass().getName());
		
		dataInjector.inject(injectedData);
		
		lcFiles = (Map<String, String>) injectedData.get(DataInjector.DataKey.FILES_LC_TO_FILES_MAP);
		lcWords = (Map<String, String>) injectedData.get(DataInjector.DataKey.WORDS_LC_TO_WORDS_MAP);
		matches = (Set<String>) injectedData.get(DataInjector.DataKey.MATCHING_SET);
		
		return this;
	}
	
	@Override
	public void finish() throws IOException {
		
		writerPool.close();
		
		log.info("Job is finished");

	}
	
	
	protected String getFileByLowerCaseName(String inFileName) throws Exception {		
		return lcFiles.get(inFileName);
	}

	protected String getWordByLowerCaseName(String inFileName) throws Exception {		
		return lcWords.get(inFileName);
	}
	
	protected boolean containsMatch(String match) {		
		return matches.contains(match);
	}
	
	protected void writeOutput1(String output) throws IOException {
		writerPool.writeOutput1(output + "\r\n");
	}
	
	protected void writeOutput2(String output) throws IOException {
		writerPool.writeOutput2(output + "\r\n");
	}
	
	protected void writeOutput3(String output) throws IOException {
		writerPool.writeOutput3(output + "\r\n");
	}
	
	protected void writeOutput4(String output) throws IOException {
		writerPool.writeOutput4(output + "\r\n");
	}

}
