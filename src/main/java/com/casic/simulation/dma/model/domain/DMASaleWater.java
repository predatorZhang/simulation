package com.casic.simulation.dma.model.domain;

import com.casic.simulation.dma.model.domain.DMAInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ALARM_DMA_SALE_WATER")
@SequenceGenerator(name = "SEQ_DMA_SALE_WATER", sequenceName = "SEQ_DMA_SALE_WATER", allocationSize=1,initialValue=1)
public class DMASaleWater implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4074613669372207428L;
	private Long id;               //ID
	private Date startDate;        //起始时间
	private Date endDate;          //结束时间
	private Double saleWater;      //售水量
	private Double noValueWater;   //无收益水量
	private DMAInfo dmaInfo;       //关联分区
	private Date insertDate;
	private Date updateDate;
	private int familyNum;        //该售水量包含用户数
	private Boolean active;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DMA_SALE_WATER")
	@Column(name = "DBID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="STARTDATE")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name="ENDDATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name="SALEWATER")
	public Double getSaleWater() {
		return saleWater;
	}
	public void setSaleWater(Double saleWater) {
		this.saleWater = saleWater;
	}

	@Column(name="NOVALUEWATER")
	public Double getNoValueWater() {
		return noValueWater;
	}
	public void setNoValueWater(Double noValueWater) {
		this.noValueWater = noValueWater;
	}

	@ManyToOne(fetch= FetchType.LAZY, targetEntity=DMAInfo.class)
	public DMAInfo getDmaInfo() {
		return dmaInfo;
	}
	public void setDmaInfo(DMAInfo dmaInfo) {
		this.dmaInfo = dmaInfo;
	}

	@Column(name="INSERTDATE")
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@Column(name="UPDATEDATE")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name="FAMILYNUM")
	public int getFamilyNum() {
		return familyNum;
	}
	public void setFamilyNum(int familyNum) {
		this.familyNum = familyNum;
	}

	@Column(name="ACTIVE")
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
}
