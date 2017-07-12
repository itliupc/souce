package com.demo.base;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ShiroConfiguration {
  
  /**
   * ShiroFilterFactoryBean 处理拦截资源文件问题。
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

      // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 
      // authc:所有url都必须认证通过才可以访问; 
      // anon:所有url都都可以匿名访问
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
      
      securityManager.setSessionManager(sessionManager());
      
      // 用户授权/认证信息Cache, 采用EhCache 缓存 
      securityManager.setCacheManager(ehCacheManager());
      // 注入记住我管理器;  
      securityManager.setRememberMeManager(rememberMeManager()); 
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
      //告诉realm,使用credentialsMatcher加密算法类来验证密文
      //myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
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
  
  // Shiro生命周期处理器
  @Bean(name = "lifecycleBeanPostProcessor")  
  public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {  
      return new LifecycleBeanPostProcessor();  
  }  
  
  /**
   * hashedCredentialsMatcher，这个类是为了对密码进行编码的，防止密码在数据库里明码保存， 
   * 当然在登陆认证的生活，这个类也负责对form里输入的密码进行编码。
   * @return
   */
  /*@Bean(name="credentialsMatcher")
  public HashedCredentialsMatcher hashedCredentialsMatcher(){
     HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

     hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
     hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
     //storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
     hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);

     return hashedCredentialsMatcher;
  }*/
  
  /**
   * @see DefaultWebSessionManager
   * @return
   */
  @Bean(name="sessionManager")
  public DefaultWebSessionManager sessionManager() {
      DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
      sessionManager.setCacheManager(ehCacheManager());
      sessionManager.setGlobalSessionTimeout(1800000);//超时时间
      sessionManager.setSessionValidationSchedulerEnabled(true);//定时清除无效的session
      sessionManager.setDeleteInvalidSessions(true);//删除无效的session
      sessionManager.setSessionDAO(sessionDao());
      return sessionManager;
  }
  
  @Bean(name = "sessionDAO")
  public MySessionDao sessionDao(){
      MySessionDao mySessionDAO = new MySessionDao();
      mySessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
      mySessionDAO.setCacheManager(ehCacheManager());
      return mySessionDAO;
  }
  /**
   * EhCacheManager，缓存管理，用户登陆成功后，把用户信息和权限信息缓存起来，
   * 然后每次用户请求时，放入用户的session中，如果不设置这个bean，每个请求都会查询一次数据库。
   * 
   * @return
   */
  @Bean(name = "ehCacheManager")
  @DependsOn("lifecycleBeanPostProcessor")
  public EhCacheManager ehCacheManager() {
      EhCacheManager cacheManager = new EhCacheManager();
      cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
      return cacheManager;
  }

  @Bean(name = "rememberMeCookie")
  public SimpleCookie rememberMeCookie() {
      // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
      SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
      // 记住我cookie生效时间30天 ,单位秒;
      simpleCookie.setMaxAge(259200);
      return simpleCookie;
  }
  /**
   * cookie管理对象;
   * 
   * @return
   */
 @Bean(name = "rememberMeManager")
  public CookieRememberMeManager rememberMeManager() {
      CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
      cookieRememberMeManager.setCookie(rememberMeCookie());
      return cookieRememberMeManager;
  }
}
