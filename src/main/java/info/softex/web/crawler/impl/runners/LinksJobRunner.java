package info.softex.web.crawler.impl.runners;

import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.BasicJobData;
import info.softex.web.crawler.io.StringOutputStream;
import info.softex.web.crawler.utils.DownloadUtils;
import info.softex.web.crawler.utils.UrlUtils;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The runner that recursively iterates over the resolved links
 * 
 * @since version 2.2,		04/16/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class LinksJobRunner implements JobRunner {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	protected final BasicJobData jobData = new BasicJobData();
	
	protected final String initialUrl;
	protected final int recursionDepth;
	protected int totalLinksFollowed = 0;
	
	public LinksJobRunner(String inUrl, int inRecursionDepth) {
		this.initialUrl = inUrl;
		this.recursionDepth = inRecursionDepth;
	}

	@Override
	public void run(JobRunnable job) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("Started processing links with recursion depth of {}", recursionDepth);
		
		// Start recursive downloading
		downloadAll(job, initialUrl, 0);
		
		// Finish job
		job.finish();
		
		long time = (System.currentTimeMillis() - startTime) / 1000;
		log.info("Processing complete. Time: {} sec. Total Links Followed: {}", time, totalLinksFollowed);
	}
	
	@SuppressWarnings("unchecked")
	private void downloadAll(JobRunnable job, String initialUrl, int curRecLevel) throws Exception {
		
		if (initialUrl == null) {
			return;
		}
		
		totalLinksFollowed++;
		
		// Decode the URL for logging if possible
		String logUrl = UrlUtils.decodeURL(initialUrl);
		if (logUrl == null) {
			logUrl = initialUrl;
		}
		log.info("Resolving: {}", logUrl);
		
		StringOutputStream os = new StringOutputStream();
		DownloadUtils.download(os, initialUrl);
		Set<String> links = DownloadUtils.getLinks(os.toStringUTF8());
	
		// Process the links
		jobData.setDataObject(links);
		job.processItem(jobData);
		
		// Get back the processed links to follow
		links = (Set<String>)jobData.getDataObject();
		
		// Go to the next level of recursion if it's possible
		if (curRecLevel < recursionDepth) {
			for (String link : links) {
				downloadAll(job, link, curRecLevel + 1);
			}
		} else {
			log.info("Recursion level is {}. Returning.", curRecLevel);
		}

	}

}
