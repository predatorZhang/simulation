package com.casic.simulation.health.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "YJ_WARNING_HEALTH")
@SequenceGenerator(name = "SEQ_YJ_WARNING_HEALTH", sequenceName = "SEQ_YJ_WARNING_HEALTH",allocationSize=1,initialValue=1)
public class HealthInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long dbId;
	private String pipeId;
	private String pipeType;
    private String street;
    private String healthRank;
    private Date evalTime;
    private String evalRecordId;
    private String result;
    private String suggestion;
    private String memo;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_YJ_WARNING_HEALTH")
    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    @Column(name = "PIPEID")
    public String getPipeId() {
        return pipeId;
    }
    public void setPipeId(String pipeId) {
        this.pipeId = pipeId;
    }

    @Column(name = "PIPETYPE")
    public String getPipeType() {
        return pipeType;
    }
    public void setPipeType(String pipeType) {
        this.pipeType = pipeType;
    }

    @Column(name = "STREET")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Column(name = "RANK")
    public String getHealthRank() {
        return healthRank;
    }

    public void setHealthRank(String healthRank) {
        this.healthRank = healthRank;
    }


    @Column(name = "EVALTIME")
    public Date getEvalTime() {
        return evalTime;
    }

    public void setEvalTime(Date evalTime) {
        this.evalTime = evalTime;
    }

    @Column(name = "EVALRECORDID")
    public String getEvalRecordId() {
        return evalRecordId;
    }

    public void setEvalRecordId(String evalRecordId) {
        this.evalRecordId = evalRecordId;
    }

    @Column(name = "RESULT")
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    @Column(name = "SUGGESTION")
    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    @Column(name = "MEMO")
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
}
