package com.casic.simulation.dma.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ALARM_DMA_LOG")
@SequenceGenerator(name = "SEQ_DMA_LOG_ID", sequenceName = "SEQ_DMA_LOG_ID", allocationSize=1,initialValue=1)
public class DmaLog implements Serializable {
	private static final long serialVersionUID = 4968376663472961299L;
	private Long dbId;
	private Long regionId;
	private boolean success;
	private String message;
	private Date createTime = new Date();

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DMA_LOG_ID")
	@Column(name = "DBID")
    public Long getDbId() {
        return dbId;
    }
    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    @Column(name="regionId")
    public Long getRegionId() {
        return regionId;
    }
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Column(name="success")
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Column(name="message")
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

	@Column(name="CreateTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
