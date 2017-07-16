package com.casic.simulation.dma.model.json;



import com.casic.simulation.dma.model.domain.DMASaleWater;

import java.text.SimpleDateFormat;

public class DMASaleWaterJSON {
	private Long id;               //ID
	private String startDate;        //起始时间
	private String endDate;          //结束时间
	private Double saleWater;      //售水量
	private Double noValueWater;   //无收益水量
	private Long dmaId;
	private String dmaName;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public DMASaleWaterJSON(DMASaleWater dmaSaleWater) {
		this.id = dmaSaleWater.getId();
		this.startDate = dateFormat.format(dmaSaleWater.getStartDate());
		this.endDate = dateFormat.format(dmaSaleWater.getEndDate());
		this.saleWater = dmaSaleWater.getSaleWater();
		this.noValueWater = dmaSaleWater.getNoValueWater();
		if(null != dmaSaleWater.getDmaInfo()) {
			this.dmaId = dmaSaleWater.getDmaInfo().getID();
			this.dmaName = dmaSaleWater.getDmaInfo().getName();
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Double getSaleWater() {
		return saleWater;
	}
	public void setSaleWater(Double saleWater) {
		this.saleWater = saleWater;
	}
	public Double getNoValueWater() {
		return noValueWater;
	}
	public void setNoValueWater(Double noValueWater) {
		this.noValueWater = noValueWater;
	}
	public Long getDmaId() {
		return dmaId;
	}

	public void setDmaId(Long dmaId) {
		this.dmaId = dmaId;
	}

	public String getDmaName() {
		return dmaName;
	}

	public void setDmaName(String dmaName) {
		this.dmaName = dmaName;
	}
}
