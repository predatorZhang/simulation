package com.casic.simulation.dma.analysis.json;

import java.io.Serializable;
import java.util.Date;

public class leakageEva implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5697315170187178995L;
	public String BData_DMA;// 分区编号
	public Date ReportDate;// 评估日期
	public Double AllowedMinWater;// 允许最小流量
	public Double MinInstantWater;// 最小流量
	public Date MinInstantWaterTime;//监测最小流量时间
	public Double LeakWater;//日漏失水量
	public Double SupplyWater;//日供水量
	public Double LeakRate;//日漏失率
	public Double LeakControlRate;//阶段漏损控制目标
    public Double PipeLength;//管道总长度
    public Double LeakWaterPerPipeLeng;//单位管长漏失水量
    public Double MaxInstantWater;//监测最大流量（瞬时）
    public Date MaxInstantWaterTime;//监测最大流量时间
    public Double AvgInstantWater;//日均流量（瞬时）
}
