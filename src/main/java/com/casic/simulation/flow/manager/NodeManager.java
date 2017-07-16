package com.casic.simulation.flow.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.ExecResult;
import com.casic.simulation.flow.domain.Node;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2017/4/24.
 */
@Service
public class NodeManager extends HibernateEntityDao<Node> {

    /**
     * 根据message status获取node，该方法不会报错，可依赖性很高
     * @param status
     * @return
     */
    public ExecResult<Node> getNodeByStatus(Integer status) {
        if (status == null) {
            return ExecResult.fail("message status is null.");
        }
        try {
            Criteria criteria = createCriteria(Node.class);
            criteria.add(Restrictions.eq("messageStatus", status));
            Node node = (Node) criteria.setMaxResults(1).uniqueResult();
            if (node == null) {
                return ExecResult.fail("can not find the node which message status is " + status);
            } else {
                return ExecResult.succ(node);
            }
        } catch (Exception e) {
            return ExecResult.fail(e.getMessage());
        }
    }
}
