package info.softex.web.crawler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 1.0,	02/21/2014
 * 
 * @modified version 2.2,	04/16/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class DownloadUtils {
	
	private final static String UTF8 = "UTF-8";
	
	private final static String BROWSER = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) " +
		"AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.102 Safari/537.36";

	private final static Logger log = LoggerFactory.getLogger(DownloadUtils.class);
	
	public static Set<String> getLinks(File file) {

		try {
			String html = FileUtils.file2String(file);
			return getLinks(html);
		} catch (IOException e) {
			log.error("Error", e);
			return null;
		}
		
	}
	
	public static Set<String> getLinks(String html) {

		HashSet<String> linkSet = new HashSet<String>();
		
		Document doc = Jsoup.parse(html, UTF8);
		Elements links = doc.select("a");
		
		for (Element link : links) {
			String ref = link.attr("href");
			if (ref != null && !ref.trim().isEmpty()) {
				linkSet.add(ref.trim());
			}
		}
		
		return linkSet;
		
	}
	
	public static Set<String> filterLinks(Set<String> inLinks, String inStartsWith) throws IOException {
		HashSet<String> filteredLinks = new HashSet<String>();
		for (String link : inLinks) {
			if (link != null && link.startsWith(inStartsWith)) {
				filteredLinks.add(link);
			}
		}
		return filteredLinks;
	}
	
	public static DownloadStatus download(File file, String uri) {
		
		try {
			if (FileUtils.fileExists(file)) {
				return DownloadStatus.EXISTS;
			}
			
			DownloadStatus status = download(new FileOutputStream(file), uri);
			
			// If file is empty, delete it and return error
			if (file.length() == 0) {
				file.delete();
				return DownloadStatus.ERROR;
			}
			return status;
			
		} catch (IOException e) {
			log.error("Error: {}", e.getMessage());
			return DownloadStatus.ERROR;
		}
		
	}
	
	public static DownloadStatus download(OutputStream os, String uri) {
		try {

			Response resultImageResponse = Jsoup.connect(uri).
				userAgent(BROWSER).ignoreContentType(true).
				//followRedirects(false).
				execute();
			
			//log.error("W " + file.getName() + " R " + resultImageResponse.statusCode());

			byte[] body = resultImageResponse.bodyAsBytes();
			
			if (body != null && body.length > 0) {
		        os.write(body);
		        os.close();
		        return DownloadStatus.DOWNLOADED;
			} else {
				return DownloadStatus.ERROR;
			}
			
		} catch (HttpStatusException e) {
			log.error("Error: {}", e.getMessage() + "; " + uri);
			return DownloadStatus.NOT_FOUND;
		} catch (Exception e) {
			log.error("Error: {}", e.getMessage() + "; " + uri);
			return DownloadStatus.ERROR;
		}
		
	}
	
	public enum DownloadStatus {
		DOWNLOADED,
		EXISTS,
		NOT_FOUND,
		ERROR
	}
	
}
