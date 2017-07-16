package com.casic.simulation.device.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ALARM_DEVICE")
@SequenceGenerator(name = "SEQ_DEVICE_ID", sequenceName = "SEQ_DEVICE_ID", allocationSize=1,initialValue=1)
public class Device implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2023118078562324658L;
	private Long id;
	private String devCode;
	private String devName;
	private String latitude;
	private String longtitude;
	private String height;
	private String turnX = "0";
	private String turnY = "0";
	private String turnZ = "0";
	private String zoomX = "1";
	private String zoomY = "1";
	private String zoomZ = "1";
	private String gaocheng = "0";
	private String factory;
	private Date outDate;
	private Date installDate;
	private DeviceType deviceType;
	private AcceptPerson acceptPerson;
	private Boolean active = true;
	private Region region;
	private List<DeviceSensor> deviceSensors;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
	public List<DeviceSensor> getDeviceSensors() {
		return deviceSensors;
	}

	public void setDeviceSensors(List<DeviceSensor> deviceSensors) {
		this.deviceSensors = deviceSensors;
	}



	/**
	 * 埃德尔设备新增属性
	 */
	private String no; // 设备编码
	private String installPosition; // 安装位置
	private String simid; // SIM卡号
	private Date beginUseTime; // 开始使用时间

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEVICE_ID")
	@Column(name = "DBID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DEVCODE", nullable = false)
	public String getDevCode() {
		return devCode;
	}

	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}

	@Column(name = "DEVNAME", nullable = false)
	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	@Column(name = "LATITUDE", nullable = false)
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Column(name = "LONGTITUDE", nullable = false)
	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	@Column(name = "HEIGHT", nullable = false)
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Column(name = "TURNX")
	public String getTurnX() {
		return turnX;
	}

	public void setTurnX(String turnX) {
		this.turnX = turnX;
	}

	@Column(name = "TURNY")
	public String getTurnY() {
		return turnY;
	}

	public void setTurnY(String turnY) {
		this.turnY = turnY;
	}

	@Column(name = "TURNZ")
	public String getTurnZ() {
		return turnZ;
	}

	public void setTurnZ(String turnZ) {
		this.turnZ = turnZ;
	}

	@Column(name = "ZOOMX")
	public String getZoomX() {
		return zoomX;
	}

	public void setZoomX(String zoomX) {
		this.zoomX = zoomX;
	}

	@Column(name = "ZOOMY")
	public String getZoomY() {
		return zoomY;
	}

	public void setZoomY(String zoomY) {
		this.zoomY = zoomY;
	}

	@Column(name = "ZOOMZ")
	public String getZoomZ() {
		return zoomZ;
	}

	public void setZoomZ(String zoomZ) {
		this.zoomZ = zoomZ;
	}

	@Column(name = "GAOCHENG")
	public String getGaocheng() {
		return gaocheng;
	}

	public void setGaocheng(String gaocheng) {
		this.gaocheng = gaocheng;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICETYPE_ID", nullable = false)
	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	@Column(name = "FACTORY")
	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "OUTDATE")
	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) throws ParseException {
		this.outDate = outDate;
	}

	@Column(name = "INSTALLDATE")
	public Date getInstallDate() {
		return installDate;
	}

	public void setInstallDate(Date installDate) throws ParseException {
		this.installDate = installDate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCEPTPERSON_ID")
	public AcceptPerson getAcceptPerson() {
		return acceptPerson;
	}

	public void setAcceptPerson(AcceptPerson acceptPerson) {
		this.acceptPerson = acceptPerson;
	}

	@Column(name = "ACTIVE", nullable = false)
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGION_ID")
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Column(name = "BDATA_POSITION")
	public String getInstallPosition() {
		return installPosition;
	}

	public void setInstallPosition(String installPosition) {
		this.installPosition = installPosition;
	}

	@Column(name = "SIMID")
	public String getSimid() {
		return simid;
	}

	public void setSimid(String simid) {
		this.simid = simid;
	}

	@Column(name = "BEGINUSETIME")
	public Date getBeginUseTime() {
		return beginUseTime;
	}

	public void setBeginUseTime(Date beginUseTime) {
		this.beginUseTime = beginUseTime;
	}
}
