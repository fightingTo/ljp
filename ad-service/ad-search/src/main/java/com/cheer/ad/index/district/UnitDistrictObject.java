package com.cheer.ad.index.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地域索引映射对象
 *
 * @Created by ljp on 2019/11/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDistrictObject {
    /**
     * 索引结构：
     *      key -> (province-city)
     *      value -> unitId
     */
    private Long unitId;
    private String province;
    private String city;

}