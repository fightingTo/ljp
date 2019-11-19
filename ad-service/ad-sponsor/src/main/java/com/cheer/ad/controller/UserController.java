package com.cheer.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cheer.ad.exception.AdException;
import com.cheer.ad.service.IUserService;
import com.cheer.ad.vo.UserRequest;
import com.cheer.ad.vo.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户
 *
 * @Created by ljp on 2019/11/19.
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/create/user")
    public UserResponse createUser(@RequestBody UserRequest request) throws AdException {
        log.info("ad-sponsor: createUser -> {}", JSON.toJSONString(request));
        return userService.createUser(request);
    }
}
