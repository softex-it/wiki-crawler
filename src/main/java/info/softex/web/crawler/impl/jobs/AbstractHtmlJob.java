package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.api.WriterPool;
import info.softex.web.crawler.utils.FileUtils;
import info.softex.web.crawler.utils.JsoupUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Abstract processing job for HTML content. It simplifies creation of custom processors.
 * 
 * @since version 1.0,		03/22/2014
 * 
 * @modified version 2.0,	01/21/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public abstract class AbstractHtmlJob extends AbstractJob {
	
	protected final Set<String> linksFound = new HashSet<>();
	protected final Set<String> linksMissing = new HashSet<>();
	
	protected int linksLinked = 0;
	protected int linksRemoved = 0;
	protected int linksTotal = 0;
	protected int linksJump = 0;
	protected int linksExternal = 0;
	
	protected int soundsLinked = 0;
	protected int soundsRemoved = 0;
	protected int soundsTotal = 0;
	
	protected int imagesLinked = 0;
	protected int imagesRemoved = 0;
	protected int imagesTotal = 0;
	
	public AbstractHtmlJob(LogPool inLogPool, WriterPool inWriterPool) throws IOException {
		super(inLogPool, inWriterPool);
	}

	@Override
	public boolean processItem(JobData jobData) throws Exception {
		
		Document doc = Jsoup.parse(jobData.getContent(), UTF8);
		String title = jobData.getTitle();
		
		Element content = processDocument(doc, title);
		
		if (content == null) {
			log.info("Couldn't find content for {}", title);
			return false;
		}
		
		processContent(content, title);
		
		processLinks(content, title);
		
		processImages(content, title);
		
		processSounds(content, title);
		
		String outHtml = processOutput(content, title);
		
		saveOutput(outHtml, title);
		//FileUtils.string2File(outHtmlPath + File.separator + jobData.getFileName(), outHtml);
		
		itemsProcessed++;
		
		return true;
		
	}
	
	protected void saveOutput(String output, String inTitle) throws Exception {
		log.warn("Override this method to save output: {}", output);
	}
	
	protected Element processDocument(Document inDocument, String inTitle) throws Exception {
		return inDocument.body();
	}
	
	protected boolean processContent(Element content, String inTitle) throws Exception {
		return true;
	}
	
	protected String processOutput(Element content, String inTitle) throws Exception {
		return content.html();
	}
	
	protected void processLinks(Element content, String inTitle) throws Exception {
		
		Elements links = content.select("a");
		
		linksTotal += links.size();
		
		for (Element link : links) {
			
			String href = link.attr("href").trim();
			
			// Remove undefined jump links
			if (href.equals("#")) {
				JsoupUtils.removeContainerTag(link);
				linksRemoved++;
			} else if (href.startsWith("#")) { // Skip jump links
				linksLinked++;
				linksJump++;
			} else if (href.toLowerCase().startsWith("http")) { // Skip http(s) links
				linksLinked++;
				linksExternal++;
			} else {
			
				String alteredLink = processLink(href, inTitle);
				if (alteredLink != null) {
					link.attr("href", alteredLink);
					linksLinked++;
				} else {
					JsoupUtils.removeContainerTag(link);
					linksRemoved++;
				}
			
			}
		
		}

	}
	
	protected void processImages(Element content, String fileName) throws Exception {
		Elements images = content.select("img");
		imagesTotal += images.size();
		for (Element image : images) {
			processImage(image, fileName);
		}
	}
	
	protected void processSounds(Element content, String fileName) throws Exception {
		Elements sounds = content.select("audio");
		soundsTotal += sounds.size();
		for (Element sound : sounds) {
			processSound(sound, fileName);
		}
	}
	
	protected String processLink(String href, String inTitle) throws Exception {
		return href;
	}
	
	protected void processImage(Element image, String inFileName) throws Exception {
		imagesLinked++;
	}
	
	protected void processSound(Element sound, String inFileName) throws Exception {
		soundsLinked++;
	}
	
	protected String title2FileName(String title) {
		return FileUtils.title2FileName(title);
	}
	
	@Override
	public void finish() throws IOException {
		
		super.finish();
		
		log.info(
				"Links | Total/Jump/External: {}/{}/{}; Linked: {}; Removed: {}", 
				linksTotal, linksJump, linksExternal, linksLinked, linksRemoved
			);
		log.info(
			"Images | Total: {}; Linked: {}; Removed: {}", 
			imagesTotal, imagesLinked, imagesRemoved
		);
		log.info(
			"Sounds | Total: {}; Linked: {}; Removed: {}", 
			soundsTotal, soundsLinked, soundsRemoved
		);
		
	}
	
}
