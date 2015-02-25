package info.softex.web.crawler.impl.runners;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.filters.HtmlFileFilter;
import info.softex.web.crawler.impl.BasicJobData;
import info.softex.web.crawler.impl.injectors.FilesLowerCaseToFilesMapInjector;
import info.softex.web.crawler.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 1.0,		03/17/2014
 * 
 * @modified version 2.0,	02/13/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class FilesJobRunner implements JobRunner {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	protected final String inPath;
	
	protected final FileFilter fileFilter;
	
	public FilesJobRunner(String inInHtmlPath) throws IOException {
		this(inInHtmlPath, new HtmlFileFilter());
	}
	
	public FilesJobRunner(String inInHtmlPath, FileFilter inFileFilter) throws IOException {
		this.inPath = inInHtmlPath;
		this.fileFilter = inFileFilter;
	}

	@Override
	public void run(JobRunnable job) throws Exception {
		
		long t1 = System.currentTimeMillis();
		
		// Validate the input directory
		File inDirectory = new File(inPath);
		if (!inDirectory.exists()) {
			throw new IllegalArgumentException("Input directory doesn't exist: " + inPath);
		}
		if (inDirectory.isFile()) {
			throw new IllegalArgumentException("Input file was found but directory is expected: " + inPath);
		}
		
		log.info("Reading files from: {}. It may take a few minutes.", inPath);
		
		// HTML file list
		File[] inFiles = null;
		if (fileFilter != null) {
			inFiles = new File(inPath).listFiles(fileFilter);
		} else {
			inFiles = new File(inPath).listFiles();
		}
		
		log.info("Finished reading files from: {}. Number of files: {}", inPath, inFiles.length);
		
		job.injectData(new FilesLowerCaseToFilesMapInjector(inFiles));
		
		int totalFiles = inFiles.length;
		int ignoredFiles = 0;
		
		BasicJobData jobData = new BasicJobData();
		
		for (int i = 0; i < inFiles.length; i++) {
			
			if (i % 50000 == 0 && i > 0) {
				long partTime = (System.currentTimeMillis() - t1) / 1000;
				log.info("Processed Items: {}. Total Time: {} sec", i, partTime);
			}

			File inFile = inFiles[i];
			
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
