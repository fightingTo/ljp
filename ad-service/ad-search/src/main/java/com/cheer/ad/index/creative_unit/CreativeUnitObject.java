package com.cheer.ad.index.creative_unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 创意关联单元索引对象
 *
 * @Created by ljp on 2019/11/19.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreativeUnitObject {

    private Long creativeId;
    private Long unitId;

}