package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.impl.pools.BasicLogPool;
import info.softex.web.crawler.impl.pools.BasicWriterPool;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Processes links extracted from html pages. By default it will remove
 * jump links, filter the ones with :, replace underscores with spaces, 
 * and write them to file. 
 * 
 * The job tracks each item to be written to the file only once.
 * 
 * @since version 2.2,	04/16/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public abstract class AbstractLinksProcessorJob extends AbstractJob {
	
	protected final Set<String> trackedLinks = new LinkedHashSet<String>(); 

	public AbstractLinksProcessorJob(String filePath) throws IOException {
		super(BasicLogPool.create(), BasicWriterPool.create().outputFile1(filePath));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processItem(JobData jobData) throws Exception {
		
		Set<String> resultLinks = new LinkedHashSet<>();
		
		Set<String> unprocessedLinks = (Set<String>)jobData.getDataObject();
		for (String link : unprocessedLinks) {
			String trackedLink = getTrackedLink(link);
			if (!trackedLinks.contains(trackedLink)) {
				trackedLinks.add(trackedLink);
				resultLinks.add(getFollowedLink(link));
				writeOutput1(trackedLink);
			}
		}
		
		// Copy processed links back
		unprocessedLinks.clear();
		unprocessedLinks.addAll(resultLinks);
		
		return true;
	}
	
	abstract public String getTrackedLink(String link);

	abstract public String getFollowedLink(String link);
	
}
