package com.company.salesreport.job.processor;

import java.util.Map;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.item.ItemProcessor;

import com.company.salesreport.domain.LineReportResult;
import com.company.salesreport.domain.enumerated.LineTypeEnum;
import com.company.salesreport.job.fieldmapper.EntryFieldSetMapper;

public class SaleReportItemProcessor implements ItemProcessor<Map<String, String>,LineReportResult>,ItemProcessListener<Map<String, String>, LineReportResult>{

	@Override
	public LineReportResult process(Map<String, String> item) throws Exception {

		final LineReportResult result = new LineReportResult();

		final String itemCode = item.get(EntryFieldSetMapper.LINE_TYPE_CODE);
		switch (itemCode) {
		case EntryFieldSetMapper.CLIENT_CODE:
			this.processClient(item,result);
			break;
		case EntryFieldSetMapper.SALE_CODE:
			this.processSell(item,result);
			break;
		case EntryFieldSetMapper.SALESMAN_CODE:
			this.processSaleman(item,result);
			break;
		default:
			break;
		}

		return result;
	}

	private void processSaleman(Map<String, String> item, LineReportResult result) {
		result.setType(LineTypeEnum.SALESMAN);

	}

	private void processSell(Map<String, String> item, LineReportResult result) {
		result.setType(LineTypeEnum.SALE);
		result.setSalesMan(item.get(EntryFieldSetMapper.COL3));
		result.setSaleId(Long.valueOf(item.get(EntryFieldSetMapper.COL1)));
		
		final String saleString = item.get(EntryFieldSetMapper.COL2);
		
		final String[] saleData = saleString.substring(1,saleString.length()-1).split(",");
		final String[] productsIds = saleData[0].split("-");
		final String[] itemsQuantity = saleData[1].split("-");
		final String[] itemsValues = saleData[2].split("-");
		
		Double saleTotal = 0d;
		for (int i = 0; i < productsIds.length; i++) {
			Double productSaleValue = Double.valueOf(itemsQuantity[i]) * Double.valueOf(itemsValues[i]);
			saleTotal += productSaleValue;
		}
		
		result.setSaleValue(saleTotal);
	}

	private void processClient(Map<String, String> item, LineReportResult result) {
		result.setType(LineTypeEnum.CLIENT);
	}

	@Override
	public void beforeProcess(Map<String, String> item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterProcess(Map<String, String> item, LineReportResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProcessError(Map<String, String> item, Exception e) {
		// TODO Auto-generated method stub
		System.out.println();
		
	}



}
