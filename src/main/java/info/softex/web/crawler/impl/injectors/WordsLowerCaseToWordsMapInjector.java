package info.softex.web.crawler.impl.injectors;

import info.softex.web.crawler.api.DataInjector;
import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.jobs.AbstractJob;
import info.softex.web.crawler.impl.pools.BasicLogPool;
import info.softex.web.crawler.impl.pools.BasicWriterPool;
import info.softex.web.crawler.impl.runners.TextLinesJobRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 2.1,	01/25/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class WordsLowerCaseToWordsMapInjector implements DataInjector {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private final String filePath;
	
	public WordsLowerCaseToWordsMapInjector(String inFilePath) {
		this.filePath = inFilePath;
	}

	@Override
	public void inject(Map<DataInjector.DataKey, Object> dataMap) throws Exception {
		
		HashMap<String, String> words2Keys = new HashMap<>();
		
		log.info("Reading mapper keys from file: {}", filePath);
		
		JobRunner runner = new TextLinesJobRunner(filePath);
		runner.run(new KeysToMapJob(words2Keys));
		
		log.info("Finished reading mapper keys. Total items read: {}", words2Keys.size());

		dataMap.put(DataInjector.DataKey.WORDS_LC_TO_WORDS_MAP, words2Keys);
		
		log.info("Data injection is completed");
		
	}

	private static class KeysToMapJob extends AbstractJob {

		private final Map<String, String> words;
		
		public KeysToMapJob(Map<String, String> inWords) throws IOException {
			super(new BasicLogPool(), BasicWriterPool.create());
			this.words = inWords;
		}

		@Override
		public boolean processItem(JobData jobData) throws Exception {
		
			String line = jobData.getContent();
			if (line == null || line.trim().isEmpty()) {
				return false;
			}
			
			words.put(line.toLowerCase(), line);
			
			return true;
			
		}

	}
	
	
}
