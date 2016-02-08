package info.softex.web.crawler.filters;

import java.io.File;
import java.io.FileFilter;

/**
 * 02/22/2014
 * 
 * @author Dmitry Viktorov
 *
 */
public class ImageFileFilter implements FileFilter {
	
	@Override
	public boolean accept(File file) {
		
		if (file.isFile()) {
			String lcPath = file.getPath().toLowerCase();
			return accept(lcPath);	
		}
		
		return false;
		
	}
	
	public boolean accept(String filePath) {
		
		if (filePath != null && !filePath.isEmpty()) {
			String lcPath = filePath.toLowerCase();
			if (lcPath.endsWith(".jpg") || lcPath.endsWith(".jpeg") || 
				lcPath.endsWith(".png") || lcPath.endsWith(".gif")) {
				return true;
			}
		}
		
		return false;
		
	}
	
}
