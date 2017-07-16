package com.casic.simulation.dma.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "AD_DJ_FLOW")
@SequenceGenerator(name = "SEQ_AD_DJ_FLOW_ID", sequenceName = "SEQ_AD_DJ_FLOW_ID", allocationSize=1,initialValue=1)
public class SensorFlowRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8183493315905464861L;
	private Long dbId;
	private String devId;
	private String insData;
	private String netData;
	private String posDate;
	private String negData;
	private String signal;
	private String cell;
	private String status;
	private Date uptime;
	private Date logtime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AD_DJ_FLOW_ID")
	@Column(name = "DBID")
	public Long getDbId() {
		return dbId;
	}
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	
	@Column(name = "DEVCODE")
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	
	@Column(name = "INSDATA")
	public String getInsData() {
		return insData;
	}
	public void setInsData(String insData) {
		this.insData = insData;
	}
	
	@Column(name = "NETDATA")
	public String getNetData() {
		return netData;
	}
	public void setNetData(String netData) {
		this.netData = netData;
	}
	
	@Column(name = "POSDATA")
	public String getPosDate() {
		return posDate;
	}
	public void setPosDate(String posDate) {
		this.posDate = posDate;
	}
	
	@Column(name = "NEGDATA")
	public String getNegData() {
		return negData;
	}
	public void setNegData(String negData) {
		this.negData = negData;
	}
	
	@Column(name = "SIGNAL")
	public String getSignal() {
		return signal;
	}
	public void setSignal(String signal) {
		this.signal = signal;
	}
	
	@Column(name = "CELL")
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "UPTIME")
	public Date getUptime() {
		return uptime;
	}
	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}
	
	@Column(name = "LOGTIME")
	public Date getLogtime() {
		return logtime;
	}
	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}
}
