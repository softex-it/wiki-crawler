package info.softex.web.crawler.cli;

/**
 * Command line interface statically configured for downloading of wiki.
 * 
 * @since version 2.2,		04/18/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class ListDownloaderStaticCLI extends ListDownloaderCLI {
	
	public String getInputFile() {
		return "/ext/wiki/articles_keys.txt";
	}
	
	public String getOutputDir() {
		return "/ext/wiki";
	}
	
	public String getHttpURL() {
		return "http://zh.m.wikipedia.org/wiki";
		//return "http://lurkmore.to";
	}
	
}
