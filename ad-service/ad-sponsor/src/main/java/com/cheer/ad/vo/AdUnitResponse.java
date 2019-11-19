package com.cheer.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推广单元响应
 *
 * @Created by ljp on 2019/11/14.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitResponse {
    private Long id;
    private String unitName;
}
