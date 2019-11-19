package com.cheer.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创意响应
 *
 * @Created by ljp on 2019/11/14.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreativeResponse {
    private Long id;
    private String name;
}
