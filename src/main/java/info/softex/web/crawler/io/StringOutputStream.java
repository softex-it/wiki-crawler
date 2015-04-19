package info.softex.web.crawler.io;

import java.io.ByteArrayOutputStream;

/**
 * 
 * @since version 2.2,		04/16/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class StringOutputStream extends ByteArrayOutputStream {

	public synchronized String toStringUTF8() {
		
		String result = null;
		
		try {
			byte[] bytes = toByteArray();
			result = new String(bytes, "UTF-8");	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return result;
		
	}
	
}
