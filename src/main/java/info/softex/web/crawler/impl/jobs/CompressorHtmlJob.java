package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.utils.ConversionUtils;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @since version 1.0,	04/05/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class CompressorHtmlJob extends AbstractHtmlJob {

	public CompressorHtmlJob(LogPool inLogPool, File inOutFile) throws IOException {
		super(inLogPool, inOutFile);
	}
	
	@Override
	public boolean processItem(JobData jobData) throws Exception {
		String title = jobData.getTitle();
		String content = jobData.getContent();
		
		String output = title + "  " + ConversionUtils.compressHtml(content);
		writeOutput2Writer(output);
		
		return true;
	}

}
