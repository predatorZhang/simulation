package com.casic.simulation.dma.analysis.manager;

import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.DateUtils;
import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.dto.PointDTO;
import com.casic.simulation.dma.analysis.json.LeakageEvaJSON;
import com.casic.simulation.dma.model.manager.DMAAreaManager;
import com.casic.simulation.dma.model.manager.DMAInfoManager;

import javax.annotation.Resource;
import java.util.*;

public class MockWaterPipeLineAnalysisManager {

    @Resource
    private DMAInfoManager dmaInfoManager;

    @Resource
    private DMAAreaManager dmaAreaManager;

    public Page getCurrentLeakge(int page, int rows) {

        String hql = "from DMAInfo d where active=1";
        Map<String, Object> map = new HashMap<String, Object>();
        Page p = dmaInfoManager.pagedQuery(hql.toString(), page, rows, map);
        List<DMAInfo> dmaInfos = (List<DMAInfo>)p.getResult();

        List<LeakageEvaJSON> list = new ArrayList<LeakageEvaJSON>();
        for (DMAInfo dmaInfo : dmaInfos) {
            LeakageEvaJSON leakageEvaJSON = new LeakageEvaJSON();
            leakageEvaJSON.dmaId = dmaInfo.getID().toString();
            leakageEvaJSON.dmaInfoName = dmaInfo.getName();
            leakageEvaJSON.setReportDate(DateUtils.sdf4.format((new Date())));
            List<PointDTO> pts = dmaAreaManager.findPointsOfArea(dmaInfo.getID());
            leakageEvaJSON.points = pts;
            list.add(leakageEvaJSON);
        }
        p.setResult(list);
        return p;
    }
}
