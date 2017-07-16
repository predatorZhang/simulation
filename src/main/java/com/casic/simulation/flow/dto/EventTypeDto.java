package com.casic.simulation.flow.dto;

/**
 * Created by lenovo on 2017/5/2.
 */
public class EventTypeDto {

    private Integer index;
    private String typeName;
    private Boolean selected;

    public EventTypeDto(){}

    public EventTypeDto(Integer index, String typeName) {
        this(index, typeName, false);
    }

    public EventTypeDto(Integer index, String typeName, boolean selected){
        this.index = index;
        this.typeName = typeName;
        this.selected = selected;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
