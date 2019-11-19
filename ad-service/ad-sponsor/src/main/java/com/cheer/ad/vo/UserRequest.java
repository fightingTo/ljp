package com.cheer.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * 用户请求数据封装对象
 *
 * @Created by ljp on 2019/11/14.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;

    public boolean validate() {

        return !StringUtils.isEmpty(username);
    }
}
