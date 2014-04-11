package info.softex.web.crawler.api;

/**
 * 
 * @since version 1.0,	04/05/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public interface ArticleWriter {
	
	public void writeArticle(String inContent, String inTitle) throws Exception;
	
	public void writeAbbreviation(String inContent, String inTitle) throws Exception;
	
	public void writeImage(String inMedia) throws Exception;

	public void writeAudio(String inAudio) throws Exception;
	
}
