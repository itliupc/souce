package com.demo.service;

import com.demo.entity.UUser;

public interface IUserService {
  UUser findUserByUserNameAndPwd(String username, String password);
}
