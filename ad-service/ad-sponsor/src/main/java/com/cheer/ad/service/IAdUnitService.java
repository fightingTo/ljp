package com.cheer.ad.service;

import com.cheer.ad.exception.AdException;
import com.cheer.ad.vo.AdUnitDistrictRequest;
import com.cheer.ad.vo.AdUnitDistrictResponse;
import com.cheer.ad.vo.AdUnitItRequest;
import com.cheer.ad.vo.AdUnitItResponse;
import com.cheer.ad.vo.AdUnitKeywordRequest;
import com.cheer.ad.vo.AdUnitKeywordResponse;
import com.cheer.ad.vo.AdUnitRequest;
import com.cheer.ad.vo.AdUnitResponse;
import com.cheer.ad.vo.CreativeUnitRequest;
import com.cheer.ad.vo.CreativeUnitResponse;

/**
 * 广告单元（及维度）接口定义
 *
 * @Created by ljp on 2019/11/14.
 */
public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException;

    AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException;

    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;

    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;
}
