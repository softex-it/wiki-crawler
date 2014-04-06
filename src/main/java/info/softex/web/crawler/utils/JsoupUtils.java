package info.softex.web.crawler.utils;

import static info.softex.web.crawler.utils.HtmlConstants.ATT_HEIGHT;
import static info.softex.web.crawler.utils.HtmlConstants.ATT_SRC;
import static info.softex.web.crawler.utils.HtmlConstants.ATT_STYLE;
import static info.softex.web.crawler.utils.HtmlConstants.ATT_WIDTH;

import java.util.Map;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * 
 * @since version 1.0,	02/23/2014
 * 
 * @author Dmitry Viktorov
 * 
 */
public class JsoupUtils {
	
	public static void removeContainerTag(Element container) {
		container.after(container.html());
		container.remove();
	}
	
	public static void removeContainerTags(Elements containers) {
		for (Element element : containers) {
			removeContainerTag(element);
		}
	}
	
	public static void removeBody(Element container) {

		for (Node n : container.childNodes()) {
			n.remove();
		}

	}
	
	public static void removeComments(Node node) {

		for (int i = 0; i < node.childNodes().size();) {
			Node child = node.childNode(i);
			if (child.nodeName().equals("#comment"))
				child.remove();
			else {
				removeComments(child);
				i++;
			}
		}
	}
	
	public static void removeScriptTags(Element el) {
		el.select("script").remove();
	}

	public static void removeStyleTags(Element el) {
		el.select("style").remove();
	}

	public static void removeImageTags(Element el) {
		el.select("img").remove();
	}
	
	public static void removeEmptyStyleAttrs(Element el) {
		el.select("[style~=^$]").removeAttr("style");
	}
	
	public static void removeClassAttrs(Element el) {
		el.select("[class]").removeAttr("class");;
	}
	
	public static void removeIdAttrs(Element el) {
		el.select("[id]").removeAttr("id");;
	}
	
	public static void removeJSEvents(Element element) {

		Elements all = element.select("*");

		for (Element el : all) {
			for (Attribute attr : el.attributes()) {
				String attrKey = attr.getKey();
				if (attrKey.startsWith("on")) {
					el.removeAttr(attrKey);
				}
			}
		}

	}
	
	public static void filterImageAttributes(Element image) {

		Attributes atts = image.attributes();
		for (Attribute att : atts) {
			String key = att.getKey();
			boolean isAllowed = ATT_SRC.equals(key) || ATT_WIDTH.equals(key) || 
				ATT_HEIGHT.equals(key) || ATT_STYLE.equals(key);
			if (!isAllowed) {
				atts.remove(key);
			}
		}
		
//		Element newImg = new Element(Tag.valueOf("img"), "");
//		newImg.attr("src", alteredSrc);
//		
//		if (image.attr("width") != null && !image.attr("width").trim().isEmpty()) {
//			newImg.attr("width", image.attr("width"));
//		}
//		
//		if (image.attr("height") != null && !image.attr("height").trim().isEmpty()) {
//			newImg.attr("height", image.attr("height"));
//		}
//		
//		if (image.attr("style") != null && !image.attr("style").trim().isEmpty()) {
//			newImg.attr("style", image.attr("style"));
//		}
//		
//		image.after(newImg);
//		image.remove();

	}
	
	public static Map<String, String> getStyleMap(Element el) {
		String styleAtt = el.attr(ATT_STYLE).trim();
		return CssUtils.string2StyleMap(styleAtt);
	}
	
	public static void setStyleMap(Element el, Map<String, String> styles) {
		String styleValue = CssUtils.styleMap2String(styles);
		if (!styleValue.isEmpty()) {
			el.attr(ATT_STYLE, styleValue);
		} else {
			el.removeAttr(ATT_STYLE);
		}
	}
	
	public static int getElementSquare(Element el) {
		String width = el.attr(HtmlConstants.ATT_WIDTH);
		String height = el.attr(HtmlConstants.ATT_HEIGHT);
		try {
			int w = Integer.parseInt(width);
			int h = Integer.parseInt(height);
			return w * h;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}

}
