package com.casic.simulation.dma.model.domain;

import com.casic.simulation.dma.model.json.WaterPipelineRegionTreeJSON;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DMAINFO")
@SequenceGenerator(name = "SEQ_DMAINFO_ID", sequenceName = "SEQ_DMAINFO_ID", allocationSize=1,initialValue=1)
public class DMAInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9072886829080460101L;

	private Long ID; // ID
	private String Name; // 分区名称
	private String NO; // 分区编码
	private String BDataParent_DMA = "-1";// 上级分区
	private Double UserCount = 0.0; // 用户数量
	private Double NormalWater = 0.0; // 正常夜间用水量
	private Double PipeLeng = 0.0; // 管道总长度
	private Double UserPipeLeng = 0.0; // 户表后管道总长度
	private Double PipeLinks = 0.0; // 管道连接总数
	private Double Icf = 0.0; // ICF参数
	private Double LeakControlRate = 0.0; // 阶段漏损控制目标
	private Double SaleWater = 0.0; // 日售水量
	private Boolean active = true;
	private List<PosDMA> posInDmaList = new ArrayList<PosDMA>();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DMAINFO_ID")
	@Column(name = "DBID")
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	@Column(name = "NO")
	public String getNO() {
		return NO;
	}
	public void setNO(String no) {
		NO = no;
	}
	
	@Column(name = "BDATAPARENT_DMA")
	public String getBDataParent_DMA() {
		return BDataParent_DMA;
	}
	public void setBDataParent_DMA(String bDataParent_DMA) {
		BDataParent_DMA = bDataParent_DMA;
	}
	
	@Column(name = "USERCOUNT")
	public Double getUserCount() {
		return UserCount;
	}
	public void setUserCount(Double userCount) {
		UserCount = userCount;
	}
	
	@Column(name = "NORMALWATER")
	public Double getNormalWater() {
		return NormalWater;
	}
	public void setNormalWater(Double normalWater) {
		NormalWater = normalWater;
	}
	
	@Column(name = "PIPELENG")
	public Double getPipeLeng() {
		return PipeLeng;
	}
	public void setPipeLeng(Double pipeLeng) {
		PipeLeng = pipeLeng;
	}
	
	@Column(name = "USERPIPELENG")
	public Double getUserPipeLeng() {
		return UserPipeLeng;
	}
	public void setUserPipeLeng(Double userPipeLeng) {
		UserPipeLeng = userPipeLeng;
	}
	
	@Column(name = "PIPELINKS")
	public Double getPipeLinks() {
		return PipeLinks;
	}
	public void setPipeLinks(Double pipeLinks) {
		PipeLinks = pipeLinks;
	}
	
	@Column(name = "ICF")
	public Double getIcf() {
		return Icf;
	}
	public void setIcf(Double icf) {
		Icf = icf;
	}
	
	@Column(name = "LEAKCONTROLRATE")
	public Double getLeakControlRate() {
		return LeakControlRate;
	}
	public void setLeakControlRate(Double leakControlRate) {
		LeakControlRate = leakControlRate;
	}
	
	@Column(name = "SALEWATER")
	public Double getSaleWater() {
		return SaleWater;
	}
	public void setSaleWater(Double saleWater) {
		SaleWater = saleWater;
	}
	
	@OneToMany(cascade= CascadeType.ALL, mappedBy="dmaInfo", targetEntity=PosDMA.class)
	public List<PosDMA> getPosInDmaList() {
		return posInDmaList;
	}
	public void setPosInDmaList(List<PosDMA> posInDmaList) {
		this.posInDmaList = posInDmaList;
	}
	
	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

    public static WaterPipelineRegionTreeJSON convertToJSON(DMAInfo dma) {
        WaterPipelineRegionTreeJSON node =
                new WaterPipelineRegionTreeJSON();
        node.setId(dma.getID());
        node.setElementName(dma.getName());
        node.setIconCls("icon-folder");
        if (dma.getBDataParent_DMA() != null) {
            node.set_parentId(Long.parseLong(dma.getBDataParent_DMA()));
        }
        node.setIsRegion(true);
        node.setIsPosition(false);
        node.setRegionID(dma.getID());
        return node;
    }
}
