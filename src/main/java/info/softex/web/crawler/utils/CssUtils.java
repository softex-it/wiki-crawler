package info.softex.web.crawler.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since version 1.0,	03/28/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class CssUtils {

	public static Map<String, String> string2StyleMap(String styleValue) {
		Map<String, String> stylesMap = new HashMap<String, String>();
		if (!styleValue.isEmpty()) {
			String[] stylesArr = styleValue.split(";");
			for (int i = 0; i < stylesArr.length; i++) {
				String curStyle = stylesArr[i].trim();
				if (!curStyle.isEmpty()) {
					int colPos = curStyle.indexOf(":");
					if (colPos >= 0) {
						String stKey = curStyle.substring(0, colPos).trim();
						String stValue = curStyle.substring(colPos + 1).trim();
						if (!stKey.isEmpty() && !stValue.isEmpty()) {
							stylesMap.put(stKey, stValue);
						}
					} else {
						stylesMap.put(curStyle, "");
					}
				}
			}
		}
		return stylesMap;
	}
	
	public static String styleMap2String(Map<String, String> styles) {
		String stylesString = "";
		for (Map.Entry<String, String> curStyle : styles.entrySet()) {
			String stKey = curStyle.getKey().trim();
			if (!stKey.isEmpty()) {
				String stValue = curStyle.getValue().trim();
				if (!stValue.isEmpty()) {
					stylesString += curStyle.getKey() + ": " + curStyle.getValue().trim() + "; ";
				} else {
					stylesString += curStyle.getKey() + "; ";
				}
			}
		}
		return stylesString.trim();
	}
	
}
