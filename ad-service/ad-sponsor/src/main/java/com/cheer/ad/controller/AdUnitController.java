package com.cheer.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cheer.ad.exception.AdException;
import com.cheer.ad.service.IAdUnitService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 推广单元
 *
 * @Created by ljp on 2019/11/19.
 */
@Slf4j
@Controller
public class AdUnitController {

    @Autowired
    private IAdUnitService unitService;

    @PostMapping("/create/unit")
    public AdUnitResponse createUnit(@RequestBody AdUnitRequest request) throws AdException {
        log.info("ad-sponsor: createUnit -> {}",
                JSON.toJSONString(request));
        return unitService.createUnit(request);
    }

    @PostMapping("/create/unitKeyword")
    public AdUnitKeywordResponse createUnitKeyword(@RequestBody AdUnitKeywordRequest request)
            throws AdException {
        log.info("ad-sponsor: createUnitKeyword -> {}",
                JSON.toJSONString(request));
        return unitService.createUnitKeyword(request);
    }

    @PostMapping("/create/unitIt")
    public AdUnitItResponse createUnitIt(@RequestBody AdUnitItRequest request)
            throws AdException {
        log.info("ad-sponsor: createUnitIt -> {}",
                JSON.toJSONString(request));
        return unitService.createUnitIt(request);
    }

    @PostMapping("/create/unitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(@RequestBody AdUnitDistrictRequest request)
            throws AdException {
        log.info("ad-sponsor: createUnitDistrict -> {}",
                JSON.toJSONString(request));
        return unitService.createUnitDistrict(request);
    }

    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(@RequestBody CreativeUnitRequest request)
            throws AdException {
        log.info("ad-sponsor: createCreativeUnit -> {}",
                JSON.toJSONString(request));
        return unitService.createCreativeUnit(request);
    }

}
