package com.casic.simulation.dma.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by yxw on 2017/3/14.
 */

@Entity
@Table(name = "DmaMinimumFlow")
@SequenceGenerator(name = "SEQ_DMAMINIMUMFLOW_ID", sequenceName = "SEQ_DMAMINIMUMFLOW_ID", allocationSize=1,initialValue=1)
public class DmaMinimumFlow implements Serializable {

    private static final long serialVersionUID = 4968376663472961299L;
    private Long dbId;
    private Long dmaInfo;
    private Date minFlowTime;
    private String minFlowValue;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DMAMINIMUMFLOW_ID")
    @Column(name = "DBID")
    public Long getDbId() {
        return dbId;
    }
    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    @Column(name="DMAINFO")
    public Long getDmaInfo() {
        return dmaInfo;
    }

    public void setDmaInfo(Long dmaInfo) {
        this.dmaInfo = dmaInfo;
    }

    @Column(name="MINFLOWTIME")
    public Date getMinFlowTime() {
        return minFlowTime;
    }
    public void setMinFlowTime(Date minFlowTime) {
        this.minFlowTime = minFlowTime;
    }

    @Column(name="MINFLOWVALUE")
    public String getMinFlowValue() {
        return minFlowValue;
    }
    public void setMinFlowValue(String minFlowValue) {
        this.minFlowValue = minFlowValue;
    }
}
