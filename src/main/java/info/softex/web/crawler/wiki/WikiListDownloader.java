package info.softex.web.crawler.wiki;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.impl.BasicLogPool;
import info.softex.web.crawler.impl.jobs.AbstractHtmlJob;
import info.softex.web.crawler.impl.runners.TextLinesJobRunner;
import info.softex.web.crawler.utils.DownloadUtils;
import info.softex.web.crawler.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @since version 1.0,	03/15/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class WikiListDownloader {
	
	public static final String HOST = "http://ru.m.wikipedia.org";
	//public static final String HOST = "http://uk.m.wikipedia.org";
	
	public static void main(String[] args) throws Exception {
		
//		if (args.length < 3) {
//			System.out.print("Arguments not found. ");
//			System.out.println("Expected: path/for/downloaded/files /path/to/articles_keys mobile_wiki_website");
//			System.out.println("Example: ./downloaded ./articles_keys.txt http://en.m.wikipedia.org");
//			return;
//		}
		
		String path = "/ext/wiki";
		String htmlPath = path + "/downloaded";
		String mediaPath = path + "/media";
		
		LogPool logPool = new BasicLogPool();
		logPool.setSuccessLogFile(new File(path + "/articles-succes.txt"));
		logPool.setErrorLogFile(new File(path + "/articles-error.txt"));
		logPool.setDebugLogFile(new File(path + "/articles-not-found.txt"));
		
		JobRunner runner = new TextLinesJobRunner(new File(path + "/articles_keys.txt"));
			
		runner.run(new DownloadWordsLineRunnable(logPool, htmlPath, mediaPath));
		
	}
	
	private static class DownloadWordsLineRunnable extends AbstractHtmlJob {
		
		public DownloadWordsLineRunnable(LogPool inlLogPool, String inOutHtmlPath, String inOutMediaPath) throws IOException {
			super(inlLogPool, inOutHtmlPath, inOutMediaPath);
		}

		@Override
		public boolean processItem(JobData jobData) throws IOException {

			String word = jobData.getContent();
			
			String link = WikiUtils.createLinkFromWord(HOST, word);
			
			File curFile = new File(outHtmlPath + File.separator + FileUtils.title2FileName(word));

			DownloadUtils.DownloadStatus status = DownloadUtils.download(curFile, link);
			if (status == DownloadUtils.DownloadStatus.NOT_FOUND) {
				logPool.logDebug(word + "\t: " + status);
				return false;
			} else if (status == DownloadUtils.DownloadStatus.ERROR) {
				logPool.logError(word + "\t: " + status);
				return false;
			} else {
				logPool.logSuccess(word);
//				if (status == DownloadUtils.DownloadStatus.DOWNLOADED) {
//					Thread.sleep(1500);
//				}
				return true;
			}

		}

	}

}
