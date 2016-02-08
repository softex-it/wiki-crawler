package info.softex.web.crawler.wiki;

import static info.softex.web.crawler.utils.HtmlConstants.ATT_SRC;
import info.softex.web.crawler.api.JobRunner;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.api.WriterPool;
import info.softex.web.crawler.cli.PageProcessorCLI;
import info.softex.web.crawler.impl.jobs.AbstractHtmlJob;
import info.softex.web.crawler.impl.pools.BasicLogPool;
import info.softex.web.crawler.impl.pools.BasicWriterPool;
import info.softex.web.crawler.impl.runners.FilesJobRunner;
import info.softex.web.crawler.tools.attributes.MediaDownloadRate;
import info.softex.web.crawler.utils.ConversionUtils;
import info.softex.web.crawler.utils.DownloadUtils;
import info.softex.web.crawler.utils.FileUtils;
import info.softex.web.crawler.utils.JsoupUtils;
import info.softex.web.crawler.utils.StringUtils;
import info.softex.web.crawler.utils.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 * 
 * @since version 1.0,		02/05/2014
 * 
 * @modified version 2.2,	04/14/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class LurkProcessor {

	public static void main(String[] args) throws Exception {
		
		PageProcessorCLI cli = new PageProcessorCLI(args);
		String inputDir = cli.getInputDir();
		String outputDir = cli.getOutputDir();
		String httpUrl = cli.getHttpURL();
		MediaDownloadRate mediaRate = cli.getMediaDownloadRate();
		
		if (mediaRate == MediaDownloadRate.LIGHT) {
			throw new IllegalArgumentException("Light media download rate is not supported");
		}
				
		LogPool logPool = BasicLogPool.create().
			errorFile(outputDir + File.separator + "links-error.txt").
			debugFile(outputDir + File.separator + "links-debug.txt");

		WriterPool writerPool = BasicWriterPool.create().
			outputContentDir(outputDir + File.separator + "articles_html").
			outputMediaDir(outputDir + File.separator + "media");
		
		LurkHtmlFileJob wikiJob = new LurkHtmlFileJob(logPool, writerPool, httpUrl, mediaRate);	

		JobRunner runner = new FilesJobRunner(inputDir, null);
		runner.run(wikiJob);
		
	}
	
	private static class LurkHtmlFileJob extends AbstractHtmlJob {
		
		protected final static Pattern IMG_PATH_PATTERN = Pattern.compile(".*?:url\\((.+?)\\);[.*]*");
		
		protected final Set<String> absentLinks = new HashSet<String>();

		protected final MediaDownloadRate mediaDownloadRate;
		protected final String httpUrl;
		
		public LurkHtmlFileJob(LogPool inLogPool, WriterPool inWriterPool, 
				String inHttpUrl, MediaDownloadRate inMediaDownloadRate) throws IOException {
			super(inLogPool, inWriterPool);
			this.mediaDownloadRate = inMediaDownloadRate;
			this.httpUrl = inHttpUrl;
		}
		
		@Override
		public Element processDocument(Document inDocument, String inTitle) throws Exception {
			Element content = inDocument.select("div#content").first();
			if (content == null) {
				return null;
			}
			Element root = new Element(Tag.valueOf("div"), "");
			//root.append("<h2>" + inTitle + "</h2>");
			root.insertChildren(-1, content.childNodes());
			return root;
		}
		
		@Override
		protected boolean processContent(Element content, String inTitle) throws Exception {
			
			// Remove top anchor
			content.select("a#top").remove();
			
			content.select("script").remove();
			
			// Replace the title h1 with h2
			content.select("h1.firstHeading").tagName("h2");
			
			// Remove edit link before each header
			content.select("span.editsection").remove();
			
			// Remove class printfooter (bottom)
			content.select("div.printfooter").remove();
			
			// Remove class catlinks (bottom)
			content.select("div.catlinks").remove();
			
			// Remove site notice
			content.select("div#siteNotice").remove();

			// Remove repetitive site sub
			content.select("h3#siteSub").remove();

			// Remove jump to nav
			content.select("div#jump-to-nav").remove();
			
			// Fix styles of images (center thumbinners)
			Elements thumbs = content.select("div.thumbinner");
			for (Element thumb : thumbs) {
				String oldStyle = thumb.attr("style");
				if (!oldStyle.endsWith(";")) {
					oldStyle += ";";
				}
				thumb.attr("style", oldStyle + "margin-right:auto;margin-left:auto;margin-top:15px");
			}
			
			replaceVideoWithImages(content);

			// Remove all comments
			JsoupUtils.removeComments(content);
			
			return true;
			
		}
		
		@Override
		protected String processLink(String href, String inTitle) throws Exception {
			
			// Don't process external links
			if (href.startsWith("//") || href.startsWith("http://") || href.startsWith("https://")) {
				if (href.startsWith("//")) {
					href = "http:" + href;
				}
				linksExternal++;
				return href;
			}
			
			String newHref = UrlUtils.decodeURL(href);
			
			// Don't process blank or invalid links
			if (StringUtils.isBlank(newHref)) {
				linksRemoved++;
				return null;
			}
			
			if (newHref.contains(":")) { // Fix special links to refer to http
				newHref = httpUrl + newHref;
				linksExternal++;
				return newHref;
			}

			if (newHref.startsWith("/")) {
				newHref = newHref.substring(1);
			}
			
			newHref = ConversionUtils.replaceUnderscoresWithSpaces(newHref);
			
			String jumpId = UrlUtils.extractJumpId(href);
			if (jumpId != null) {
				href = href.substring(0, href.length() - jumpId.length()).trim();
			}
			
			String realFileName = getFileByLowerCaseName(FileUtils.title2FileName(newHref).toLowerCase());

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

			return newHref;
			
		}
		
		@Override
		protected void processImage(Element image, String inFileName) throws Exception {

			// Remove image if the media download rate is NONE
			if (mediaDownloadRate == MediaDownloadRate.NONE) {
				image.remove();
				imagesRemoved++;
				return;
			}
			
			JsoupUtils.filterImageAttributes(image);
			
			String src = image.attr(ATT_SRC).trim();
			String mediaDir = writerPool.getMediaDir();
		
			if (!src.contains("/")) {
				try {
					
					File srcFile = new File(mediaDir + "/" + src);
					String decodedName = UrlUtils.decodeURL(src);
					if (!FileUtils.fileExists(srcFile)) {
						//System.out.println("Direct file not found: " + srcFile);
						srcFile = new File(mediaDir + "/" + decodedName);
					}
					
					// Try to check lower case, it works for magnify-clip.png
					if (!FileUtils.fileExists(srcFile)) {
						//System.out.println("Decoded file not found: " + srcFile);
						decodedName = decodedName.toLowerCase();
						srcFile = new File(mediaDir + "/" + decodedName);
					}
					
					String fixedName = ConversionUtils.removeSpecialSymbols(decodedName);

					if (FileUtils.fileExists(srcFile)) {
						FileUtils.copyFile(srcFile, new File(mediaDir + "/" + fixedName), false);
						// Rewrite the image URL
				        image.attr("src", fixedName);
					} else {
						log.warn("Decoded & Lowercase file is not found: " + fixedName);
						image.remove();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (src.startsWith("//") || src.toLowerCase().startsWith("http") || src.toLowerCase().startsWith("https")) {
				try {
					
					if (src.startsWith("//")) {
						src = "http:" + src;
					}
					
					String lpFileName = src.substring(src.lastIndexOf('/') + 1, src.length());
					lpFileName = ConversionUtils.removeSpecialSymbols(lpFileName);
					String resolvedName = UrlUtils.decodeURL(lpFileName);
					if (resolvedName == null) {
						resolvedName = lpFileName;
					}

					//System.out.println(fileNameFixed + " " + imPath);
					
					String[] segments = src.split("/");
					if (resolvedName.contains("hqdefault")) {
						if (segments.length > 2) {
							resolvedName = segments[segments.length - 2] + "-" + resolvedName;
						}
					}
					
					// Check if file exists with different case
					File extraImage = new File(mediaDir + "/" + resolvedName);
					if (!FileUtils.fileExists(extraImage) && extraImage.exists()) {
						if (segments.length > 2) {
							resolvedName = segments[segments.length - 3] + "-" + 
								segments[segments.length - 2] + "-" + resolvedName;
						} else if (segments.length > 1) {
							resolvedName = segments[segments.length - 2] + "-" + resolvedName;
						}
						extraImage = new File(mediaDir + "/" + resolvedName);
					}

					DownloadUtils.DownloadStatus status = DownloadUtils.download(extraImage, src);

					if (status == DownloadUtils.DownloadStatus.EXISTS || status == DownloadUtils.DownloadStatus.DOWNLOADED) {
						image.attr(ATT_SRC, resolvedName);
						imagesLinked++;
						return;	
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			image.remove();
			imagesRemoved++;
			
		}
	
		protected static void replaceVideoWithImages(Element content) {
			
			// Replace all videos with images
			Elements videos = content.select("div.embed-placeholder");
			for (Element video : videos) {
				String styles = video.attr("style");

				Matcher m = IMG_PATH_PATTERN.matcher(styles);
				// System.out.println(styles + " " + m.find());
				if (m.find()) {
					Element img = new Element(Tag.valueOf("img"), "");
					img.attr("width", video.attr("width"));
					img.attr("height", video.attr("height"));
					img.attr("src", m.group(1));
					video.after(img);
				}
				
				video.remove();
			}
			
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

	}
	
}
