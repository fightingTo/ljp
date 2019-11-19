package com.cheer.ad.index.plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 推广计划索引实体类
 * 定义的字段属性代表就是需要添加索引的字段属性
 *
 * @Created by ljp on 2019/11/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdPlanObject {

    private Long planId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;
}
