package com.casic.simulation.flow.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.flow.domain.Department;
import com.casic.simulation.flow.domain.FlowDepartment;
import com.casic.simulation.flow.dto.DepartmentDTO;
import com.casic.simulation.flow.dto.FlowDepartmentDto;
import com.casic.simulation.flow.web.FlowDepPersonController;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lenovo on 2017/4/24.
 */
@Service
public class FlowDepartmentManager
        extends HibernateEntityDao<FlowDepartment> {

    public static final Long ROOT_DEP_ID = 1l;

    /**
     * 根据ID获取有效FlowDepartment
     * @param ID
     * @return
     */
    public FlowDepartment getActiveByID(Long ID) {
        Criteria criteria = createCriteria(FlowDepartment.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("id", ID));
        return (FlowDepartment)criteria.uniqueResult();
    }

    /**
     * 获取系统有效部门，且该部门不能已经关联过
     * @return
     */
    public List<DepartmentDTO> queryForSysDep(){
        String hql = "from Department as a where a.status=1 and a.id not in (select b.sysDepID from FlowDepartment as b where b.active = 1 and b.id <> ?)";
        return DepartmentDTO.ConvertDTOs(
                find(hql, ROOT_DEP_ID)
        );
    }

    /**
     * 根据父ID获取所有有效处置部门
     * @param pID
     * @return
     */
    public List<FlowDepartment> queryForFlowDepByParentID(Long pID) {
        Criteria criteria = createCriteria(FlowDepartment.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("parent", get(pID)));
        return criteria.list();
    }

    /**
     * 查询是否存在关联系统部门多次的情况
     * @param sysDepID
     * @return
     */
    public boolean exist(Long sysDepID) {
        Criteria criteria = createCriteria(FlowDepartment.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("sysDepID", sysDepID));
        return getCount(criteria) > 0;
    }

    /**
     * 增加部门
     * @param sysDepID
     * @param parent
     * @param name
     * @return
     */
    public ExecInfo add(Long sysDepID,
                        FlowDepartment parent,
                        String name) {
        try {
            if (sysDepID != FlowDepPersonController.SYS_DEP_FOR_NULL) {
                Criteria criteria = createCriteria(Department.class);
                criteria.add(Restrictions.eq("status", 1));
                criteria.add(Restrictions.eq("id", sysDepID));
                Department ref = (Department) criteria.uniqueResult();
                if (ref == null || parent == null
                        || StringUtils.isBlank(name)) {
                    return ExecInfo.fail("关联部门已无效");
                }
            }
            FlowDepartment temp = new FlowDepartment();
            temp.setActive(true);
            temp.setDepName(name);
            temp.setParent(parent);
            temp.setSysDepID(sysDepID);
            save(temp);
            return ExecInfo.succ("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     * 查询除ROOT节点外的所有有效节点
     * @return
     */
    public List<FlowDepartmentDto> queryExceptRootForActive() {
        Criteria criteria = createCriteria(FlowDepartment.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.not(Restrictions.eq(
                "id", ROOT_DEP_ID)));
        criteria.addOrder(Order.asc("id"));
        return FlowDepartmentDto.ConvertDTOs(criteria.list());
    }
}
