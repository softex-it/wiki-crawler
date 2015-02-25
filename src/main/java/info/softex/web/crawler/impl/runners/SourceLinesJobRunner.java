package info.softex.web.crawler.impl.runners;

import info.softex.web.crawler.impl.BasicJobData;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since version 1.0,		04/04/2014
 * 
 * @modified version 2.1,	01/25/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class SourceLinesJobRunner extends TextLinesJobRunner {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	public SourceLinesJobRunner(File inInFile) throws IOException {
		super(inInFile);
	}
	
	public SourceLinesJobRunner(String inFilePath) throws IOException {
		super(new File(inFilePath));
	}
	
	@Override
	protected boolean populateJobData(BasicJobData inJobData, String inLine) {
		int divider = inLine.indexOf("  ");
		if (divider > 0) {
			String title = inLine.substring(0, divider).trim();
			String article = inLine.substring(divider + 2, inLine.length()).trim();
			inJobData.setTitle(title);
			inJobData.setContent(article);
			return true;
		} else {
			log.warn("Couldn't split the line: {}", inLine);
			return false;
		}
	}

}
