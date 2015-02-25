package info.softex.web.crawler.utils;


/**
 * 
 * @since version 2.1,	01/24/2015
 * 
 * @author Dmitry Viktorov
 * 
 */
public class LanguageUtils {
	
	public static boolean containsUnicodeScript(String s, Character.UnicodeScript script) {
	    for (int i = 0; i < s.length(); ) {
	        int codepoint = s.codePointAt(i);
	        i += Character.charCount(codepoint);
	        if (Character.UnicodeScript.of(codepoint) == script) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Checks if string contains Cyrillic symbols.
	 * 
	 * Cyrillic:            U+0400–U+04FF ( 1024 -  1279)
	 * Cyrillic Supplement: U+0500–U+052F ( 1280 -  1327)
	 * Cyrillic Extended-A: U+2DE0–U+2DFF (11744 - 11775)
	 * Cyrillic Extended-B: U+A640–U+A69F (42560 - 42655)
	 */
	public static boolean containsCyrillic(String s) {
		return containsUnicodeScript(s, Character.UnicodeScript.CYRILLIC);
	}
	
	/**
	 * Checks if string contains Latin symbols.
	 * 
	 * Per Unicode 7.0 Latin contains 1338 symbols.
	 */
	public static boolean containsLatin(String s) {
		return containsUnicodeScript(s, Character.UnicodeScript.HAN);
	}
	
	/**
	 * Checks if string contains Han symbols (Chinese, Japanese, etc).
	 */
	public static boolean containsHan(String s) {
		return containsUnicodeScript(s, Character.UnicodeScript.HAN);
	}

}
