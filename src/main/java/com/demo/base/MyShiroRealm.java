package com.demo.base;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.entity.UUser;
import com.demo.service.IUserService;

public class MyShiroRealm extends AuthorizingRealm{
  @Autowired
  private IUserService userService;
  
  
  public MyShiroRealm() {
    setName("MyShiroRealm"); 
  }
  
  /**
   * 认证信息.(身份验证) : Authentication 是用来验证用户身份
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken)
      throws AuthenticationException {
    System.out.println("身份认证方法：MyShiroRealm.doGetAuthenticationInfo()");

    UsernamePasswordToken token = (UsernamePasswordToken) authToken;
    // 从数据库获取对应用户名密码的用户
    UUser user = userService.findUserByUserNameAndPwd(token.getUsername(),String.valueOf(token.getPassword()));
    if (null == user) {
        throw new AccountException("帐号或密码不正确！");
    }else if(user.getStatus()==0){
        throw new DisabledAccountException("帐号已经禁止登录！");
    }
    
    return new SimpleAuthenticationInfo(token.getUsername(), user.getPswd(), getName());
  }
  
  /**
   * 授权
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    System.out.println("权限认证方法：MyShiroRealm.doGetAuthenticationInfo()");
    SecurityUtils.getSubject().getPrincipal();
//    String userId = token.getId();
    SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
    //根据用户ID查询角色（role），放入到Authorization里。
    /*Map<String, Object> map = new HashMap<String, Object>();
    map.put("user_id", userId);
    List<SysRole> roleList = sysRoleService.selectByMap(map);
    Set<String> roleSet = new HashSet<String>();
    for(SysRole role : roleList){
        roleSet.add(role.getType());
    }*/
    //实际开发，当前登录用户的角色和权限信息是从数据库来获取的，我这里写死是为了方便测试
    Set<String> roleSet = new HashSet<String>();
    roleSet.add("100002");
    info.setRoles(roleSet);
    //根据用户ID查询权限（permission），放入到Authorization里。
    /*List<SysPermission> permissionList = sysPermissionService.selectByMap(map);
    Set<String> permissionSet = new HashSet<String>();
    for(SysPermission Permission : permissionList){
        permissionSet.add(Permission.getName());
    }*/
    Set<String> permissionSet = new HashSet<String>();
    permissionSet.add("权限添加");
    info.setStringPermissions(permissionSet);
    return info;
  }
  
}
