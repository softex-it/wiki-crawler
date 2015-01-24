package info.softex.web.crawler.wiki;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.jobs.HtmlToSourceConverterJob;
import info.softex.web.crawler.impl.runners.HtmlFilesJobRunner;

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
			"/ext/wiki/articles_html"
		);
		
		JobRunnable htmlToSourceJob = new HtmlToSourceConverterJob(
			"/ext/wiki/articles.txt"
			// "/ext/articles.txt"
		);
		
		runner.run(htmlToSourceJob);
		
	}

}
