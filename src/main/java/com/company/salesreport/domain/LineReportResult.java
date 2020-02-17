package com.company.salesreport.domain;

import com.company.salesreport.domain.enumerated.LineTypeEnum;

public class LineReportResult {

	private LineTypeEnum type;
	private Double saleValue;
	private Long saleId;
	private String salesMan;

	public LineReportResult() {
		saleValue = 0d;
	}

	public LineTypeEnum getType() {
		return type;
	}

	public void setType(LineTypeEnum type) {
		this.type = type;
	}

	public Double getSaleValue() {
		return saleValue;
	}

	public void setSaleValue(Double saleValue) {
		this.saleValue = saleValue;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public String getSalesMan() {
		return salesMan;
	}

	public void setSalesMan(String salesMan) {
		this.salesMan = salesMan;
	}

	
}
