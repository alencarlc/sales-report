package com.company.salesreport.job.transformer;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.company.salesreport.service.FileService;

@Component
public class FileToJobTransformer implements ApplicationContextAware {

	@Autowired
	private Job job;

	@SuppressWarnings("unused")
	private ApplicationContext context;

	@Autowired
	private FileService fileService;

	Logger logger = LoggerFactory.getLogger(FileToJobTransformer.class);

	@ServiceActivator(inputChannel = "fileInputChannel", outputChannel = "jobChannel", requiresReply = "false")
	public JobLaunchRequest transform(File aFile) {
		final String filePath = aFile.getAbsolutePath();
		if (!this.fileService.isFileProcessed(filePath)) {
			this.fileService.markFileAsProcessed(filePath);
			String fileName = aFile.getAbsolutePath();

			JobParameters jobParameters = new JobParametersBuilder()
					.addString("fileName", fileName)
					.addDate("date", new Date())
					.toJobParameters();


			JobLaunchRequest request = new JobLaunchRequest(job, jobParameters);

			return request;
		} else {
			logger.error("processamento descartado "+aFile);
			return null;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}