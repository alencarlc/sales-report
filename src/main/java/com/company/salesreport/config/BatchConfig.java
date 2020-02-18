package com.company.salesreport.config;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.company.salesreport.config.constraints.IOConstraints;
import com.company.salesreport.domain.LineReportResult;
import com.company.salesreport.job.fieldmapper.EntryFieldSetMapper;
import com.company.salesreport.job.processor.SaleReportItemProcessor;
import com.company.salesreport.job.writer.ReportResultFileWriter;

@Component
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	private IOConstraints ioConstraints;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;


	private DateFormat dateFormat;

	@PostConstruct
	private void init() {
		dateFormat = new SimpleDateFormat("yyyyMMddHHmmss_");
	}

	@Bean
	public Job processingJobBean(Step processingStep) {
		return jobBuilderFactory.get("salesReportJob").incrementer(new RunIdIncrementer()).flow(processingStep).end().build();
	}

	@Bean
	public Step processingStepBean(ItemReader<Map<String, String>> reader,
			ItemProcessor<Map<String, String>, LineReportResult> processor, ItemWriter<LineReportResult> writer) {
		
		return stepBuilderFactory.get("fileProcessStep").<Map<String, String>, LineReportResult>chunk(5).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	public ItemProcessor<Map<String, String>, LineReportResult> processor() {
		return new SaleReportItemProcessor();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Map<String, String>> reader(@Value("#{jobParameters['fileName']}") String fileName)
			throws Exception {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer("ç");
		tokenizer.setNames(new String[] { EntryFieldSetMapper.LINE_TYPE_CODE, EntryFieldSetMapper.COL1, EntryFieldSetMapper.COL2,
				EntryFieldSetMapper.COL3 });

		DefaultLineMapper<Map<String, String>> mapper = new DefaultLineMapper<>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(new EntryFieldSetMapper());
		mapper.afterPropertiesSet();

		FlatFileItemReader<Map<String, String>> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(fileName));
		reader.setLineMapper(mapper);
		reader.afterPropertiesSet();
		reader.setName("ItemReaderFlatItem");
		
		reader.setEncoding("UTF-8");
		return reader;
	}

	@Bean
	@StepScope
	public ReportResultFileWriter writer(@Value("#{jobParameters['fileName']}") String fileName) {
		ReportResultFileWriter writer = new ReportResultFileWriter(fileName);
		String outputFileName = this.ioConstraints.getOutputDir() + dateFormat.format(new Date())
				+ fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.length());
		writer.setResource(new FileSystemResource(outputFileName));
		writer.setName("ReportResultFileWriter");
		return writer;
	}

}
