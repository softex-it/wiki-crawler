package info.softex.web.crawler.impl.injectors;

import info.softex.web.crawler.api.DataInjector;

import java.io.File;
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
public class FilesLowerCaseToFilesMapInjector implements DataInjector {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private final File[] htmlFiles;
	
	public FilesLowerCaseToFilesMapInjector(File[] inFiles) {
		this.htmlFiles = inFiles;
	}

	@Override
	public void inject(Map<DataInjector.DataKey, Object> dataMap) {

		HashMap<String, String> words2FilesMap = new HashMap<>();
		
		log.info("Started creating word mapper");
		for (int i = 0; i < htmlFiles.length; i++) {
			words2FilesMap.put(htmlFiles[i].getName().toLowerCase(), htmlFiles[i].getName());
		}
		log.info("Finished creating word mapper. Number of words: {}", words2FilesMap.size());
		
		dataMap.put(DataInjector.DataKey.FILES_LC_TO_FILES_MAP, words2FilesMap);
		
		log.info("Data injection is completed");
		
	}
	
}
