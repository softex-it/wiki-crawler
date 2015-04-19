package info.softex.web.crawler.tools;

import info.softex.web.crawler.api.DataInjector;
import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.impl.jobs.FilesSelectorJob;
import info.softex.web.crawler.impl.runners.FilesJobRunner;
import info.softex.web.crawler.utils.FileUtils;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 2.1,	03/09/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class FileSelector {
	
	public static void main(String[] args) throws Exception {
		
		String root = "/Volumes/Media/ln/ES_RU_ES_2013/EsRu/EsRu_Universal_2011-13_Combined";
		
		JobRunner runner = new FilesJobRunner(root + "/media", null);
		
		JobRunnable job = new FilesSelectorJob(root + "/media", root + "/media_out");
		job.injectData(new FileMatchesInjector(root + "/articles.dsl"));
		
		runner.run(job);
	}
	
	public static class FileMatchesInjector implements DataInjector {
	
		protected final Logger log = LoggerFactory.getLogger(getClass());
		
		protected final String filePath;
		
		public FileMatchesInjector(String inFilePath) {
			this.filePath = inFilePath;
		}

		@Override
		public void inject(Map<DataKey, Object> dataMap) throws Exception {
			
			String input = FileUtils.file2String(new File(filePath));
			
			String mt = "\\[s\\](.*?)\\[/s\\]";
			Pattern p = Pattern.compile(mt);
			Matcher m = p.matcher(input);

			Set<String> matches = new LinkedHashSet<String>();
			
			int count = 0;
			while (m.find()) {
				matches.add(m.group(1).trim());
				count++;
			}
			
			dataMap.put(DataKey.MATCHING_SET, matches);
			
			log.info("Total / Unique Number of Matches: {} / {}", count, matches.size());
			
		}
		
	}

}
