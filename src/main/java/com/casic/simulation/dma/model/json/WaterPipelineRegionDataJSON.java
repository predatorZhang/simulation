package com.casic.simulation.dma.model.json;

import com.casic.simulation.dma.model.domain.DMAInfo;

public class WaterPipelineRegionDataJSON {
	private Long id; // ID
	private String name; // 分区名称
	private String no; // 分区编码
	private String bDataParent_DMA = "-1";// 上级分区
	private Double userCount = 0.0; // 用户数量
	private Double normalWater = 0.0; // 正常夜间用水量
	private Double pipeLeng = 0.0; // 管道总长度
	private Double userPipeLeng = 0.0; // 户表后管道总长度
	private Double pipeLinks = 0.0; // 管道连接总数
	private Double icf = 0.0; // ICF参数
	private Double leakControlRate = 0.0; // 阶段漏损控制目标
	private Double saleWater = 0.0; // 日售水量
	
	public WaterPipelineRegionDataJSON(DMAInfo dmaInfo) {
		this.id = dmaInfo.getID();
		this.name = dmaInfo.getName();
		this.no = dmaInfo.getNO();
		this.bDataParent_DMA = dmaInfo.getBDataParent_DMA();
		this.userCount = dmaInfo.getUserCount();
		this.normalWater = dmaInfo.getNormalWater();
		this.pipeLeng = dmaInfo.getPipeLeng();
		this.userPipeLeng = dmaInfo.getUserPipeLeng();
		this.pipeLinks = dmaInfo.getPipeLinks();
		this.icf = dmaInfo.getIcf();
		this.leakControlRate = dmaInfo.getLeakControlRate();
		this.saleWater = dmaInfo.getSaleWater();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getbDataParent_DMA() {
		return bDataParent_DMA;
	}

	public void setbDataParent_DMA(String bDataParent_DMA) {
		this.bDataParent_DMA = bDataParent_DMA;
	}

	public Double getUserCount() {
		return userCount;
	}

	public void setUserCount(Double userCount) {
		this.userCount = userCount;
	}

	public Double getNormalWater() {
		return normalWater;
	}

	public void setNormalWater(Double normalWater) {
		this.normalWater = normalWater;
	}

	public Double getPipeLeng() {
		return pipeLeng;
	}

	public void setPipeLeng(Double pipeLeng) {
		this.pipeLeng = pipeLeng;
	}

	public Double getUserPipeLeng() {
		return userPipeLeng;
	}

	public void setUserPipeLeng(Double userPipeLeng) {
		this.userPipeLeng = userPipeLeng;
	}

	public Double getPipeLinks() {
		return pipeLinks;
	}

	public void setPipeLinks(Double pipeLinks) {
		this.pipeLinks = pipeLinks;
	}

	public Double getIcf() {
		return icf;
	}

	public void setIcf(Double icf) {
		this.icf = icf;
	}

	public Double getLeakControlRate() {
		return leakControlRate;
	}

	public void setLeakControlRate(Double leakControlRate) {
		this.leakControlRate = leakControlRate;
	}

	public Double getSaleWater() {
		return saleWater;
	}

	public void setSaleWater(Double saleWater) {
		this.saleWater = saleWater;
	}
}
