package com.demo.base;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class URLPermissionsFilter extends PermissionsAuthorizationFilter {
  @Override
  public boolean isAccessAllowed(ServletRequest request, ServletResponse response,
      Object mappedValue) {
    Subject subject = getSubject(request, response);
    System.out.println(subject.getSession().getId());
    boolean isPermitted = false;
    String[] perms = (String[])mappedValue;
    for (String perm : perms) {
      if(subject.isPermitted(perm)){
        isPermitted = true;
      }
    }
    
    return isPermitted;
  }
}
