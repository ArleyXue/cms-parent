package com.arley.cms.console.service.impl;

import com.arley.cms.console.mapper.SysRoleMapper;
import com.arley.cms.console.pojo.Do.SysRoleDO;
import com.arley.cms.console.pojo.Do.SysRolePermissionDO;
import com.arley.cms.console.pojo.query.SysRoleQuery;
import com.arley.cms.console.pojo.vo.SysRoleVO;
import com.arley.cms.console.service.SysRoleService;
import com.arley.cms.console.util.DateUtils;
import com.arley.cms.console.util.Pagination;
import com.arley.cms.console.util.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/16 11:15
 */
@Service("sysRoleService")
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;


    @Override
    public void insertRole(SysRoleVO sysRoleVO, String permissionIds) {
        SysRoleDO sysRoleDO = new SysRoleDO();
        BeanUtils.copyProperties(sysRoleVO, sysRoleDO);
        sysRoleDO.setModifier(ShiroUtils.getLoginUser().getUserName());
        sysRoleDO.setGmtCreate(DateUtils.getLocalDateTime());
        sysRoleDO.setGmtModified(DateUtils.getLocalDateTime());
        sysRoleMapper.insert(sysRoleDO);

        // 保存新的权限
        List<SysRolePermissionDO> rolePermissionList = putSysRolePermission(sysRoleDO.getRoleId(), permissionIds);
        if (rolePermissionList.size() > 0) {
            sysRoleMapper.saveRolePermission(rolePermissionList);
        }
    }

    @Override
    public Pagination listRoleByPay(SysRoleQuery roleQuery) {
        LambdaQueryWrapper<SysRoleDO> qw = new QueryWrapper<SysRoleDO>().lambda().orderByAsc(SysRoleDO::getRoleId);
        if (null != roleQuery.getRoleId()) {
            qw.eq(SysRoleDO::getRoleId, roleQuery.getRoleId());
        }
        IPage<SysRoleDO> iPage = sysRoleMapper.selectPage(new Page<>(roleQuery.getPage(), roleQuery.getLimit()), qw);
        Pagination<SysRoleVO> pagination = new Pagination<>();
        pagination.setData(convertList(iPage.getRecords()));
        pagination.setCount(iPage.getTotal());
        return pagination;
    }

    @Override
    public SysRoleVO getRoleById(Integer roleId) {
        SysRoleDO sysRoleDO = sysRoleMapper.selectById(roleId);
        SysRoleVO sysRoleVO = new SysRoleVO();
        BeanUtils.copyProperties(sysRoleDO, sysRoleVO);
        return sysRoleVO;
    }

    @Override
    public void updateRole(SysRoleVO sysRoleVO, String permissionIds) {
        // 修改角色
        SysRoleDO sysRoleDO = sysRoleMapper.selectById(sysRoleVO.getRoleId());
        sysRoleDO.setRoleName(sysRoleVO.getRoleName());
        sysRoleDO.setRemark(sysRoleVO.getRemark());
        sysRoleDO.setRoleState(sysRoleVO.getRoleState());
        sysRoleDO.setGmtModified(DateUtils.getLocalDateTime());
        sysRoleMapper.updateById(sysRoleDO);

        // 删除角色权限
        sysRoleMapper.deleteRolePermissionByRoleId(sysRoleDO.getRoleId());

        // 赋予角色新权限
        List<SysRolePermissionDO> rolePermissionList = putSysRolePermission(sysRoleDO.getRoleId(), permissionIds);
        if (rolePermissionList.size() > 0) {
            sysRoleMapper.saveRolePermission(rolePermissionList);
        }
    }

    @Override
    public void deleteRoleById(Integer roleId) {
        sysRoleMapper.deleteById(roleId);

        // 角色权限
        sysRoleMapper.deleteRolePermissionByRoleId(roleId);
    }

    @Override
    public List<SysRoleVO> listRole() {
        List<SysRoleDO> sysRoleDOList = sysRoleMapper.selectList(new QueryWrapper<SysRoleDO>().lambda().orderByAsc(SysRoleDO::getRoleId));
        return convertList(sysRoleDOList);
    }

    private List<SysRolePermissionDO> putSysRolePermission(Integer roleId, String permissionIds) {
        List<SysRolePermissionDO> result = new ArrayList<>();
        if (StringUtils.isNotBlank(permissionIds)) {
            String[] menuIds = permissionIds.split(",");
            if (menuIds.length > 0) {
                for (String menuId : menuIds) {
                    SysRolePermissionDO rolePermission = new SysRolePermissionDO();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(Integer.parseInt(menuId));
                    result.add(rolePermission);
                }
            }
        }

        return result;
    }

    private List<SysRoleVO> convertList(List<SysRoleDO> sysRoleDOList) {
        List<SysRoleVO> sysRoleVOList = new ArrayList<>();
        if (sysRoleDOList != null && sysRoleDOList.size() > 0) {
            sysRoleDOList.forEach(e -> {
                SysRoleVO vo = new SysRoleVO();
                BeanUtils.copyProperties(e, vo);
                sysRoleVOList.add(vo);
            });
        }
        return sysRoleVOList;
    }
}
