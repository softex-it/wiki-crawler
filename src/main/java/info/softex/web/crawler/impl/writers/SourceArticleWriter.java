package info.softex.web.crawler.impl.writers;

import info.softex.web.crawler.api.ArticleWriter;

import java.io.File;

/**
 * 
 * @since version 1.0,	04/05/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class SourceArticleWriter implements ArticleWriter {
	
	private final String contentFilePath;
	private final String mediaPath;
	
	public SourceArticleWriter(String inDirectory) {
		this.contentFilePath = inDirectory + File.separator + "articles.txt";
		this.mediaPath = inDirectory + File.separator + "media";
		new File(this.mediaPath).mkdirs();
	}

	@Override
	public void writeArticle(String inContent, String inTitle) throws Exception {
		
	}

	@Override
	public void writeImage(String inMedia) throws Exception {
		
	}

	@Override
	public void writeAbbreviation(String inContent, String inTitle) throws Exception {
		
	}

	@Override
	public void writeAudio(String inAudio) throws Exception {
		
	}


}
