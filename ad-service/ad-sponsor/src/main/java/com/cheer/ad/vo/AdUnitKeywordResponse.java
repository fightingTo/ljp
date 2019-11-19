package com.cheer.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 关键词响应
 *
 * @Created by ljp on 2019/11/14.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdUnitKeywordResponse {
    private List<Long> ids;
}
