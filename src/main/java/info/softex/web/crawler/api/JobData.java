package info.softex.web.crawler.api;

import java.util.Set;

/**
 * 
 * @since version 1.0,		03/18/2014
 * 
 * @modified version 2.2,	04/16/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public interface JobData {
	
	public String getTitle();
	
	public String getContent();
	
	public Set<String> getContentSet();
	
}
