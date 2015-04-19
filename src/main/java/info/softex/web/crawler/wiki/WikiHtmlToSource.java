package info.softex.web.crawler.wiki;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.jobs.HtmlToSourceJob;
import info.softex.web.crawler.impl.runners.FilesJobRunner;

/**
 * 
 * @since version 1.0,		03/18/2014
 * 
 * @modified version 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class WikiHtmlToSource {
	
	public static void main(String[] args) throws Exception {
		
		JobRunner runner = new FilesJobRunner(
			"/ext/wiki/articles_html"
		);
		
		JobRunnable htmlToSourceJob = new HtmlToSourceJob(
			"/ext/wiki/articles.txt",
			// "/ext/articles.txt",
			true
		);
		
		runner.run(htmlToSourceJob);
		
	}

}
