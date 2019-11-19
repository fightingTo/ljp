package com.cheer.ad.service.impl;

import com.cheer.ad.dao.AdUserRespository;
import com.cheer.ad.entity.AdUser;
import com.cheer.ad.exception.AdException;
import com.cheer.ad.service.IUserService;
import com.cheer.ad.utils.CommonUtils;
import com.cheer.ad.vo.CommonError;
import com.cheer.ad.vo.UserRequest;
import com.cheer.ad.vo.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实现类
 *
 * @Created by ljp on 2019/11/14.
 */
@Service
@Slf4j
public class UserServiceimpl  implements IUserService {

    @Autowired
    private AdUserRespository userRespository;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) throws AdException {
        if (!userRequest.validate()) {
            throw new AdException(CommonError.REQUEST_PARAM_ERROR);
        }

        AdUser username = userRespository.findByUsername(userRequest.getUsername());
        if (username != null) {
            throw new AdException(CommonError.SAME_NAME_ERROR);
        }
        AdUser adUser = userRespository.save(new AdUser(
                userRequest.getUsername(),
                CommonUtils.md5(userRequest.getUsername())
        ));

        return UserResponse.builder()
                .userId(adUser.getId())
                .username(adUser.getUsername())
                .token(adUser.getToken())
                .createTime(adUser.getCreateTime())
                .updateTime(adUser.getUpdateTime())
                .build();
    }
}
