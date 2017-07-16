package com.casic.simulation.device.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "ALARM_ACCEPT_PERSON")
@SequenceGenerator(name = "SEQ_PERSON_ID", sequenceName = "SEQ_PERSON_ID", allocationSize=1,initialValue=1)
public class AcceptPerson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2442455395413792947L;
	private Long id;
	private String personCode;
	private String personName;
	private String password = "";//字段含义：默认短信发送人
	private Boolean active = true;
	private String telePhone;
	private String email;
	private Set<Device> devices;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERSON_ID")
	@Column(name = "DBID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PERSONCODE", unique = true, nullable = false)
	public String getPersonCode() {
		return personCode;
	}

	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}

	@Column(name = "PERSONNAME", nullable = false)
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getTelePhone() {
		return telePhone;
	}

	@Column(name = "TELEPHONE")
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getEmail() {
		return email;
	}

	@Column(name = "EMAIL")
	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acceptPerson")
	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}

}
