package com.casic.simulation.dma.model.json;

public class WaterPipelineRegionTreeJSON {

    private Long id;
    private String deviceCode;
    private String elementName;
    private Long _parentId;
    private String iconCls;
    private Boolean checked = true;
    private Boolean isRegion;
    private Boolean isPosition;
    private Long regionID;
    private Long positionID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Long get_parentId() {
        return _parentId;
    }

    public void set_parentId(Long _parentId) {
        this._parentId = _parentId;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Boolean getIsRegion() {
        return isRegion;
    }

    public void setIsRegion(Boolean isRegion) {
        this.isRegion = isRegion;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getIsPosition() {
        return isPosition;
    }

    public void setIsPosition(Boolean isPosition) {
        this.isPosition = isPosition;
    }

    public Long getRegionID() {
        return regionID;
    }

    public void setRegionID(Long regionID) {
        this.regionID = regionID;
    }

    public Long getPositionID() {
        return positionID;
    }

    public void setPositionID(Long positionID) {
        this.positionID = positionID;
    }

    public static final WaterPipelineRegionTreeJSON root = new WaterPipelineRegionTreeJSON();;

    static {
        root.setId(-1l);
        root.setElementName("全部");
        root.setIconCls("icon-folder");
        root.setIsRegion(true);
        root.setIsPosition(false);
        root.setRegionID(-1l);
    }
}
