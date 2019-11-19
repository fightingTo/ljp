package com.cheer.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创意单元关联请求封装
 *
 * @Created by ljp on 2019/11/14.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreativeUnitRequest {
    private List<CreativeUnitItem> creativeUnitItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreativeUnitItem {
        private Long creativeId;
        private Long unitId;
    }
}
