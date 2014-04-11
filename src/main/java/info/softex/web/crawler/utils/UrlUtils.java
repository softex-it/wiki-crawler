package info.softex.web.crawler.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * @since version 1.0,	03/23/2014
 * 
 * @author Dmitry Viktorov
 * 
 */
public class UrlUtils {
	
	public static final String UTF8 = "UTF-8";
	
	public static String getLastSegment(String url) {
		int segmentPos = url.lastIndexOf('/');
		if (segmentPos > 0) {
			return url.substring(segmentPos + 1);
		}
		return null;
	}
	
	public static String decodeURL(String url) {
		try {
			return URLDecoder.decode(url, UTF8);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static String encodeURL(String url) {
		try {
			return URLEncoder.encode(url, UTF8);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static String extractJumpId(String url) {
		int jumpPos = url.indexOf("#");
		if (jumpPos > 0) {
			return url.substring(jumpPos);
		}
		return null;
	}

}
