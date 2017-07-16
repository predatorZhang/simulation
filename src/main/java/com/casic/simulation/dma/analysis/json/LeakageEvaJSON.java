package com.casic.simulation.dma.analysis.json;

import com.casic.simulation.dma.model.dto.PointDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LeakageEvaJSON extends leakageEva {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5290729928552728811L;

    public String dmaId;
	public String dmaInfoName;
    public double LeakRate;
	public String ReportDate;// 评估日期
	public String MinInstantWaterTime;//监测最小流量时间
    public String MaxInstantWaterTime;//监测最大流量时间
    public String physiqueAnalysis = "该分区不用查漏，已达到终极目标，请维持原状";
    public List<PointDTO> points;

    private int code;
    private String errorMsg;

    private SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LeakageEvaJSON() {

    }

    public LeakageEvaJSON(leakageEva leakageEva, String dmaInfoName, String physiqueAnalysis, String dmaId) {
        this.dmaId = dmaId;
    	this.dmaInfoName = dmaInfoName;
        this.BData_DMA = leakageEva.BData_DMA;// 分区编号
        try {
        	Date reportDate = leakageEva.ReportDate;
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(reportDate);
        	calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 8);
            this.ReportDate = sFormat.format(reportDate);// 评估日期
        } catch(Exception e) {
        	this.ReportDate = "";// 评估日期
        }
       
        this.AllowedMinWater = leakageEva.AllowedMinWater;// 夜间允许最小流量
        this.MinInstantWater = leakageEva.MinInstantWater;// 夜间允许最小流量
        try {
        	Date minInstantWaterTime = leakageEva.MinInstantWaterTime;
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(minInstantWaterTime);
        	calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 8);
            this.MinInstantWaterTime = sFormat.format(calendar.getTime());//监测最小流量时间
        } catch(Exception e) {
        	this.MinInstantWaterTime = "";
        }
        this.LeakWater = leakageEva.LeakWater;//日漏失水量
        this.SupplyWater = leakageEva.SupplyWater;//日供水量
        this.LeakRate = leakageEva.LeakRate;//日漏失率
        this.LeakControlRate = leakageEva.LeakControlRate;//阶段漏损控制目标
        this.PipeLength = leakageEva.PipeLength;//管道总长度
        this.LeakWaterPerPipeLeng = leakageEva.LeakWaterPerPipeLeng;//单位管长漏失水量
        this.MaxInstantWater = leakageEva.MaxInstantWater;//监测最大流量（瞬时）
        try {
        	Date maxInstantWaterTime = leakageEva.MaxInstantWaterTime;
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(maxInstantWaterTime);
        	calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 8);
            this.MaxInstantWaterTime = sFormat.format(calendar.getTime());//监测最大流量时间
        } catch(Exception e) {
        	this.MaxInstantWaterTime = "";
        }
        this.AvgInstantWater = leakageEva.AvgInstantWater;//日均流量（瞬时）
        this.physiqueAnalysis = physiqueAnalysis;
    }

    public String getDmaId() {
        return dmaId;
    }

    public void setDmaId(String dmaId) {
        this.dmaId = dmaId;
    }

    public double getLeakRate() {
        return LeakRate;
    }

    public void setLeakRate(double leakRate) {
        LeakRate = leakRate;
    }

    public String getReportDate() {
		return ReportDate;
	}

	public void setReportDate(String reportDate) {
		ReportDate = reportDate;
	}

	public String getMinInstantWaterTime() {
		return MinInstantWaterTime;
	}

	public void setMinInstantWaterTime(String minInstantWaterTime) {
		MinInstantWaterTime = minInstantWaterTime;
	}

	public String getMaxInstantWaterTime() {
		return MaxInstantWaterTime;
	}

	public void setMaxInstantWaterTime(String maxInstantWaterTime) {
		MaxInstantWaterTime = maxInstantWaterTime;
	}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
