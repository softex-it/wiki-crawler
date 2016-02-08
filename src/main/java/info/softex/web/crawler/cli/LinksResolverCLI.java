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
 * Command line interface for links resolver.
 * 
 * @since version 2.2,		04/18/2015
 * 
 * @author Dmitry Viktorov
 *
 */
public class LinksResolverCLI {
	
	protected String outputFile;
	protected String httpURL;
	protected String initialKey;
	protected int recursionDepth = -1;
	
	@SuppressWarnings({ "static-access"})
	public LinksResolverCLI(String[] args) throws ParseException, IOException {
		
		// Create Options object
		Options options = new Options();

		options.addOption(
			OptionBuilder.withDescription("output file for the extracted keys").
			hasArg().isRequired().create("o"));

		options.addOption(
			OptionBuilder.withDescription("http url to use for downloading").
			hasArg().isRequired().create("u"));
		
		options.addOption(
			OptionBuilder.withDescription("initial key to use for downloading").
			hasArg().create("k"));
		
		options.addOption(
			OptionBuilder.withDescription("recursion depth for links resolution").
			hasArg().isRequired().create("r"));
		
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(options, args);

		outputFile = cmd.getOptionValue("o");
		httpURL = cmd.getOptionValue("u");
		initialKey = cmd.getOptionValue("k");

		// Get the integer for recursion depth
		String recursionDepthString = cmd.getOptionValue("r");
		try {
			recursionDepth = Integer.parseInt(recursionDepthString);
		} catch (Exception e) {};
		
		// Validate output file
		File output = new File(outputFile);
		if (FileUtils.fileExists(output)) {
			throw new IllegalArgumentException("File already exists: " + outputFile + ". Please provide another path.");
		}
		output.getParentFile().mkdirs();
			
		// Validate HTTP URL
		if (!httpURL.startsWith("http")) {
			throw new IllegalArgumentException("HTTP URL for download is defined incorrectly: " + httpURL + ". It should start from http:// or https://");
		}
		if (httpURL.endsWith("/")) {
			httpURL = httpURL.substring(0, httpURL.length() - 1);
		}
		
		// Validate recursion depth
		if (recursionDepth <= 0) {
			throw new IllegalArgumentException("Recursion depth must be an integer greater than 0: " + recursionDepthString);
		}
		
	}
	
	public String getOutputFile() {
		return outputFile;
	}
	
	public void setOutputFile(String inOutputFile) {
		this.outputFile = inOutputFile;
	}

	public String getHttpURL() {
		return httpURL;
	}

	public void setHttpURL(String httpURL) {
		this.httpURL = httpURL;
	}
	
	public String getInitialKey() {
		return initialKey;
	}

	public void setInitialKey(String initialKey) {
		this.initialKey = initialKey;
	}

	public int getRecursionDepth() {
		return recursionDepth;
	}
	
	public void setRecursionDepth(int inRecursionDepth) {
		this.recursionDepth = inRecursionDepth;
	}

}
