package info.softex.web.crawler.cli;

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
 * Command line interface for list downloader.
 * 
 * @since version 2.2,		04/18/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class ListDownloaderCLI {
	
	protected String inputFile;
	protected String outputDir;
	protected String httpURL;
	
	public ListDownloaderCLI() {}
	
	@SuppressWarnings({ "static-access"})
	public ListDownloaderCLI(String[] args) throws ParseException, IOException {
		
		// Create Options object
		Options options = new Options();
		
		options.addOption(
			OptionBuilder.withDescription("input file with words to download").
			hasArg().isRequired().create("i"));

		options.addOption(
			OptionBuilder.withDescription("output folder for the downloaded files and logs").
			hasArg().isRequired().create("o"));

		options.addOption(
			OptionBuilder.withDescription("http url to use for downloading").
			hasArg().isRequired().create("u"));
		
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(options, args);
		
		inputFile = cmd.getOptionValue("i");
		outputDir = cmd.getOptionValue("o");
		httpURL = cmd.getOptionValue("u");
		
		if (!FileUtils.fileExists(new File(inputFile))) {
			throw new IllegalArgumentException("Input file is not found: " + inputFile);
		}
		
		File outputDirFile = new File(outputDir);
		if (!outputDirFile.exists()) {
			outputDirFile.mkdirs();
		}
		
		if (!httpURL.startsWith("http")) {
			throw new IllegalArgumentException("HTTP URL for download is defined incorrectly: " + httpURL + 
				". It should start from http:// or https://");
		}
		if (httpURL.endsWith("/")) {
			httpURL = httpURL.substring(0, httpURL.length() - 1);
		}
		
	}
	
	public String getInputFile() {
		return inputFile;
	}
	
	public String getOutputDir() {
		return outputDir;
	}
	
	public String getHttpURL() {
		return httpURL;
	}
	
}
