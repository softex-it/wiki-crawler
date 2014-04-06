package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.impl.BasicLogPool;
import info.softex.web.crawler.utils.ConversionUtils;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @since version 1.0,	03/18/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class HtmlToSourceConverterJob extends AbstractHtmlJob {

	public HtmlToSourceConverterJob(File inOutFile) throws IOException {
		super(new BasicLogPool(), inOutFile);
	}
	
	@Override
	public void injectData(Object... data) throws Exception {}
	
	@Override
	public boolean processItem(JobData jobData) throws Exception {
		
		String title = jobData.getTitle();
		String article = ConversionUtils.removeLineBreaks(jobData.getContent());
		
		outWriter.write(title + "  " + article + "\r\n");
		outWriter.flush();
		
		return true;
	}
	
}