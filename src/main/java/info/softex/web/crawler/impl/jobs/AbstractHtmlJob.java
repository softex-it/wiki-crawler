package info.softex.web.crawler.impl.jobs;

import info.softex.web.crawler.api.JobData;
import info.softex.web.crawler.api.JobRunnable;
import info.softex.web.crawler.api.LogPool;
import info.softex.web.crawler.utils.FileUtils;
import info.softex.web.crawler.utils.JsoupUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 1.0,	03/22/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public abstract class AbstractHtmlJob implements JobRunnable {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected String outHtmlPath;
	protected String outMediaPath;
	
	
	protected BufferedWriter outWriter;
	
	protected final LogPool logPool;
	
	protected final HashMap<String, String> wordFileMap = new HashMap<String, String>();
	
	protected final Set<String> imagesRemovedSet = new HashSet<String>(); 
	
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
	
	public AbstractHtmlJob(LogPool inLogPool, String inOutHtmlPath, String inOutMediaPath) throws IOException {
		if (inOutHtmlPath == null || inOutHtmlPath.isEmpty()) {
			throw new IllegalArgumentException("Out HTML Path can't be null or empty");
		}
		this.logPool = inLogPool;
		
		this.outHtmlPath = inOutHtmlPath;
		this.outMediaPath = inOutMediaPath;

		new File(outHtmlPath).mkdirs();
		new File(outMediaPath).mkdirs();
	}
	
	public AbstractHtmlJob(LogPool inLogPool, File inOutFile) throws IOException {
		if (inOutFile == null) {
			throw new IllegalArgumentException("Out File can't be null");
		}
		this.logPool = inLogPool;
		this.outWriter = FileUtils.createWriter(inOutFile);
	}
	
	@Override
	public void injectData(Object... data) throws Exception {
		
		File[] htmlFiles = (File[]) data[0];

		log.info("Started creating word mapper");
		for (int i = 0; i < htmlFiles.length; i++) {
			wordFileMap.put(htmlFiles[i].getName().toLowerCase(), htmlFiles[i].getName());
		}
		log.info("Finished creating word mapper");
		
	}
	
	@Override
	public void finish() throws IOException {
		if (outWriter != null) {
			outWriter.flush();
			outWriter.close();
		}
		log.info("Job is finished");
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
	
	protected String getWordFileMappedName(String inFileName) throws Exception {
		return wordFileMap.get(inFileName.toLowerCase());
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
		
		saveOutput(outHtml);
		//FileUtils.string2File(outHtmlPath + File.separator + jobData.getFileName(), outHtml);
		
		return true;
		
	}
	
	protected Element processDocument(Document inDocument, String inTitle) throws Exception {
		return inDocument.body();
	}
	
	protected boolean processContent(Element content, String inTitle) throws Exception {
		return true;
	}
	
	protected String processOutput(Element content, String inTitle) throws Exception {
		return content.html(); // "<br>&nbsp;"
	}
	
	protected void saveOutput(String output) throws Exception {
		log.warn("Override this method to save output: {}", output);
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
		//JsoupUtils.filterImageAttributes(image);
		imagesLinked++;
	}
	
	protected void processSound(Element sound, String inFileName) throws Exception {
		soundsLinked++;
	}
	
	protected void writeOutput2Writer(String output) throws IOException {
		outWriter.write(output + "\r\n");
		outWriter.flush();
	}
	
	protected String filename2Title(String fileName) {
		return FileUtils.fileName2Title(fileName);
	}

}
