package com.cheer.ad.vo;

import com.cheer.ad.constant.CommonStatus;
import com.cheer.ad.entity.Creative;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 创意请求封装
 *
 * @Created by ljp on 2019/11/14.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreativeRequest {
    private String name;
    private Integer type;
    private Integer materialType;
    private Integer height;
    private Integer width;
    private Long size;
    private Integer duration;
    private Long userId;
    private String url;

    public Creative convertToEntity() {
        return Creative.builder()
                .name(name)
                .type(type)
                .materialType(materialType)
                .height(height)
                .width(width)
                .size(size)
                .duration(duration)
                .userId(userId)
                .url(url)
                .createTime(new Date())
                .updateTime(new Creative().getCreateTime())
                .auditStatus(CommonStatus.VALID.getStatus())
                .build();
    }
}
