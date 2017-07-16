package com.casic.simulation.flow.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.flow.domain.FlowDepartment;
import com.casic.simulation.flow.domain.FlowPerson;
import com.casic.simulation.flow.domain.UserInfo;
import com.casic.simulation.flow.dto.UserInfoDTO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/24.
 */
@Service
public class FlowPersonManager extends HibernateEntityDao<FlowPerson> {

    @Resource
    private FlowDepartmentManager flowDepartmentManager;

    @Resource
    private NodeLimitManager nodeLimitManager;

    /**
     * 根据ID获取有效FlowPerson
     * @param ID
     * @return
     */
    public FlowPerson getActiveByID(Long ID) {
        Criteria criteria = createCriteria(FlowPerson.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("id", ID));
        return (FlowPerson)criteria.uniqueResult();
    }

    /**
     * 获取系统有效人员
     * @return
     */
    public List<UserInfoDTO> queryForSysPerson(){
        Criteria criteria = createCriteria(UserInfo.class);
        criteria.add(Restrictions.eq("status", 1));
        criteria.addOrder(Order.asc("department"));
        return UserInfoDTO.ConvertToDTO(sortByStatus(
                criteria.list()
        ));
    }

    /**
     * 将系统人员按照部门进行排序
     * @param users
     * @return
     */
    private List<UserInfo> sortByStatus(List<UserInfo> users) {
        List<UserInfo> useful = new ArrayList<UserInfo>();
        List<UserInfo> useless = new ArrayList<UserInfo>();
        for (UserInfo userInfo : users) {
            if (userInfo == null ||
                    userInfo.getDepartment() == null ||
                    userInfo.getDepartment().getStatus() == 0) {
                useless.add(userInfo);
            } else {
                useful.add(userInfo);
            }
        }
        useful.addAll(useless);
        return useful;
    }

    /**
     * 根据ID获取唯一全局用户信息
     * @param userID
     * @return
     */
    public UserInfoDTO queryForSysPersonByID(Long userID) {
        Criteria criteria = createCriteria(UserInfo.class);
        criteria.add(Restrictions.eq("id", userID));
        criteria.add(Restrictions.eq("status", 1));
        return UserInfoDTO.ConverToDTO((UserInfo)criteria.uniqueResult());
    }

    /**
     * 根据部门ID获取所有直接员工列表
     * @param depID
     * @return
     */
    public List<FlowPerson> queryPersonsByDepID(Long depID) {
        Criteria criteria = createCriteria(FlowPerson.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("flowDepartment",
                flowDepartmentManager.getActiveByID(depID)));
        return criteria.list();
    }

    /**
     * 查询在同一部门下是否存在相同人员
     * @param depID
     * @param sysPersonID
     * @return
     */
    public boolean exist(Long depID, Long sysPersonID) {
        Criteria criteria = createCriteria(FlowPerson.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("sysPersonID", sysPersonID));
        criteria.add(Restrictions.eq("flowDepartment",
                flowDepartmentManager.getActiveByID(depID)));
        return getCount(criteria) > 0;
    }

    /**
     * 增加新的用户
     * @param sysPersonID
     * @param department
     * @return
     */
    public ExecInfo add(Long sysPersonID, FlowDepartment department) {
        try {
            UserInfoDTO user = queryForSysPersonByID(sysPersonID);
            if (user == null || department == null) {
                return ExecInfo.fail("系统人员或关联部门无效");
            }
            FlowPerson person = new FlowPerson();
            person.setActive(true);
            person.setFlowDepartment(department);
            person.setSysPersonID(sysPersonID);
            person.setUserName(user.getUsername());
            save(person);
            return ExecInfo.succ("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     * 根据ID删除处置人员信息
     * @param id
     * @return
     */
    public ExecInfo del(Long id) {
        try {
            FlowPerson person = getActiveByID(id);
            person.setActive(false);
            nodeLimitManager.delByFlowPerson(person.getId());
            save(person);
            return ExecInfo.succ("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     * 根据处置部门ID删除处置部门
     * @param depid
     * @return
     */
    @Transactional
    public void delDep(Long depid) {
        delDepAndPerson(depid);
    }

    private void delDepAndPerson(Long depid) {
        FlowDepartment department =
                flowDepartmentManager.getActiveByID(depid);
        List<FlowDepartment> children =
                flowDepartmentManager.queryForFlowDepByParentID(depid);
        for (FlowDepartment temp : children) {
            delDepAndPerson(temp.getId());
        }
        List<FlowPerson> persons = queryPersonsByDepID(depid);
        for (FlowPerson person : persons) {
            person.setActive(false);
            nodeLimitManager.delByFlowPerson(person.getId());
            save(person);
        }
        department.setActive(false);
        flowDepartmentManager.save(department);
    }
}
