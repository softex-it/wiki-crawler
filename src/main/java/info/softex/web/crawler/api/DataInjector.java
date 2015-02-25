package info.softex.web.crawler.api;

import java.util.Map;

/**
 * 
 * @since version 2.1,	01/25/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public interface DataInjector {

	public void inject(Map<DataInjector.DataKey, Object> dataMap) throws Exception;
	
	public static enum DataKey {
		FILES_LC_TO_FILES_MAP,
		WORDS_LC_TO_WORDS_MAP
	}
	
}
