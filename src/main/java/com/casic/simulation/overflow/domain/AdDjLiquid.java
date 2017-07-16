package com.casic.simulation.overflow.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "AD_DJ_LIQUID")
@SequenceGenerator(name = "SEQ_AD_DJ_LIQUID", sequenceName = "SEQ_AD_DJ_LIQUID")
public class AdDjLiquid implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6250930880206117358L;
	private Long dbId;
	private String devId;
	private String liquidData;
	private String cell;
	private String signal;
	private String status;
	private Date uptime;
	private Date logtime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AD_DJ_LIQUID")
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
	
	@Column(name = "LIQUIDDATA")
	public String getLiquidData() {
		return liquidData;
	}
	public void setLiquidData(String liquidData) {
		this.liquidData = liquidData;
	}
	
	@Column(name = "CELL")
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	
	@Column(name = "SIGNAL")
	public String getSignal() {
		return signal;
	}
	public void setSignal(String signal) {
		this.signal = signal;
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
