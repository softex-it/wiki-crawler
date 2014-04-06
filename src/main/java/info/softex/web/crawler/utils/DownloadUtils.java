package info.softex.web.crawler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * @author Dmitry Viktorov
 *
 */
public class DownloadUtils {
	
	private final static String BROWSER = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) " +
		"AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.102 Safari/537.36";

	private final static Logger log = LoggerFactory.getLogger(DownloadUtils.class);
	
	public static Set<String> getLinks(File file) {

		Document doc = null;
		try {
			doc = Jsoup.parse(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		HashSet<String> linkSet = new HashSet<String>();
		
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
		} catch (IOException e) {
			log.error("Error: {}", e.getMessage());
			return DownloadStatus.ERROR;
		}
		
		try {
			Response resultImageResponse = Jsoup.connect(uri).userAgent(BROWSER).ignoreContentType(true).execute();
			FileOutputStream out = (new FileOutputStream(file));
	        out.write(resultImageResponse.bodyAsBytes());
	        out.close();
	        return DownloadStatus.DOWNLOADED;
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
