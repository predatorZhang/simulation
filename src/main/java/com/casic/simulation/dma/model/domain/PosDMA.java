package com.casic.simulation.dma.model.domain;

import com.casic.simulation.dma.model.json.WaterPipelineRegionTreeJSON;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "POSDMA")
@SequenceGenerator(name = "SEQ_POSDMA_ID", sequenceName = "SEQ_POSDMA_ID", allocationSize=1,initialValue=1)
public class PosDMA implements Serializable,Comparable<PosDMA> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8784794374182869670L;
	private Long ID; //ID     
	private DMAInfo dmaInfo;            //分区ID
	private PositionInfo positionInfo;  //监测点ID
    private String Direction;  //流向     选择输入[{流入：1}，{流出：-1} ]
    private Boolean active;
    
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSDMA_ID")
	@Column(name = "DBID")
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}

	@ManyToOne(fetch= FetchType.EAGER, targetEntity=DMAInfo.class)
	public DMAInfo getDmaInfo() {
		return dmaInfo;
	}
	public void setDmaInfo(DMAInfo dmaInfo) {
		this.dmaInfo = dmaInfo;
	}
	
	@ManyToOne(fetch= FetchType.EAGER, targetEntity=PositionInfo.class)
	public PositionInfo getPositionInfo() {
		return positionInfo;
	}
	public void setPositionInfo(PositionInfo positionInfo) {
		this.positionInfo = positionInfo;
	}
	
	@Column(name="DIRECTION")
	public String getDirection() {
		return Direction;
	}
	public void setDirection(String direction) {
		Direction = direction;
	}
	
	@Column(name="ACTIVE")
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}


	@Override
	public int compareTo(PosDMA posDma) {
       return  this.positionInfo.getID()>posDma.getPositionInfo().getID()?1:-1;
	}

    public static WaterPipelineRegionTreeJSON convertToJSON(PosDMA posDma, DMAInfo parent) {
        if (!posDma.getPositionInfo().getActive()
                || null == posDma.getActive()
                || posDma.getActive() == Boolean.FALSE) {
            return null;
        }

        WaterPipelineRegionTreeJSON node = new WaterPipelineRegionTreeJSON();
        node.setId((long) (new Date().getTime() * Math.random())
                + parent.getID() + posDma.getPositionInfo().getID());
        node.setElementName(posDma.getPositionInfo().getName());
        node.setIconCls("icon-tip");
        node.set_parentId(parent.getID());
        node.setIsRegion(false);
        node.setIsPosition(true);
        node.setPositionID(posDma.getPositionInfo().getID());
        return node;
    }
}
