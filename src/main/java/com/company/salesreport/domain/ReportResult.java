package com.company.salesreport.domain;

public class ReportResult {

	private Long totalAmountClient;
	private Long totalAmountSalesMen;
	private String worstSalesMan;
	private Long mostExpensiveSaleId;

	public ReportResult() {
		super();
		this.totalAmountClient = 0L;
		this.totalAmountSalesMen = 0L;
	}

	public Long getTotalAmountClient() {
		return totalAmountClient;
	}

	public void setTotalAmountClient(Long totalAmountClient) {
		this.totalAmountClient = totalAmountClient;
	}

	public Long getTotalAmountSalesMen() {
		return totalAmountSalesMen;
	}

	public void setTotalAmountSalesMen(Long totalAmountSalesMen) {
		this.totalAmountSalesMen = totalAmountSalesMen;
	}

	public void incrementTotalAmountClient() {
		this.totalAmountClient++;
	}

	public void incrementTotalAmountSalesMen() {
		this.totalAmountSalesMen++;
	}

	public String getWorstSalesMan() {
		return worstSalesMan;
	}

	public void setWorstSalesMan(String worstSalesMan) {
		this.worstSalesMan = worstSalesMan;
	}

	public Long getMostExpensiveSaleId() {
		return mostExpensiveSaleId;
	}

	public void setMostExpensiveSaleId(Long mostExpensiveSaleId) {
		this.mostExpensiveSaleId = mostExpensiveSaleId;
	}

}
