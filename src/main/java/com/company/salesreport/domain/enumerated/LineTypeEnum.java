package com.company.salesreport.domain.enumerated;

public enum LineTypeEnum {
	
	SALE("001"),
	SALESMAN("002"),
	CLIENT("003");
	
	private String code;
	
	private LineTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
