package com.arley.cms.console;

import com.arley.cms.console.pojo.query.SysUserQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsConsoleApplicationTests {

    @Test
    public void contextLoads() {
        SysUserQuery a = new SysUserQuery();
        a.setPage(1);
        SysUserQuery b = new SysUserQuery();
        BeanUtils.copyProperties(b, a);

    }

}
