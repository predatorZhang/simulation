package com.casic.simulation.dma.model.dto;

public class WaterPipelineRegionForm {
    private Long regionID;
    private Long positionID;
    private String Name; // 分区名称
    private String No; // 分区编码
    private String BDataParent_DMA = "-1";// 上级分区
    private Double UserCount = 0.0; // 用户数量
    private Double NormalWater = 0.0; // 正常夜间用水量
    private Double PipeLeng = 0.0; // 管道总长度
    private Double UserPipeLeng = 0.0; // 户表后管道总长度
    private Double PipeLinks = 0.0; // 管道连接总数
    private Double Icf = 0.0; // ICF参数
    private Double LeakControlRate = 0.0; // 阶段漏损控制目标
    private Double SaleWater = 0.0; // 日售水量
    private int page;
    private int rows;
    private String region;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getBDataParent_DMA() {
        return BDataParent_DMA;
    }

    public void setBDataParent_DMA(String bDataParent_DMA) {
        BDataParent_DMA = bDataParent_DMA;
    }

    public Double getUserCount() {
        return UserCount;
    }

    public void setUserCount(Double userCount) {
        UserCount = userCount;
    }

    public Double getNormalWater() {
        return NormalWater;
    }

    public void setNormalWater(Double normalWater) {
        NormalWater = normalWater;
    }

    public Double getPipeLeng() {
        return PipeLeng;
    }

    public void setPipeLeng(Double pipeLeng) {
        PipeLeng = pipeLeng;
    }

    public Double getUserPipeLeng() {
        return UserPipeLeng;
    }

    public void setUserPipeLeng(Double userPipeLeng) {
        UserPipeLeng = userPipeLeng;
    }

    public Double getPipeLinks() {
        return PipeLinks;
    }

    public void setPipeLinks(Double pipeLinks) {
        PipeLinks = pipeLinks;
    }

    public Double getIcf() {
        return Icf;
    }

    public void setIcf(Double icf) {
        Icf = icf;
    }

    public Double getLeakControlRate() {
        return LeakControlRate;
    }

    public void setLeakControlRate(Double leakControlRate) {
        LeakControlRate = leakControlRate;
    }

    public Double getSaleWater() {
        return SaleWater;
    }

    public void setSaleWater(Double saleWater) {
        SaleWater = saleWater;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public WaterPipelineRegionForm() {
        this.page = this.page <= 0 ? 1 : this.page;
        this.rows = this.rows <= 0 ? 10 : this.rows;
    }
}
