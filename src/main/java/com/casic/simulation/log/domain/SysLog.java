package com.casic.simulation.log.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SM_SYS_LOG")
@SequenceGenerator(name = "SEQ_SM_SYS_LOG_ID", sequenceName = "SEQ_SM_SYS_LOG_ID", allocationSize=1,initialValue=1)
public class SysLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4968376643472961299L;
	private Long id;
	private String businessName;
	private String operationType;
	private String content;
	private String createUser;
	private Date createTime = new Date();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SM_SYS_LOG_ID")
	@Column(name = "DBID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="BussnessName")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Column(name="OperationType")
	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	@Column(name="Content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name="CreateUser")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name="CreateTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
