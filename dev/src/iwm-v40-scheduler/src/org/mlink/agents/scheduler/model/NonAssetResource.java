package org.mlink.agents.scheduler.model;

public class NonAssetResource {
	Long stockNumber;
	Integer quantityOnHand;
	Integer type;
	
	public Long getStockNumber() { return stockNumber;}
	public Integer getQuantityOnHand() { return quantityOnHand;}
	public Integer getType() { return type;}
	
	public void setStockNumber(Long l) { stockNumber=l;}
	public void setQuantityOnHand(Integer i) { quantityOnHand=i;}
	public void setType(Integer i) { type=i;}
}
