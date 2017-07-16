package com.casic.simulation.dma;

/**
 * Created by admin on 2017/2/15.
 */
public class DMAResult {

    /*
    TODO LIST:根据不同的code来标识计算不同的出错类型
     */
    private int code;
    private double leakageRate;
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getLeakageRate() {
        return leakageRate;
    }

    public void setLeakageRate(double leakageRate) {
        this.leakageRate = leakageRate;
    }
}
