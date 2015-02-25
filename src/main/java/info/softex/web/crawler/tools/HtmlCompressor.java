package info.softex.web.crawler.tools;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.jobs.HtmlToSourceJob;
import info.softex.web.crawler.impl.runners.SourceLinesJobRunner;

import java.io.File;

/**
 * 
 * @since version 1.0,	04/05/2014
 * 
 * @since modified 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class HtmlCompressor {
	
	public static void main(String[] args) throws Exception {
		JobRunner runner = new SourceLinesJobRunner("/ext/wikip/articles.txt");
		JobRunnable job = new HtmlToSourceJob(
			"/ext/wikip/articles-condensed.txt",
			true
		);
		
		runner.run(job);
	}

}
