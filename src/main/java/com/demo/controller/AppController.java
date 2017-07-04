package com.demo.controller;

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
    
    @RequestMapping("/")
    public String root(ModelMap map) {
      return "login";  
    }
    
    @RequestMapping("/index")
    public String index(ModelMap map) {
      return "index";  
    }
    
    
    @RequestMapping("/login")
    @ResponseBody
    public String login(String username, String password) {
      return "Success";    
    }
    
    @RequestMapping("/user")
    @ResponseBody
    public UUser allUser(@RequestParam("username") String username) {
     
      return userService.findUserByUserName(username);  
    }
}