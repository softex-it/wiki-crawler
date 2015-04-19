package info.softex.web.crawler.impl.runners;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.BasicJobData;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 1.0,	03/22/2014
 * 
 * @since modified 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class TextLinesJobRunner implements JobRunner {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final File inFile;
	private final BufferedReader reader;
	
	public TextLinesJobRunner(File inInFile) throws IOException {
		this.inFile = inInFile;
		this.reader = Files.newBufferedReader(inFile.toPath(), Charset.forName(UTF8));
	}
	
	public TextLinesJobRunner(String inFilePath) throws IOException {
		this(new File(inFilePath));
	}

	@Override
	public void run(JobRunnable job) throws Exception {
		
		long t1 = System.currentTimeMillis();
		
		log.info("Processing lines from: {}", inFile);
		
		String currentLine = null;
		
		int totalItems = 0;
		int ignoredItems = 0;
		
		BasicJobData jobData = new BasicJobData();
		
		while ((currentLine = reader.readLine()) != null) {
			if (totalItems % 50000 == 0 && totalItems > 0) {
				long partTime = (System.currentTimeMillis() - t1) / 1000;
				log.info("Processed Items: {}. Total Time: {} sec", totalItems, partTime);
			}
			
			totalItems++;
			populateJobData(jobData, currentLine);
			boolean isProcessed = job.processItem(jobData);
			if (!isProcessed) {
				ignoredItems++;
			}
		}
		
		reader.close();
		
		job.finish();
		
		int processedItems = totalItems - ignoredItems;
		long time = (System.currentTimeMillis() - t1) / 1000;
		log.info("Processing complete. Time: {} sec. Items ignored: {}, processed: {}, total: {}", 
			time, ignoredItems, processedItems, totalItems
		);
		
	}
	
	protected boolean populateJobData(BasicJobData inJobData, String inLine) {
		inJobData.setContent(inLine);
		return true;
	}

}
