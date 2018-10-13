package com.arley.cms.console.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.arley.cms.console.shiro.PasswordMatcher;
import com.arley.cms.console.shiro.ShiroAuthRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/15 17:14
 */
@Configuration
public class ShiroConfiguration {

    @Bean
    public ShiroFilterFactoryBean shirFilter() {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
        shiroFilterFactoryBean.setLoginUrl("/user/login.do");
        // 设置无权限时跳转的 url;
        shiroFilterFactoryBean.setUnauthorizedUrl("/test");

        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/user/logout", "logout");
        // 静态资源放行
        filterChainDefinitionMap.put("/static/**", "anon");
        //开放登陆接口
        filterChainDefinitionMap.put("/user/login.do", "anon");
        //其余接口一律拦截
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        System.out.println("Shiro拦截器工厂类注入成功");

        return shiroFilterFactoryBean;
    }

    /**
     * Shiro生命周期处理器
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    /**
     * 缓存管理器 使用Ehcache实现
     * @return
     */
    @Bean(name="shiroEhcacheManager")
    public EhCacheManager shiroEhcacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");
        return ehCacheManager;
    }

    /**
     * 核心安全事务管理器
     * @return
     */
    @Bean(name="securityManager")
    public SecurityManager securityManager() {
        System.err.println("--------------shiro已经加载----------------");
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(authRealm());
        manager.setCacheManager(shiroEhcacheManager());
        manager.setSessionManager(sessionManager());
        return manager;
    }

    /**
     * 自定义的权限登录器
     * @return
     */
    @Bean(name="authRealm")
    public ShiroAuthRealm authRealm() {
        ShiroAuthRealm authRealm=new ShiroAuthRealm();
        authRealm.setCredentialsMatcher(credentialsMatcher());
        authRealm.setCacheManager(shiroEhcacheManager());
        // 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        authRealm.setAuthenticationCachingEnabled(false);
        // 缓存AuthenticationInfo信息的缓存名称
        authRealm.setAuthenticationCacheName("authenticationCache");
        // 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        authRealm.setAuthorizationCachingEnabled(false);
        // 缓存authorizationCache信息的缓存名称
        authRealm.setAuthorizationCacheName("authorizationCache");
        return authRealm;
    }

    /**
     * 自定义的密码比较器
     * @return
     */
    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new PasswordMatcher();
    }

    /**
     * 会话管理器
     * @return
     */
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDAO());

        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        // 定时检查失效的session
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(1800000);
        sessionManager.setSessionIdCookie(sessionIdCookie());

        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    /**
     * 会话DAO 用于会话的CRUD
     * @return
     */
    @Bean(name = "sessionDAO")
    public SessionDAO sessionDAO() {
        return new MemorySessionDAO();
    }

    /**
     * 会话Cookie模板
     * @return
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("SHRIOSESSIONID");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}