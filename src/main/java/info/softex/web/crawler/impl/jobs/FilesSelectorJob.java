package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.DataInjector;
import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.impl.pools.BasicLogPool;
import info.softex.web.crawler.impl.pools.BasicWriterPool;
import info.softex.web.crawler.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * The job filters the files from the input directory using the set of matches.
 * 
 * @since version 2.1,	03/09/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class FilesSelectorJob extends AbstractJob {
	
	protected final String inDir;
	protected final String outDir;
	
	private Set<String> unusedMatches;

	public FilesSelectorJob(String inInDir, String inOutDir) throws IOException {
		super(BasicLogPool.create(), BasicWriterPool.create());
		this.inDir = inInDir;
		this.outDir = inOutDir;
		new File(inOutDir).mkdirs();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JobRunnable injectData(DataInjector dataInjector) throws Exception {
		super.injectData(dataInjector);
		// Clone the matches to use them later to find matches that were not used
		unusedMatches = new HashSet<String>((Set<String>) injectedData.get(DataInjector.DataKey.MATCHING_SET));
		log.info("Cloned the matches to track the consumed matches, size: {}", unusedMatches.size());
		return this;
	}
	
	@Override
	public void finish() throws IOException {
		log.warn("Matches not found: {}", unusedMatches);
		super.finish();
	}

	@Override
	public boolean processItem(JobData jobData) throws IOException {
		
		String fileName = jobData.getTitle();
		
		// Replace " " with "\ " for dsl resources
		if (fileName.contains(" ")) {
			log.info("Found file with space: {}", fileName);
			fileName = fileName.replaceAll(" ", "\\\\ ");
		}
		
		if (containsMatch(fileName)) {
			
			unusedMatches.remove(fileName);
			
			// Replace "\ " with " " for dsl resources
			if (fileName.contains(" ")) {
				fileName = fileName.replaceAll("\\\\ ", " ");
				log.info("File space replaced back: {}", fileName);
			}

			File inFile = new File(inDir + File.separator + fileName);
			File outFile = new File(outDir + File.separator + fileName);
			
			if (inFile.exists()) {
				FileUtils.copyFile(inFile, outFile, false);
				inFile.delete();
			} else {
				log.warn("Source file not found: {}", inFile.getPath());
			}
			
			return true;
		}
		
		return false;
		
	}
	
}
