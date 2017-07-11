package com.demo.base;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class UserLoginFilter extends FormAuthenticationFilter {
  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    if ((isLoginRequest(request, response))) {
      if ((isLoginSubmission(request, response))) {
        return executeLogin(request, response);
      }
      return true;
    }else{
      saveRequestAndRedirectToLogin(request, response);
      return false;
    }
  }
}
