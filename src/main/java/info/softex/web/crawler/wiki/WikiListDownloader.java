package info.softex.web.crawler.wiki;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.impl.BasicLogPool;
import info.softex.web.crawler.impl.jobs.AbstractHtmlJob;
import info.softex.web.crawler.impl.runners.TextLinesJobRunner;
import info.softex.web.crawler.utils.ConversionUtils;
import info.softex.web.crawler.utils.DownloadUtils;

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
	
	public static void main(String[] args) throws Exception {
		
		String path = "/ext/wiki";
		String dwlPath = path + "/downloaded";
		
		LogPool logPool = new BasicLogPool();
		logPool.setSuccessLogFile(new File(path + "/words-succes.txt"));
		logPool.setErrorLogFile(new File(path + "/words-error.txt"));
		logPool.setDebugLogFile(new File(path + "/words-not-found.txt"));
		
		JobRunner runner = new TextLinesJobRunner(new File(path + "/words.txt"));
			
		runner.run(new DownloadWordsLineRunnable(logPool, dwlPath));
		
	}
	
	private static class DownloadWordsLineRunnable extends AbstractHtmlJob {
		
		public DownloadWordsLineRunnable(LogPool inlLogPool, String inDwlPath) throws IOException {
			super(inlLogPool, "", "");
		}

		@Override
		public boolean processItem(JobData jobData) throws IOException {

			String word = jobData.getContent();
			
			String link = WikiUtils.createLinkFromWord(HOST, word);
			
			File curFile = new File(outHtmlPath + File.separator + ConversionUtils.mapWordToFileName(word));

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
