package info.softex.web.crawler.wiki;

import static info.softex.web.crawler.utils.HtmlConstants.ATT_SRC;
import static info.softex.web.crawler.utils.HtmlConstants.STL_FLOAT_KEY;
import static info.softex.web.crawler.utils.HtmlConstants.STL_FLOAT_VAL_LEFT;
import static info.softex.web.crawler.utils.HtmlConstants.STL_FLOAT_VAL_RIGHT;
import static info.softex.web.crawler.utils.HtmlConstants.STL_MARGIN_LEFT;
import static info.softex.web.crawler.utils.HtmlConstants.STL_MARGIN_RIGHT;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.api.WriterPool;
import info.softex.web.crawler.filters.ImageFileFilter;
import info.softex.web.crawler.impl.jobs.AbstractHtmlJob;
import info.softex.web.crawler.impl.pools.BasicLogPool;
import info.softex.web.crawler.impl.pools.BasicWriterPool;
import info.softex.web.crawler.impl.runners.FilesJobRunner;
import info.softex.web.crawler.utils.ConversionUtils;
import info.softex.web.crawler.utils.DownloadUtils;
import info.softex.web.crawler.utils.FileUtils;
import info.softex.web.crawler.utils.JsoupUtils;
import info.softex.web.crawler.utils.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 * 
 * @since version 1.0,	03/17/2014
 * 
 * @since modified 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class WikiHtmlProcessor {
	
	public static final String HOST = "http://ru.m.wikipedia.org";
	
	//private static final Logger log = LoggerFactory.getLogger(WikiHtmlProcessor.class);
	
	private final static int IMG_SQUARE_MAX = 60 * 60;
	
	private final static ImageFileFilter imageFilter = new ImageFileFilter();
	
	private final static String[] IMAGE_WHITE_LIST = {
		//"flag_", "seal_", "coat_of_arms_",
		"flag", "seal", "coat_of_arms", "coa",
		"order", "medal",
		"logo", "icon", "symbol", // Iconic images
		"soccer", "hockey", "tennis", "football", "baseball", "basketball", "volleyball", "golf",
		
		"skeletal", // Chemical diagrams
		"/math/"	// Math Expressions
		
		// "template"
	};
	
	// Replace symbols ...
	//	for (int i = 0; i < sortedList.size(); i++) {
	//		String word = sortedList.get(i);
	//		if (word.startsWith("\u2026")) {
	//			word = "..." + word.substring(1);
	//			sortedList.set(i, word);
	//		}
	//	}
	
	public static void main(String[] args) throws Exception {
		
		JobRunner runner = new FilesJobRunner(
			"/ext/wiki/downloaded",
			//"/ext/wiki/experiment",
			null
		);
		
		LogPool logPool = BasicLogPool.create().
			errorFile("/ext/wiki/linksError.txt").
			debugFile("/ext/wiki/linksDebug.txt");

		//logPool.imageDebugFile("/ext/wiki/imagesDebug.txt");
		
		WriterPool writerPool = BasicWriterPool.create().
			outputContentDir("/ext/wiki/articles_html").
			outputMediaDir("/ext/wiki/media");
		
		WikiHtmlFileJob wikiJob = new WikiHtmlFileJob(logPool, writerPool);	
		
		//WikiHtmlFileJob wikiJob = new WikiHtmlFileJob(logPool, new File("/ext/wiki/articles.txt"));
		
		runner.run(wikiJob);
		
	}
	
	private static class WikiHtmlFileJob extends AbstractHtmlJob {
		
		private String startPattern = "/wiki/";
		
		private Set<String> absentLinks = new HashSet<String>();
		
		public WikiHtmlFileJob(LogPool inLogPool, WriterPool inWriterPool) throws IOException {
			super(inLogPool, inWriterPool);
		}
		
		@Override
		public Element processDocument(Document inDocument, String inTitle) throws Exception {
//			Element preContent = inDocument.select("div.pre-content").first();
			Element content = inDocument.select("div.content").first();
//			if (preContent == null || content == null) {
//				return null;
//			}
			Element root = new Element(Tag.valueOf("div"), "");
			//root.insertChildren(-1, preContent.childNodes());
			root.append("<h2>" + inTitle + "</h2>");
			root.insertChildren(-1, content.childNodes());
			return root;
		}
		
		@Override
		public boolean processContent(Element content, String inTitle) throws Exception {
	 		
			// Cut page actions from pre-content
			//content.select("ul#page-actions").remove();
			
			// Cut edit links
			content.select("a.edit-page").remove();
			
			// Cut links for read in different languages
			content.select("div#page-secondary-actions").remove();
			
			// Remove whitespace style from metadata (i.e. notice about incomplete articles)
			content.select("table.metadata").select("span").removeAttr("style");
			
			// Restyle infobox
			Elements tablesIB = content.select("table.infobox");
			for (Element table : tablesIB) {
				Map<String, String> styles = JsoupUtils.getStyleMap(table);
				styles.put(STL_FLOAT_KEY, STL_FLOAT_VAL_LEFT);
				styles.put(STL_MARGIN_RIGHT, "30px");
				JsoupUtils.setStyleMap(table, styles);
			}
			
			// Restyle pages for float:right (it causes it to move left off screen at low dimension)
			Elements tables = content.select("table");
			for (Element table : tables) {
				Map<String, String> styles = JsoupUtils.getStyleMap(table);
				String floatValue = styles.get(STL_FLOAT_KEY);
				if (floatValue != null && floatValue.equalsIgnoreCase(STL_FLOAT_VAL_RIGHT)) {
					styles.put(STL_FLOAT_KEY, STL_FLOAT_VAL_LEFT);
					String marginRightVal = styles.get(STL_MARGIN_LEFT);
					if (marginRightVal != null) {
						styles.remove(STL_MARGIN_LEFT);
						styles.put(STL_MARGIN_RIGHT, marginRightVal);
					} else { 
						styles.put(STL_MARGIN_RIGHT, "1em");
					}
				}
				JsoupUtils.setStyleMap(table, styles);
			}
			
			// Remove all spans with display:none
			content.select("span[style*=display:none]").remove();
			
			// Remove span xml:lang containers
			JsoupUtils.removeContainerTags(content.select("[xml:lang]"));
			
			// Remove all scripts
			JsoupUtils.removeScriptTags(content);
			
			// Remove all styles
			JsoupUtils.removeStyleTags(content);
			
			// Cut images
			//JsoupUtils.removeImageTags(content);
			
			// Remove all comments
			JsoupUtils.removeComments(content);

			// Remove empty style attrs
			JsoupUtils.removeEmptyStyleAttrs(content);

			// Remove all class attrs
			JsoupUtils.removeClassAttrs(content);
			
			// Remove all id attrs
			JsoupUtils.removeIdAttrs(content);

			return true;
		}
		
		@Override
		protected String processLink(String href, String inTitle) throws Exception {
			
			String newHref = null;
			
			if (href.startsWith("//")) { // Fix incomplete http(s)
				newHref = "http:" + href;
				linksExternal++;
			} else if (href.startsWith(startPattern)) {
				href = href.substring(startPattern.length());
				if (!href.isEmpty()) {
					href = UrlUtils.decodeURL(href);
							
					if (href.contains(":")) { // Fix special links to refer to http
						href = HOST + startPattern + href;
						linksExternal++;
					} else {
						href = ConversionUtils.replaceUnderscoresWithSpaces(href);
						
						// Check if it's a jump link
						String jumpId = UrlUtils.extractJumpId(href);
						if (jumpId != null) {
							href = href.substring(0, href.length() - jumpId.length()).trim();
						}
						
						String realFileName = getFileByLowerCaseName(FileUtils.title2FileName(href).toLowerCase());

						if (realFileName != null) {
							newHref = FileUtils.fileName2Title(realFileName);
							if (jumpId != null) {
								newHref += jumpId;
							}
						} else {
							if (!absentLinks.contains(href)) {
								absentLinks.add(href);
								logPool.logError(href);
							}
							//log.warn("Couldn't find file for link {} at {}", href, inFileName); 
						}
					}
					
				}
			}
			
			return newHref;
		}
		
		@Override
		protected void processImage(Element image, String inFileName) throws Exception {

			JsoupUtils.filterImageAttributes(image);
			
			String src = image.attr(ATT_SRC).trim();
		
			if (src.startsWith("//")) {
				src = "http:" + src; 
			}
			
			String decodedSrc = UrlUtils.decodeURL(src).replaceAll("'", "");
			String lowerSrc = decodedSrc.toLowerCase();
			
			// Include symbolic images
			if (imageFilter.accept(src)) {

				//if (true) {

				int imgSquare = JsoupUtils.getElementSquare(image);
				boolean isAllowedSquare = imgSquare > 0 && imgSquare <= IMG_SQUARE_MAX;
				if (isAllowedSquare || isImageWhiteListed(lowerSrc)) {
					
					String imgFileName = UrlUtils.getLastSegment(decodedSrc);
					if (imgFileName != null) {

						imgFileName = imgFileName.toLowerCase();
						
						File imgFile = new File(writerPool.getMediaDir() + File.separator + imgFileName);
						DownloadUtils.DownloadStatus status = DownloadUtils.download(imgFile, src);
						if (status == DownloadUtils.DownloadStatus.EXISTS || status == DownloadUtils.DownloadStatus.DOWNLOADED) {
							image.attr(ATT_SRC, imgFileName);
							imagesLinked++;
							return;
						}
					}
					
				}
			}
			image.remove();
			imagesRemoved++;
//			if (!imagesRemovedSet.contains(src)) {
//				imagesRemovedSet.add(src);
//				logPool.logImageDebug(src);
//			}
			
		}
		
		@Override
		protected void processSound(Element sound, String fileName) throws Exception {
			sound.remove();
			soundsRemoved++;
		}
		
		@Override
		protected String processOutput(Element content, String inTitle) throws Exception {
			return content.html() + "<br>&nbsp;";
		}
		
		@Override
		protected void saveOutput(String output, String inTitle) throws Exception {
			String fileName = FileUtils.title2FileName(inTitle);
			writerPool.writeContentFile(fileName, output);
		}
		
		protected static boolean isImageWhiteListed(String name) {
			for (int i = 0; i < IMAGE_WHITE_LIST.length; i++) {
				if (name.contains(IMAGE_WHITE_LIST[i])) {
					return true;
				}
			}
			return false;
		}
		
	}

}


