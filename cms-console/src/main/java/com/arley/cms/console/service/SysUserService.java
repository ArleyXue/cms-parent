package com.arley.cms.console.service;

import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.pojo.query.SysUserQuery;
import com.arley.cms.console.util.Pagination;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/16 11:15
 */
public interface SysUserService extends IService<SysUserDO> {

    /**
     * 分页查询系统用户
     * @param sysUserQuery
     * @return
     */
    Pagination listSysUserByPage(SysUserQuery sysUserQuery);
}
