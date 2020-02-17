package com.company.salesreport.job.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.support.AbstractFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.company.salesreport.domain.LineReportResult;
import com.company.salesreport.domain.ReportResult;
import com.company.salesreport.service.FileService;

public class ReportResultFileWriter extends AbstractFileItemWriter<LineReportResult>
		implements ItemWriter<LineReportResult>, FlatFileFooterCallback {

	private ReportResult result;
	private Double mostExpensiveSaleValue;
	private Map<String, Double> salesManSalesValues;
	private String fileName;
	
    @Autowired
    private FileService fileService;

	public ReportResultFileWriter(String fileName) {
		super();
		this.mostExpensiveSaleValue = 0d;
		this.fileName = fileName;
		result = new ReportResult();
		salesManSalesValues = new HashMap<>();
		this.setFooterCallback(this);
	}

	public void writeFooter(Writer writer) throws IOException {
		writer.write("Total Client Amount Processed: " + result.getTotalAmountClient() + "\n");
		writer.write("Total Salesman Amount Processed: " + result.getTotalAmountSalesMen() + "\n");
		writer.write("Biggest sale id: " + result.getMostExpensiveSaleId() + "\n");
		writer.write("Worst Salesman Name: " + calculateWorstSalesMan());
		this.fileService.markFileAsFinished(this.fileName);
	}

	private String calculateWorstSalesMan() {
		Double minValue = null;
		String worstSalesMan = "";
		for (Map.Entry<String, Double> entry : salesManSalesValues.entrySet()) {
			if (minValue == null || entry.getValue() <= minValue) {
				worstSalesMan = entry.getKey();
				minValue = entry.getValue();
			}
		}

		return worstSalesMan;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	protected String doWrite(List<? extends LineReportResult> items) {

		for (LineReportResult item : items) {
			switch (item.getType()) {
			case SALE:
				checkSell(item);
				break;
			case CLIENT:
				this.result.incrementTotalAmountClient();
				break;

			case SALESMAN:
				this.result.incrementTotalAmountSalesMen();
				break;

			default:
				break;
			}
		}
		return "";
	}

	private void checkSell(LineReportResult item) {
		if (this.mostExpensiveSaleValue < item.getSaleValue()) {
			this.mostExpensiveSaleValue = item.getSaleValue();
			this.result.setMostExpensiveSaleId(item.getSaleId());
		}
		if (!this.salesManSalesValues.containsKey(item.getSalesMan())) {
			this.salesManSalesValues.put(item.getSalesMan(), 0d);
		}
		this.salesManSalesValues.put(item.getSalesMan(),
				this.salesManSalesValues.get(item.getSalesMan()) + item.getSaleValue());
	}

}