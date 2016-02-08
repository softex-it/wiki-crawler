package info.softex.web.crawler.cli;

import info.softex.web.crawler.tools.attributes.MediaDownloadRate;
import info.softex.web.crawler.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Command line interface for page processors.
 * 
 * @since version 2.2,		04/18/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class PageProcessorCLI {
	
	protected String inputDir;
	protected String outputDir;
	protected String httpURL;
	
	protected MediaDownloadRate mediaRate;
	
	@SuppressWarnings("static-access")
	public PageProcessorCLI(String[] args) throws ParseException, IOException {
		
		// Create Options object
		Options options = new Options();

		options.addOption(
			OptionBuilder.withDescription("input directory with the downloaded pages").
			hasArg().isRequired().create("i"));
		
		options.addOption(
			OptionBuilder.withDescription("output directory for the processed pages and media").
			hasArg().isRequired().create("o"));
		
		options.addOption(
			OptionBuilder.withDescription("rate of media download").
			hasArg().isRequired().create("m"));
		
		options.addOption(
			OptionBuilder.withDescription("http url to use for downloading").
			hasArg().isRequired().create("u"));
		
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(options, args);
		
		inputDir = cmd.getOptionValue("i");
		outputDir = cmd.getOptionValue("o");
		httpURL = cmd.getOptionValue("u");
		
		// Validate media rate
		String mediaRateString = cmd.getOptionValue("m");
		if ("full".equalsIgnoreCase(mediaRateString)) {
			mediaRate = MediaDownloadRate.FULL;
		} else if ("none".equalsIgnoreCase(mediaRateString)) {
			mediaRate = MediaDownloadRate.NONE;
		} else if ("light".equalsIgnoreCase(mediaRateString)) {
			mediaRate = MediaDownloadRate.LIGHT;
		} else {
			throw new IllegalArgumentException("Maedia download rate must be 'full', 'none', or 'light'. The received value is " + mediaRateString);
		}
		
		// Validate http url
		if (!httpURL.startsWith("http")) {
			throw new IllegalArgumentException("HTTP URL for download is defined incorrectly: " + httpURL + 
				". It should start from http:// or https://");
		}
		if (httpURL.endsWith("/")) {
			httpURL = httpURL.substring(0, httpURL.length() - 1);
		}
		
		// Validate input directory
		File inputDirFile = new File(inputDir);
		if (!FileUtils.fileExists(inputDirFile) || inputDirFile.isFile()) {
			throw new IllegalArgumentException("Input directory doesn't exist or it's a file " + inputDir);
		}
		
		// Validate output directory
		File outputDirFile = new File(outputDir);
		if (FileUtils.fileExists(outputDirFile) && outputDirFile.isFile()) {
			throw new IllegalArgumentException("Output directory is a file but a folder is expected: " + outputDir);
		}
		outputDirFile.mkdirs();
		
	}
	
	public String getInputDir() {
		return inputDir;
	}
	
	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}
	
	public String getOutputDir() {
		return outputDir;
	}
	
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	public MediaDownloadRate getMediaDownloadRate() {
		return mediaRate;
	}
	
	public String getHttpURL() {
		return httpURL;
	}

}
