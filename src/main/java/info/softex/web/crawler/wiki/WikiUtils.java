package info.softex.web.crawler.wiki;

import info.softex.web.crawler.utils.UrlUtils;

/**
 * 
 * @since version 1.0,	03/16/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class WikiUtils {
	
	public static String createLinkFromWord(String host, String word) {
		word = word.replaceAll(" ", "_");
		return host + "/wiki/" + UrlUtils.encodeURL(word);
	}

}
