package com.casic.simulation.dma.model.dto;

/**
 * Created by Administrator on 2015/8/31.
 */
public class PointDTO {
    private Double x;
    private Double y;

    public PointDTO(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public PointDTO(){}

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
