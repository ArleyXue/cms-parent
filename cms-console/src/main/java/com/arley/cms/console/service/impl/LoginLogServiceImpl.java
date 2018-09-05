package com.arley.cms.console.service.impl;

import com.arley.cms.console.mapper.LoginLogMapper;
import com.arley.cms.console.pojo.Do.LoginLogDO;
import com.arley.cms.console.service.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/16 11:15
 */
@Service("loginLogService")
@Transactional(rollbackFor = Exception.class)
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLogDO> implements LoginLogService {
}
