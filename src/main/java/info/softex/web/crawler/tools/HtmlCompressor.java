package info.softex.web.crawler.tools;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.BasicLogPool;
import info.softex.web.crawler.impl.jobs.CompressorHtmlJob;
import info.softex.web.crawler.impl.runners.SourceLinesJobRunner;

import java.io.File;

/**
 * 
 * @since version 1.0,	04/05/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class HtmlCompressor {
	
	public static void main(String[] args) throws Exception {
		JobRunner runner = new SourceLinesJobRunner(new File ("/ext/wikip/articles.txt"));
		JobRunnable job = new CompressorHtmlJob(new BasicLogPool(), new File("/ext/wikip/articles-condensed.txt"));
		runner.run(job);
	}

}
