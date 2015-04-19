package info.softex.web.crawler.wiki;

import info.softex.web.crawler.cli.LinksResolverCLI;
import info.softex.web.crawler.impl.jobs.AbstractLinksProcessorJob;
import info.softex.web.crawler.impl.runners.LinksJobRunner;
import info.softex.web.crawler.utils.ConversionUtils;
import info.softex.web.crawler.utils.StringUtils;
import info.softex.web.crawler.utils.UrlUtils;

import java.io.IOException;

/**
 * 
 * @since version 2.2,		04/16/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class LurkLinksResolver {
	
	public static void main(String[] args) throws Exception {
		
		LinksResolverCLI cli = new LinksResolverCLI(args);
		String outputFile = cli.getOutputFile();
		String httpDomain = cli.getHttpURL();
		String initialTerm = cli.getInitialKey();
		int recDepth = cli.getRecursionDepth();
		
		String initialUrl = httpDomain + "/" + UrlUtils.encodeURL(initialTerm);
		
		LurkLinksProcessor processor = new LurkLinksProcessor(outputFile, httpDomain);
		
		LinksJobRunner runner = new LinksJobRunner(initialUrl, recDepth);
		runner.run(processor);
		
	}
	
	protected static class LurkLinksProcessor extends AbstractLinksProcessorJob {

		protected final String httpDomain; 
		
		public LurkLinksProcessor(String inFilePath, String inHttpDomain) throws IOException {
			super(inFilePath);
			this.httpDomain = inHttpDomain;
		}

		@Override
		public String getTrackedLink(String link) {
	
			// Prepare term
			String term = UrlUtils.decodeURL(link);
			
			// Don't process the link if it can't be decoded
			if (term == null) {
				return null;
			}
			
			term = ConversionUtils.replaceUnderscoresWithSpaces(term);
			
			if (term.startsWith("/")) {
				term = term.substring(1);
			}
			
			// Filter term
			if (StringUtils.isNotBlank(term) && 
					//!term.startsWith("#") && // Ignore jump links
					!term.contains("#") && // TODO: it should be starts with and cutting the jump links from normal urls
					!term.startsWith("index.php") && !term.contains("?") && // Ignore requests to index
					!term.contains(":") && !term.contains("/")) {
				return term;
			}
			
			return null;

		}
		
		@Override
		public String getFollowedLink(String link) {
			String result = null;
			String processedLink = getTrackedLink(link);
			if (processedLink != null) {
				result = httpDomain + link;
			}
			return result;
		}
		
	}

}
