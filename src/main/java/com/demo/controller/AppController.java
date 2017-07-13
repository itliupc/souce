package com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.entity.UUser;
import com.demo.service.IUserService;

@Controller
public class AppController {
    @Autowired
    private IUserService userService;
    
    @RequestMapping("/login")
    public String root(ModelMap map) {
      return "login";  
    }
    
    @RequestMapping("/index")
    public String index(ModelMap map) {
      return "index";  
    }
    
    @RequestMapping("/403")
    public String forbiddenPage(ModelMap map) {
      return "403";  
    }
    
    
    @RequestMapping(value="/ajaxLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ajaxLogin(HttpServletRequest request, String username, String password) {
      Map<String, Object> resultMap = new HashMap<String, Object>();
      UsernamePasswordToken uptoken =
          new UsernamePasswordToken(username, password, false);
      WebSubject subject = (WebSubject) SecurityUtils.getSubject();
      subject.login(uptoken);
      
      resultMap.put("contentPath", request.getContextPath());
      resultMap.put("username", username.trim());
      resultMap.put("sessionId", subject.getSession().getId());
      return resultMap;    
    }
    
    @RequestMapping("/user")
    @ResponseBody
    public UUser allUser() {
     
      return null;  
    }
    
    @RequestMapping("/test")
    @ResponseBody
    public String test() {
     
      return "test";  
    }
}