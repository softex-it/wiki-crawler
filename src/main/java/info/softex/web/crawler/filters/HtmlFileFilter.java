package info.softex.web.crawler.filters;

import java.io.File;
import java.io.FileFilter;

/**
 * 02/22/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class HtmlFileFilter implements FileFilter {
	
	@Override
	public boolean accept(File file) {
		String lcPath = file.getPath().toLowerCase();
		return file.isFile() && (lcPath.endsWith(".html") || lcPath.endsWith(".htm"));
	}

}
