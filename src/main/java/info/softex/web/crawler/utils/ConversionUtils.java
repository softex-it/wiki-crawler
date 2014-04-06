package info.softex.web.crawler.utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @since version 1.0,	02/10/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class ConversionUtils {
	
	public final static String HTML_EXT = ".html"; 
	
	private final static String ikrLower = "и\u0306";
	private final static String ikrUpper = "И\u0306";
	
	public static String removeSpecialSymbols(String s) throws IOException {
		s = s.replace("%3F", ""); // ?
		s = s.replace("%3D", ""); // =
		s = s.replace("'", ""); // '
		return s;
	}
	
	public static String replaceUnderscoresWithSpaces(String s) {
		return s.replaceAll("_", " ");
	}
	
	public static String extractJumpId(String url) {
		int jumpPos = url.indexOf("#");
		if (jumpPos > 0) {
			return url.substring(jumpPos);
		}
		return null;
	}
	
	public static String replaceIncorrectSymbols(String s) {
		// Replace 2-symb й with 1-symb: "и" + "\u00ea"
		if (s.contains(ikrLower)) {
			s = s.replace(ikrLower, "й");
		}
		if (s.contains(ikrUpper)) {
			s = s.replace(ikrUpper, "Й");
		}
		return s;
	}
	
	public static String stripHtmlExtension(String str) {
		str = str.trim();
		if (str != null) {
			if (str.toLowerCase().endsWith(".html")) {
				return str.substring(0, str.length() - 5);
			}
 		}
		return str;
	}
	
	public static String stripQuotes(String str) {
		str = str.trim();
		if (str != null) {
			if (str.startsWith("'") && str.endsWith("'")) {
				return str.substring(1, str.length() - 1); 
			}
 		}
		return str;
	}
	
	public static String removeHtmlExtension(String fileName) {
		
		if (fileName == null) {
			return null;
		}
		
		if (fileName.trim().toLowerCase().endsWith(".html")) {
			fileName = fileName.substring(0, fileName.length() - 5);
		} else if (fileName.trim().toLowerCase().endsWith(".htm")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		
		return fileName;
		
	}
	
	public static String removeLineBreaks(String text) {
		return text.replaceAll("(\\r|\\n)", " ").trim();
	}
	
	public static String mapWordToFileName(String word) {
		String fileName = word.replaceAll("/", "&frasl");
		return fileName.trim() + HTML_EXT;
	}
	
	public static String mapFileNameToWord(String fileName) {
		String word = fileName.replaceAll("&frasl", "/");
		return word.substring(0, word.length() - HTML_EXT.length()).trim();
	}
	
	public static String compressHtml(String html) throws IOException {
		//return html.replaceAll("\\s+", " ");
		StringBuffer out = new StringBuffer();
		String lcHtml = html.toLowerCase();
		
		// Compose the protected ranges
		ArrayList<Integer> preRanges = new ArrayList<Integer>();
		int startIndex = lcHtml.indexOf("<pre>");
		while (startIndex >= 0) {
			preRanges.add(startIndex);
			
			int endIndex = lcHtml.indexOf("</pre>", startIndex + 1);
			if (endIndex >= 0) {
				preRanges.add(endIndex);
			} else {
				throw new IOException("Couldn't find the end tag for <pre>");
			}
			
		    startIndex = lcHtml.indexOf("<pre>", startIndex + 1);
		    if (startIndex >= 0 && startIndex < endIndex) {
		    	throw new IOException("Found twisted <pre> tags");
		    }
		    
		}
		
		// Process the input
		boolean isLastCharSpace = false;
		
		for (int i = 0; i < html.length(); i++) {
			
			char ch = html.charAt(i);
			
			// Check if the range is protected
			boolean isProtected = false;
			for (int j = 0; j < preRanges.size(); j+=2) {
				if (i >= preRanges.get(j) && i <= preRanges.get(j + 1)) {
					isProtected = true;
					break;
				}
			}
			
			if (isProtected) {
				out.append(ch);
				continue;
			}
			
			if (Character.isWhitespace(ch)) {
				if (!isLastCharSpace) {
					out.append(ch);
				}
				isLastCharSpace = true;
			} else {
				out.append(ch);
				isLastCharSpace = false;
			}
		}
		
		return out.toString().trim();
	}

}
