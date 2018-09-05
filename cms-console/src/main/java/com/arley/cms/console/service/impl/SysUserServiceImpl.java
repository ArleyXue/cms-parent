package com.arley.cms.console.service.impl;

import com.arley.cms.console.mapper.SysUserMapper;
import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.pojo.query.SysUserQuery;
import com.arley.cms.console.pojo.vo.SysUserVO;
import com.arley.cms.console.service.SysUserService;
import com.arley.cms.console.util.Pagination;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
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
@Service("sysUserService")
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Pagination listSysUserByPage(SysUserQuery sysUserQuery) {
        LambdaQueryWrapper<SysUserDO> qw = new QueryWrapper<SysUserDO>().lambda().orderByAsc(SysUserDO::getUserId);
        IPage<SysUserDO> iPage = sysUserMapper.selectPage(new Page<>(sysUserQuery.getPage(), sysUserQuery.getLimit()), qw);
        Pagination<SysUserVO> pagination = new Pagination<>();
        pagination.setData(convertList(iPage.getRecords()));
        pagination.setCount(iPage.getTotal());
        return pagination;
    }


    private SysUserVO convertOne(SysUserDO sysUserDO) {
        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(sysUserDO, vo);
       return vo;
    }
    private List<SysUserVO> convertList(List<SysUserDO> sysUserDOList) {
        List<SysUserVO> sysUserVOList = new ArrayList<>();
        if (sysUserDOList != null && sysUserDOList.size() > 0) {
            sysUserDOList.forEach(e -> {
                SysUserVO vo = new SysUserVO();
                BeanUtils.copyProperties(e, vo);
                sysUserVOList.add(vo);
            });
        }
       return sysUserVOList;
    }

}
