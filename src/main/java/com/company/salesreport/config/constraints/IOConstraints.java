package com.company.salesreport.config.constraints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IOConstraints {

	@Value("${data.input.folder}")
	private String inputDir;

	@Value("${data.output.folder}")
	private String outputDir;

	public String getInputDir() {
		return inputDir;
	}

	public String getOutputDir() {
		return outputDir;
	}
	
	
}
