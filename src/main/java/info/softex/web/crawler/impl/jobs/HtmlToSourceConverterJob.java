package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.impl.pools.BasicLogPool;
import info.softex.web.crawler.impl.pools.BasicWriterPool;
import info.softex.web.crawler.utils.ConversionUtils;

import java.io.IOException;

/**
 * 
 * @since version 1.0,	03/18/2014
 * 
 * @modified version 2.0,	01/21/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class HtmlToSourceConverterJob extends AbstractHtmlJob {

	public HtmlToSourceConverterJob(String inPath) throws IOException {
		super(new BasicLogPool(), BasicWriterPool.create().outputFile1(inPath));
	}
	
	@Override
	public void injectData(Object... data) throws Exception {}
	
	@Override
	public boolean processItem(JobData jobData) throws Exception {
		
		String title = jobData.getTitle();
		String article = ConversionUtils.removeLineBreaks(jobData.getContent());
		article = ConversionUtils.compressHtml(article);
		
		writeOutput1(title + "  " + article);
		
		return true;
		
	}
	
}