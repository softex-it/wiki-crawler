package info.softex.web.crawler.impl;

import java.util.Set;

import info.softex.web.crawler.api.JobData;

/**
 * 
 * @since version 1.0,	03/18/2014
 * 
 * @modified version 2.2,	04/16/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class BasicJobData implements JobData {
	
	private String content = null;
	private String title = null;	
	
	private Set<String> contentSet = null;	
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public String getContent() {
		return content;
	}
	
	@Override
	public Set<String> getContentSet() {
		return contentSet;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTitle(String inTitle) {
		this.title = inTitle;
	}
	
	public void setContentSet(Set<String> inContentSet) {
		this.contentSet = inContentSet;
	}

}
