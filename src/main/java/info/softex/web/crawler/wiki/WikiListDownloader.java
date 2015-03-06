package info.softex.web.crawler.wiki;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.api.WriterPool;
import info.softex.web.crawler.impl.jobs.AbstractHtmlJob;
import info.softex.web.crawler.impl.pools.BasicLogPool;
import info.softex.web.crawler.impl.pools.BasicWriterPool;
import info.softex.web.crawler.impl.runners.TextLinesJobRunner;
import info.softex.web.crawler.utils.DownloadUtils;
import info.softex.web.crawler.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Wiki Downloader that goes over the list of Wiki keys and downloads pages.
 * The keys can be extracted from Wiki XML dump with Dictan-Converter.
 * All Wiki XML files are available at http://dumps.wikimedia.org
 * EN: http://dumps.wikimedia.org/enwiki/
 * RU: http://dumps.wikimedia.org/ruwiki/
 * 
 * Usually the right base to download is marked as 
 * "Recombine all pages, current versions only."
 * 
 * @since version 1.0,	03/15/2014
 * 
 * @since modified 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class WikiListDownloader {
	
	//public static final String HOST = "http://ru.m.wikipedia.org";
	//public static final String HOST = "http://uk.m.wikipedia.org";
	
	public static void main(String[] args) throws Exception {
		
		WikiListDownloaderCLI cli = new WikiListDownloaderCLI(args);
		
		String path = cli.getOutputDir();
		String htmlPath = path + "/downloaded";
		String mediaPath = path + "/media";
		
		LogPool logPool = BasicLogPool.create().
			errorFile(path + "/articles-error.txt").
			successFile(path + "/articles-succes.txt").
			debugFile(path + "/articles-not-found.txt");
		
		WriterPool writerPool = BasicWriterPool.create().
			outputContentDir(htmlPath).
			outputMediaDir(mediaPath);
		
		JobRunner runner = new TextLinesJobRunner(cli.getInputFile());
			
		runner.run(new DownloadWordsLineRunnable(cli.getHttpURL(), logPool, writerPool));
		
	}
	
	private static class DownloadWordsLineRunnable extends AbstractHtmlJob {
		
		protected final String httpUrl;
		
		public DownloadWordsLineRunnable(String inHttpUrl, LogPool inlLogPool, WriterPool inWriterPool) throws IOException {
			super(inlLogPool, inWriterPool);
			this.httpUrl = inHttpUrl;
		}

		@Override
		public boolean processItem(JobData jobData) throws IOException {

			String word = jobData.getContent();
			
			String link = WikiUtils.createLinkFromWord(httpUrl, word);
			
			File curFile = new File(writerPool.getContentDir() + File.separator + FileUtils.title2FileName(word));
			
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
	
	private static class WikiListDownloaderCLI {
		
		protected String inputFile;
		protected String outputDir;
		protected String httpURL;
		
		@SuppressWarnings("static-access")
		public WikiListDownloaderCLI(String[] args) throws ParseException, IOException {
			
			// Create Options object
			Options options = new Options();
			
			options.addOption(
				OptionBuilder.withDescription("input file with words to download").
				hasArg().isRequired().create("i"));

			options.addOption(
				OptionBuilder.withDescription("output folder for the downloaded files and logs").
				hasArg().isRequired().create("o"));

			options.addOption(
				OptionBuilder.withDescription("http url to use for downloading").
				hasArg().isRequired().create("u"));
			
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);
			
			inputFile = cmd.getOptionValue("i");
			outputDir = cmd.getOptionValue("o");
			httpURL = cmd.getOptionValue("u");
			
			if (!FileUtils.fileExists(new File(inputFile))) {
				new IllegalArgumentException("Input file is not found: " + inputFile);
			}
			
			File outputDirFile = new File(outputDir);
			if (!outputDirFile.exists()) {
				outputDirFile.mkdirs();
			}
			
			if (!httpURL.startsWith("http")) {
				new IllegalArgumentException("HTTP URL for download is defined incorrectly: " + httpURL);
			}
			
		}
		
		public String getInputFile() {
			return inputFile;
		}
		
		public String getOutputDir() {
			return outputDir;
		}
		
		public String getHttpURL() {
			return httpURL;
		}
		
	}

}
