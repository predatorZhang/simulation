package com.casic.simulation.dma;


import com.casic.simulation.dma.model.domain.DMAInfo;

import java.util.Date;

/**
 * Created by admin on 2017/2/15.
 */
public interface INormalWaterFetcher {
    NormalWater getNormalWater(DMAInfo dmaInfo, Date start, Date end);
}