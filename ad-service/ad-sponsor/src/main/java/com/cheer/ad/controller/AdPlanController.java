package com.cheer.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cheer.ad.entity.AdPlan;
import com.cheer.ad.exception.AdException;
import com.cheer.ad.service.IAdPlanService;
import com.cheer.ad.vo.AdPlanGetRequest;
import com.cheer.ad.vo.AdPlanRequest;
import com.cheer.ad.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 推广广告计划
 *
 * @Created by ljp on 2019/11/19.
 */
@Slf4j
@Controller
public class AdPlanController {

    @Autowired
    private IAdPlanService planService;

    @PostMapping("/create/plan")
    public AdPlanResponse createAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: createPlan -> {}", JSON.toJSONString(request));
        return planService.createAdPlan(request);
    }

    @PutMapping("/update/plan")
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: updatePlan -> {}", JSON.toJSONString(request));
        return planService.updateAdPlan(request);
    }

    @PostMapping("/get/plan")
    public List<AdPlan> getPlanByIds(@RequestBody AdPlanGetRequest request) throws AdException {
        log.info("ad-sponsor: getPlanByIds -> {}", JSON.toJSONString(request));
        return planService.getAdPlanByIds(request);
    }

    @DeleteMapping ("/delete/plan")
    public void deleteAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: updatePlan -> {}", JSON.toJSONString(request));
        planService.deleteAdPlan(request);
    }


}
