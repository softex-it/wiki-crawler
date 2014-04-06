package info.softex.web.crawler.impl.runners;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.filters.HtmlFileFilter;
import info.softex.web.crawler.impl.BasicJobData;
import info.softex.web.crawler.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 1.0,	03/17/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class HtmlFilesJobRunner implements JobRunner {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	protected final String inHtmlPath;
	
	protected final FileFilter fileFilter;
	
	public HtmlFilesJobRunner(String inInHtmlPath) throws IOException {
		this(inInHtmlPath, new HtmlFileFilter());
	}
	
	public HtmlFilesJobRunner(String inInHtmlPath, FileFilter inFileFilter) throws IOException {
		this.inHtmlPath = inInHtmlPath;
		this.fileFilter = inFileFilter;
	}

	@Override
	public void run(JobRunnable job) throws Exception {
		
		long t1 = System.currentTimeMillis();
		
		log.info("Reading files from: {}", inHtmlPath);
		
		// HTML file list
		File[] htmlFiles = null;
		if (fileFilter != null) {
			htmlFiles = new File(inHtmlPath).listFiles(fileFilter);
		} else {
			htmlFiles = new File(inHtmlPath).listFiles();
		}
		
		log.info("Finished reading files from: {}", inHtmlPath);
		
		job.injectData((Object) htmlFiles);
		
		int totalFiles = htmlFiles.length;
		int ignoredFiles = 0;
		
		BasicJobData jobData = new BasicJobData();
		
		for (int i = 0; i < htmlFiles.length; i++) {
			
			if (i % 50000 == 0 && i > 0) {
				long partTime = (System.currentTimeMillis() - t1) / 1000;
				log.info("Processed Items: {}. Total Time: {} sec", i, partTime);
			}

			File inFile = htmlFiles[i];
			
			jobData.setContent(FileUtils.file2String(inFile));
			jobData.setTitle(FileUtils.fileName2Title(inFile.getName()));
			
			boolean isProcessed = job.processItem(jobData);
			
			if (!isProcessed) {
				ignoredFiles++;
			}

		}
		
		job.finish();
		
		int processedFiles = totalFiles - ignoredFiles;
		long time = (System.currentTimeMillis() - t1) / 1000;
		log.info("Processing complete. Time: {} sec. Files ignored: {}, processed: {}, total: {}", 
			time, ignoredFiles, processedFiles, totalFiles
		); 
		
	}
	


}
