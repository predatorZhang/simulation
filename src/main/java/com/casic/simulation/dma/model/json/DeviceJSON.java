package com.casic.simulation.dma.model.json;

import com.casic.simulation.device.domain.Device;
import com.casic.simulation.dma.model.domain.PosDMA;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceJSON {
	private Long id;
	private String devCode;
	private String devName;
	private Long typeId;
	private String typeName;
	private String location;
	private String latitude;
	private String longtitude;
	private String height;
	private String turnX;
	private String turnY;
	private String turnZ;
	private String zoomX;
	private String zoomY;
	private String zoomZ;
	private String gaocheng;
	private String factory;
	private String outDate;
	private String installDate;
	private Boolean active;
	private Date createdDate;
	private Date deleteDate;
	private String createPerson;
	private String deletePerson;
	private Long personId;
	private String personName;
	private String dmaName;
	private String posName;
    private String btnConfigSet;

    public String getBtnConfigSet()
    {
        return btnConfigSet;
    }
    public void setBtnConfigSet(String btnConfigSet)
    {
        this.btnConfigSet = btnConfigSet;
    }


    /**
	 * 埃德尔设备新增属性
	 */
	private String no; // 设备编码
	private String installPosition; // 安装位置
	private String simid; // SIM卡号
	private String beginUseTime; // 开始使用时间

	private String monitorType;//监测点监测类型
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public DeviceJSON() {

	}

	public DeviceJSON(Device device) {
		initDeviceJSON(device, null);
	}
	
	public DeviceJSON(Device device, PosDMA posDma) {
		initDeviceJSON(device, posDma);
	}

	public DeviceJSON(Long id, String devName) {
		this.id =id;
		this.devName=devName;

	}

	private void initDeviceJSON(Device device, PosDMA posDma) {
		this.id = device.getId();
		this.devCode = device.getDevCode();
		this.devName = device.getDevName();
		if(null!=device.getDeviceType()){
			this.typeId=device.getDeviceType().getId();
			this.typeName = device.getDeviceType().getTypeName();
		}
		this.latitude = device.getLatitude();
		this.longtitude = device.getLongtitude();
		this.height = device.getHeight();
		this.turnX = device.getTurnX();
		this.turnY = device.getTurnY();
		this.turnZ = device.getTurnZ();
		this.zoomX = device.getZoomX();
		this.zoomY = device.getZoomY();
		this.zoomZ = device.getZoomZ();
		this.gaocheng = device.getGaocheng();
		this.factory = device.getFactory();
		if (null != device.getOutDate()) {
			this.outDate = dateFormat.format(device.getOutDate());
		}
		if (null != device.getInstallDate()) {
			this.installDate = dateFormat.format(device.getInstallDate());
		}
		this.active = device.getActive();

		/**
		 * 埃德尔设备新增属性
		 */
		this.no = device.getNo();
		this.simid = device.getSimid();
		if (null != device.getInstallDate()) {
			this.installDate = dateFormat.format(device.getInstallDate());
		}
		if (null != device.getBeginUseTime()) {
			this.beginUseTime = dateFormat.format(device.getBeginUseTime());
		}
		if(null != posDma && null != posDma.getDmaInfo()) {
			this.dmaName = posDma.getDmaInfo().getName();
			if(null != posDma.getPositionInfo()) {
				this.posName = posDma.getPositionInfo().getName();
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDevCode() {
		return devCode;
	}

	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getTurnX() {
		return turnX;
	}

	public void setTurnX(String turnX) {
		this.turnX = turnX;
	}

	public String getTurnY() {
		return turnY;
	}

	public void setTurnY(String turnY) {
		this.turnY = turnY;
	}

	public String getTurnZ() {
		return turnZ;
	}

	public void setTurnZ(String turnZ) {
		this.turnZ = turnZ;
	}

	public String getZoomX() {
		return zoomX;
	}

	public void setZoomX(String zoomX) {
		this.zoomX = zoomX;
	}

	public String getZoomY() {
		return zoomY;
	}

	public void setZoomY(String zoomY) {
		this.zoomY = zoomY;
	}

	public String getZoomZ() {
		return zoomZ;
	}

	public void setZoomZ(String zoomZ) {
		this.zoomZ = zoomZ;
	}

	public String getGaocheng() {
		return gaocheng;
	}

	public void setGaocheng(String gaocheng) {
		this.gaocheng = gaocheng;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getInstallDate() {
		return installDate;
	}

	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getDeletePerson() {
		return deletePerson;
	}

	public void setDeletePerson(String deletePerson) {
		this.deletePerson = deletePerson;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getInstallPosition() {
		return installPosition;
	}

	public void setInstallPosition(String installPosition) {
		this.installPosition = installPosition;
	}

	public String getSimid() {
		return simid;
	}

	public void setSimid(String simid) {
		this.simid = simid;
	}

	public String getBeginUseTime() {
		return beginUseTime;
	}

	public void setBeginUseTime(Date beginUseTime) {
		if (null != beginUseTime) {
			this.beginUseTime = dateFormat.format(beginUseTime);
		} else {
			this.beginUseTime = "";
		}
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	
	public String getDmaName() {
		return dmaName;
	}

	public void setDmaName(String dmaName) {
		this.dmaName = dmaName;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}
}
