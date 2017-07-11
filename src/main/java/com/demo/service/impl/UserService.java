package com.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.entity.UUser;
import com.demo.mapper.UUserMapper;
import com.demo.service.IUserService;

@Component
public class UserService implements IUserService {
  @Autowired
  private UUserMapper userMapper;
  
  @Override
  public UUser findUserByUserNameAndPwd(String username, String password) {
    UUser user = new UUser();
    user.setNickname(username);
    user.setPswd(password);
    return userMapper.findUserByUserNameAndPwd(user);
  }
}
