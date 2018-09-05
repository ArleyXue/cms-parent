package com.arley.cms.console.service;

import com.arley.cms.console.pojo.Do.SysRoleDO;
import com.arley.cms.console.pojo.query.SysRoleQuery;
import com.arley.cms.console.pojo.vo.SysRoleVO;
import com.arley.cms.console.util.Pagination;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/16 11:15
 */
public interface SysRoleService extends IService<SysRoleDO> {

    /**
     * 保存角色
     * @param role
     * @param permissionIds
     */
    void insertRole(SysRoleVO role, String permissionIds);

    /**
     * 分页查询角色
     * @param roleQuery
     * @return
     */
    Pagination listRoleByPay(SysRoleQuery roleQuery);

    /**
     * 获取角色
     * @param roleId
     * @return
     */
    SysRoleVO getRoleById(Integer roleId);

    /**
     * 修改角色
     * @param sysRoleVO
     * @param permissionIds
     */
    void updateRole(SysRoleVO sysRoleVO, String permissionIds);

    /**
     * 删除橘色
     * @param roleId
     */
    void deleteRoleById(Integer roleId);

    /**
     * 获取所有角色列表
     * @return
     */
    List<SysRoleVO> listRole();
}
