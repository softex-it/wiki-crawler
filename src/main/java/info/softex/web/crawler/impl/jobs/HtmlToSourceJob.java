package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.impl.pools.BasicLogPool;
import info.softex.web.crawler.impl.pools.BasicWriterPool;
import info.softex.web.crawler.utils.ConversionUtils;

import java.io.IOException;

/**
 * 
 * @since version 1.0,	04/05/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class HtmlToSourceJob extends AbstractJob {
	
	private final boolean compressHtml;

	public HtmlToSourceJob(String filePath, boolean inCompressHtml) throws IOException {
		super(new BasicLogPool(), BasicWriterPool.create().outputFile1(filePath));
		this.compressHtml = inCompressHtml;
	}
	
	@Override
	public boolean processItem(JobData jobData) throws Exception {
		String title = jobData.getTitle();
		String article = jobData.getContent();
		
		article = ConversionUtils.removeLineBreaks(article);
		
		if (compressHtml) {
			article = ConversionUtils.compressHtml(article);
		}

		writeOutput1(title + "  " + article);
		
		return true;
	}

}
