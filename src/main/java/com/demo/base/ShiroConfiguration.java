package com.demo.base;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfiguration {
  /**
   * ShiroFilterFactoryBean 处理拦截资源文件问题。
   * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
   * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
   *
   * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
   * 3、部分过滤器可指定参数，如perms，roles
   *
   */
  @Bean
  public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
      ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
      Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();//获取filters  
      filters.put("perms", new URLPermissionsFilter());
      filters.put("authc", new UserLoginFilter());

      // 必须设置 SecurityManager
      shiroFilterFactoryBean.setSecurityManager(securityManager);

      // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
      shiroFilterFactoryBean.setLoginUrl("/login");
      // 登录成功后要跳转的链接
      shiroFilterFactoryBean.setSuccessUrl("/index");
      // 未授权界面;
      shiroFilterFactoryBean.setUnauthorizedUrl("/403");

      // 拦截器.
      Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
      // 配置不会被拦截的链接 顺序判断
      filterChainDefinitionMap.put("/css/**", "anon");
      filterChainDefinitionMap.put("/js/**", "anon");
      filterChainDefinitionMap.put("/images/**", "anon");
      filterChainDefinitionMap.put("/ajaxLogin", "anon");

      // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
      filterChainDefinitionMap.put("/logout", "logout");
      
      /**数据库查询权限***/
      filterChainDefinitionMap.put("/user", "perms[权限添加]");

      // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
      // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
      filterChainDefinitionMap.put("/**", "authc");

      shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
      System.out.println("Shiro拦截器工厂类注入成功");
      return shiroFilterFactoryBean;
  }

  @Bean
  public DefaultWebSecurityManager securityManager() {
      DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
      // 设置realm.
      securityManager.setRealm(myShiroRealm());
      return securityManager;
  }

  /**
   * 身份认证realm;
   * 
   * @return
   */
  @Bean
  public MyShiroRealm myShiroRealm() {
      MyShiroRealm myShiroRealm = new MyShiroRealm();
      return myShiroRealm;
  }
  
  /**  
   *  开启shiro aop注解支持.  
   *  使用代理方式;所以需要开启代码支持;  
   * @param securityManager  
   * @return  
   */  
  @Bean
  public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
      AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
      aasa.setSecurityManager(securityManager);
      return aasa;
  }
  
  @Bean  
  public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {  
      DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();  
      daap.setProxyTargetClass(true);  
      return daap;  
  }  
  

  @Bean(name = "lifecycleBeanPostProcessor")  
  public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {  
      return new LifecycleBeanPostProcessor();  
  }  
  
}
