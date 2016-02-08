package info.softex.web.crawler.utils;

import info.softex.web.crawler.filters.ImageFileFilter;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @since version 1.0,	04/06/2014
 * 
 * @modified version 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class ImageCopyUtils {
	
	public static void main(String[] args) throws IOException {
		
		ImageFileFilter dd = new ImageFileFilter();
		
		File f = new File("/ext/wiki/media");
		
		File [] fls = f.listFiles(dd);
		
		for (int i = 0; i < fls.length; i++) {
			FileUtils.copyFile(fls[i], new File("/ext/wiki/media-f/" + fls[i].getName()), false);
		}

	}

}
