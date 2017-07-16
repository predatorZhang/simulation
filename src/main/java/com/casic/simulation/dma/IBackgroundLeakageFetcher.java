package com.casic.simulation.dma;


import com.casic.simulation.dma.model.domain.DMAInfo;

import java.util.Date;

/**
 * Created by admin on 2017/2/15.
 */
public interface IBackgroundLeakageFetcher {
    BackgroundLeakageSum getBackgroundLeakage(DMAInfo dmaInfo, MinimumFlowSum flowSum, Date start, Date end);
}