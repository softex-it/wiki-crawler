package info.softex.web.crawler.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 
 * @since version 1.0,	04/05/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class ConversionUtilsTest {
	
	@SuppressWarnings("serial")
	private final static Map<String, String> HTML_COMPRESSION_MAP = new HashMap<String, String>() {{
		put("<p>my html  text</p>", "<p>my html text</p>");
		put("<p>my html  text with several <span> spaces   </span>  </p>", "<p>my html text with several <span> spaces </span> </p>");
		put("<p>my html  <pre>predefined  text</pre></p>", "<p>my html <pre>predefined  text</pre></p>");
		put("<p>my html <pre>predefined  text</pre> with border  conditions</p> ", "<p>my html <pre>predefined  text</pre> with border conditions</p>");
		put("  test  <pre> some  text 2 + 3   = 5</pre> is  <font> eq  </font><pre> some  text 4 + 1   = 5  </pre>    </p>      ", "test <pre> some  text 2 + 3   = 5</pre> is <font> eq </font><pre> some  text 4 + 1   = 5  </pre> </p>");
	}};

	@Test
	public void testCompressHtml() throws IOException {
		for (Map.Entry<String, String> entry : HTML_COMPRESSION_MAP.entrySet()) {
			String value = ConversionUtils.compressHtml(entry.getKey());
			assertEquals(entry.getValue(), value);
		}
	}
	
}
