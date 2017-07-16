package com.casic.simulation.dma.model.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DMA_REGIONERAE")
@SequenceGenerator(name = "SEQ_DMA_REGIONERAE_ID", sequenceName = "SEQ_DMA_REGIONERAE_ID", allocationSize = 1, initialValue = 1)
public class DMAArea implements Serializable {

    private Long id; // ID
    private DMAInfo dmaInfo;
    private String regionErea; // 分区区域
    private Boolean active = true;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DMA_REGIONERAE_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ACTIVE")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "REGIONERAE")
    public String getRegionErea() {
        return regionErea;
    }

    public void setRegionErea(String regionErea) {
        this.regionErea = regionErea;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DMA_ID")
    public DMAInfo getDmaInfo() {
        return dmaInfo;
    }

    public void setDmaInfo(DMAInfo dmaInfo) {
        this.dmaInfo = dmaInfo;
    }
}
