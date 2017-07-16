package com.casic.simulation.device.domain;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ALARM_REGION")
@SequenceGenerator(name = "SEQ_REGION_ID", sequenceName = "SEQ_REGION_ID", allocationSize=1,initialValue=1)
public class Region implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4626014545870298918L;
    private Long id;
    private Region parent;
    private Set<Region> children = new HashSet<Region>();
    private String regionName;
    private String regionArea;
    private Set<Device> devices = new HashSet<Device>();
    private Boolean active = true;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REGION_ID")
    @Column(name = "DBID")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_REGION_ID")
    @DefaultValue(value="-1")
    public Region getParent() {
        return parent;
    }
    public void setParent(Region parent) {
        this.parent = parent;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<Region> getChildren() {
        return children;
    }
    public void setChildren(Set<Region> children) {
        this.children = children;
    }

    @Column(name = "REGION_NAME")
    public String getRegionName() {
        return regionName;
    }
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Column(name = "REGION_AREA")
    public String getRegionArea() {
        return regionArea;
    }
    public void setRegionArea(String regionArea) {
        this.regionArea = regionArea;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
    @OrderBy("id desc")
    public Set<Device> getDevices() {
        return devices;
    }
    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    @Column(name = "ACTIVE")
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}
