package com.casic.simulation.device.domain;

// default package

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AlarmDeviceSensorId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class DeviceSensorId implements java.io.Serializable {

	// Fields

	private String sensorcode;
	private String sensorid;
	private Long deviceid;

	// Constructors

	/** default constructor */
	public DeviceSensorId() {
	}

	/** full constructor */
	public DeviceSensorId(String sensorcode, String sensorid,
                          Long deviceid) {
		this.sensorcode = sensorcode;
		this.sensorid = sensorid;
		this.deviceid = deviceid;
	}

	// Property accessors

	@Column(name = "SENSORCODE", nullable = false, length = 4)
	public String getSensorcode() {
		return this.sensorcode;
	}

	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
	}

	@Column(name = "SENSORID", nullable = false)
	public String getSensorid() {
		return this.sensorid;
	}

	public void setSensorid(String sensorid) {
		this.sensorid = sensorid;
	}

	@Column(name = "DEVICEID", nullable = false, scale = 0)
	public Long getDeviceid() {
		return this.deviceid;
	}

	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DeviceSensorId))
			return false;
		DeviceSensorId castOther = (DeviceSensorId) other;

		return ((this.getSensorcode() == castOther.getSensorcode()) || (this
				.getSensorcode() != null && castOther.getSensorcode() != null && this
				.getSensorcode().equals(castOther.getSensorcode())))
				&& ((this.getSensorid() == castOther.getSensorid()) || (this
						.getSensorid() != null
						&& castOther.getSensorid() != null && this
						.getSensorid().equals(castOther.getSensorid())))
				&& ((this.getDeviceid() == castOther.getDeviceid()) || (this
						.getDeviceid() != null
						&& castOther.getDeviceid() != null && this
						.getDeviceid().equals(castOther.getDeviceid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getSensorcode() == null ? 0 : this.getSensorcode()
						.hashCode());
		result = 37 * result
				+ (getSensorid() == null ? 0 : this.getSensorid().hashCode());
		result = 37 * result
				+ (getDeviceid() == null ? 0 : this.getDeviceid().hashCode());
		return result;
	}

}