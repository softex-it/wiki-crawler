package info.softex.web.crawler.wiki;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.jobs.HtmlToSourceConverterJob;
import info.softex.web.crawler.impl.runners.HtmlFilesJobRunner;

import java.io.File;

/**
 * 
 * @since version 1.0,	03/18/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class WikiHtmlToSource {
	
	public static void main(String[] args) throws Exception {
		
		JobRunner runner = new HtmlFilesJobRunner(
			"/ext/wiki/processed"
		);
		
		JobRunnable htmlToSourceJob = new HtmlToSourceConverterJob(
			new File("/ext/wiki/articles.txt")
			// new File("/ext/articles.txt")
		);
		
		runner.run(htmlToSourceJob);
		
	}

}
