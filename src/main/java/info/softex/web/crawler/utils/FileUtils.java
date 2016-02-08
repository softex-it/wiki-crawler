package info.softex.web.crawler.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since version 1.0,	03/17/2014
 * 
 * @modified version 2.1,	01/24/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class FileUtils {
	
	public final static String UTF8 = "UTF-8";
	
	public final static String EXT_HTML = ".html";
	public final static String EXT_HTM = ".htm";
	
	@SuppressWarnings("serial")
	private final static Map<String, String> FILE_NAME_REPLACEMENTS = new HashMap<String, String>() {{
		put("\"", "&#34;");
		put("\\*", "&#42;");
		put("/", "&#47;");
		put(":", "&#58;");
		put("<", "&#60;");
		put(">", "&#62;");
		put("\\?", "&#63;");
		put("\\\\", "&#92;");
		put("\\|", "&#124;");
	}};
	
	public static String file2String(File file) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(file));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	public static File string2File(String filePath, String content) throws IOException {
		File outFile = new File(filePath);
		Writer writer = openBufferedWriter(outFile, UTF8);
		writer.write(content);
		writer.flush();
		writer.close();
		return outFile;
	}
	
	/**
	 * Opens a {@link FileOutputStream} for the specified file, checking and
	 * creating the parent directory if it does not exist.
	 */
	public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canWrite() == false) {
				throw new IOException("File '" + file + "' cannot be written to");
			}
		} else {
			final File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent + "' could not be created");
				}
			}
		}
		return new FileOutputStream(file, append);
	}
	
	/**
	 * Opens a {@link FileOutputStream} for the specified file, checking and
	 * creating the parent directory if it does not exist.
	 */
	public static FileOutputStream openOutputStream(final File file) throws IOException {
		return openOutputStream(file, false);
	}
	
	public static BufferedWriter openBufferedWriter(File file, String encoding) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(openOutputStream(file, false), encoding));
	}
	
	public static void closeWriter(BufferedWriter writer) throws IOException {
		if (writer != null) {
			writer.flush();
			writer.close();
		}
	}
	
	public static boolean fileExists(File f) throws IOException {
		return f.exists() && f.getCanonicalPath().endsWith(f.getName());
	}
	
	public static void copyFile(File sourceFile, File destFile, boolean append) throws IOException {
		
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile, append).getChannel();
	        
	        // Move position to the end if file must be appended
	        long position = 0;
	        if (append) {
	        	position = destination.size();
	        }
	        
	        destination.transferFrom(source, position, source.size());
	    } finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
	
	public static String title2FileName(String title) {
		for (Map.Entry<String, String> repEntry : FILE_NAME_REPLACEMENTS.entrySet()) {
			title = title.replaceAll(repEntry.getKey(), repEntry.getValue());
		}
		return title.trim() + EXT_HTML;
	}
	
	public static String fileName2Title(String fileName) {
		for (Map.Entry<String, String> repEntry : FILE_NAME_REPLACEMENTS.entrySet()) {
			fileName = fileName.replaceAll(repEntry.getValue(), repEntry.getKey());
		}
		String lcFileName = fileName.toLowerCase();
		if (lcFileName.endsWith(EXT_HTML)) {
			return fileName.substring(0, fileName.length() - EXT_HTML.length()).trim();
		} else if (lcFileName.endsWith(EXT_HTM)) {
			return fileName.substring(0, fileName.length() - EXT_HTM.length()).trim();
		} else {
			return fileName.trim();
		}
	}

}
