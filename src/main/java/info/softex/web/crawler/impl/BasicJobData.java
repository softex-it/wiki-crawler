package info.softex.web.crawler.impl;

import info.softex.web.crawler.api.JobData;

/**
 * 
 * @since version 1.0,	03/18/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class BasicJobData implements JobData {
	
	private String content = null;
	private String title = null;	
	
	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTitle(String inTitle) {
		this.title = inTitle;
	}

}
