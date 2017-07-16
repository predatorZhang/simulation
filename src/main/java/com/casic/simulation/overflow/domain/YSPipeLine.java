package com.casic.simulation.overflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by lenovo on 2017/5/4.
 */
@Entity
@Table(name = "雨水管线")
public class YSPipeLine  implements Serializable {
    private static final long serialVersionUID = -2442455395413792947L;

    private String code;
    private String diameter;
    private String owner ;
    private String relateWell ;
    private String roadName;
    private String texture;
    private String startDepth;
    private String endDepth;

    @Column(name="材质")
    public String getTexture() {
        return texture;
    }
    public void setTexture(String texture) {
        this.texture = texture;
    }

    @Column(name="起始埋深")
    public String getStartDepth() {
        return startDepth;
    }
    public void setStartDepth(String startDepth) {
        this.startDepth = startDepth;
    }

    @Column(name="终止埋深")
    public String getEndDepth() {
        return endDepth;
    }

    public void setEndDepth(String endDepth) {
        this.endDepth = endDepth;
    }

    @Id
    @Column(name="编号")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    @Column(name="管径")
    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    @Column(name="权属单位")
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Column(name="关联管井")
    public String getRelateWell() {
        return relateWell;
    }

    public void setRelateWell(String relateWell) {
        this.relateWell = relateWell;
    }

    @Column(name="所属道路")
    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
}
