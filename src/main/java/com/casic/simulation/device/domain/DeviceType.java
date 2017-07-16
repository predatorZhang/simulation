package com.casic.simulation.device.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ALARM_DEVICE_TYPE")
@SequenceGenerator(name = "SEQ_DEVICE_TYPE_ID", sequenceName = "SEQ_DEVICE_TYPE_ID", allocationSize=1,initialValue=1)
public class DeviceType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2237554842360915685L;
	private Long id;
	private String typeCode;
	private String typeName;
	private String location;
    private String pipeName;
	private Set<Device> devices = new HashSet<Device>();
	private Boolean active = true;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEVICE_TYPE_ID")
	@Column(name = "DBID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TYPECODE", nullable = false)
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	@Column(name = "TYPENAME", nullable = false)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name="LOCATION")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

    @Column(name="PIPENAME")
    public String getPipeName() {
        return pipeName;
    }

    public void setPipeName(String pipeName) {
        this.pipeName = pipeName;
    }

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deviceType")
	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}

	@Column(name = "ACTIVE", nullable = false)
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
