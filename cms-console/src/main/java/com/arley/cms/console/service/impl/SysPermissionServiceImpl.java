package com.arley.cms.console.service.impl;

import com.arley.cms.console.mapper.SysPermissionMapper;
import com.arley.cms.console.pojo.Do.SysPermissionDO;
import com.arley.cms.console.service.SysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/16 11:15
 */
@Service("sysPermissionService")
@Transactional(rollbackFor = Exception.class)
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermissionDO> implements SysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermissionDO> listHavePermission(Integer userId) {
        return sysPermissionMapper.listHavePermission(userId);
    }

    @Override
    public List<SysPermissionDO> listHaveHierarchyPermission(Integer userId) {
        return sysPermissionMapper.listHaveHierarchyPermission(userId);
    }

    @Override
    public List<SysPermissionDO> listPermissionByRoleId(Integer roleId) {
        return sysPermissionMapper.listPermissionByRoleId(roleId);
    }
}
