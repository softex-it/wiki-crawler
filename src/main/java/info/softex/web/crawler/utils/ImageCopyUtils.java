package info.softex.web.crawler.utils;

import info.softex.web.crawler.filters.ImageFileFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ImageCopyUtils {
	
	public static void main(String[] args) throws IOException {
		
		ImageFileFilter dd = new ImageFileFilter();
		
		File f = new File("/ext/wiki/media");
		
		File [] fls = f.listFiles(dd);
		
		for (int i = 0; i < fls.length; i++) {
			copyFileUsingChannel(fls[i], new File("/ext/wiki/media-f/" + fls[i].getName()));
		}

	}
	
	private static void copyFileUsingChannel(File source, File dest) throws IOException {
	    FileChannel sourceChannel = null;
	    FileChannel destChannel = null;
	    try {
	        sourceChannel = new FileInputStream(source).getChannel();
	        destChannel = new FileOutputStream(dest).getChannel();
	        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       }finally{
	           sourceChannel.close();
	           destChannel.close();
	       }
	}

}
