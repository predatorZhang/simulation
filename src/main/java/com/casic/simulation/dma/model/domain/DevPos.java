package com.casic.simulation.dma.model.domain;

import com.casic.simulation.device.domain.Device;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DEVPOS")
@SequenceGenerator(name = "SEQ_DEVPOS_ID", sequenceName = "SEQ_DEVPOS_ID", allocationSize=1,initialValue=1)
public class DevPos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5360170502925645221L;
	private Long ID;                  //ID  
//	private String posID;             //监测点ID
//	private String eqtID;             //设备ID
	private String sensorType;        //传感器类型 选择输入 [{供水流量：Data3A}；{供水压力：Data40}；{供水噪声：Data02}；{排水液位：Data50}]
	private String PipeMaterial;      //管材 选择输入：球墨铸铁，铸铁，钢/镀锌，铜，钢筋混凝土/水泥，铅，铅银/铜合金，玻璃钢，陶瓷，PVC，PE
	private Integer PipeSize;         //管径  单位毫米
	private Double StartTotalValue;   //起始累计流量 流量传感器必填
	private Double LowInstantValue;   //低值报警
	private Double HighInstantValue;  //高值报警
	private PositionInfo positionInfo;
	private Device device;
	private Boolean active=true;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEVPOS_ID")
	@Column(name = "DBID")
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	
	@Column(name="SENSORTYPE")
	public String getSensorType() {
		return sensorType;
	}
	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
	
	@Column(name="PIPEMATERIAL")
	public String getPipeMaterial() {
		return PipeMaterial;
	}
	public void setPipeMaterial(String pipeMaterial) {
		PipeMaterial = pipeMaterial;
	}
	
	@Column(name="PIPESIZE")
	public Integer getPipeSize() {
		return PipeSize;
	}
	public void setPipeSize(Integer pipeSize) {
		PipeSize = pipeSize;
	}
	
	@Column(name="STARTTOTALVALUE")
	public Double getStartTotalValue() {
		return StartTotalValue;
	}
	public void setStartTotalValue(Double startTotalValue) {
		StartTotalValue = startTotalValue;
	}
	
	@Column(name="LOWINSTANTVALUE")
	public Double getLowInstantValue() {
		return LowInstantValue;
	}
	public void setLowInstantValue(Double lowInstantValue) {
		LowInstantValue = lowInstantValue;
	}
	
	@Column(name="HIGNINSTANTVALUE")
	public Double getHighInstantValue() {
		return HighInstantValue;
	}
	public void setHighInstantValue(Double highInstantValue) {
		HighInstantValue = highInstantValue;
	}
	
	@ManyToOne(fetch= FetchType.EAGER, targetEntity=PositionInfo.class)
	public PositionInfo getPositionInfo() {
		return positionInfo;
	}
	public void setPositionInfo(PositionInfo positionInfo) {
		this.positionInfo = positionInfo;
	}
	
	@ManyToOne(fetch= FetchType.EAGER, targetEntity=Device.class)
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	
	@Column(name="ACTIVE")
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
}
