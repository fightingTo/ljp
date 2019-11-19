package com.cheer.ad.service;

import com.cheer.ad.exception.AdException;
import com.cheer.ad.vo.UserRequest;
import com.cheer.ad.vo.UserResponse;

/**
 * 用户业务层
 *
 * @Created by ljp on 2019/11/14.
 */
public interface IUserService {

    UserResponse createUser(UserRequest userRequest) throws AdException;
}
