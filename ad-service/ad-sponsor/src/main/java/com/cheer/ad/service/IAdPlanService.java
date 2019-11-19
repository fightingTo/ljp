package com.cheer.ad.service;

import com.cheer.ad.entity.AdPlan;
import com.cheer.ad.exception.AdException;
import com.cheer.ad.vo.AdPlanGetRequest;
import com.cheer.ad.vo.AdPlanRequest;
import com.cheer.ad.vo.AdPlanResponse;

import java.util.List;

/**
 * 广告计划接口定义
 *
 * @Created by ljp on 2019/11/14.
 */
public interface IAdPlanService {

    /**
     * <h2>创建推广计划</h2>
     * */
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    /**
     * <h2>获取推广计划</h2>
     * */
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException;

    /**
     * <h2>更新推广计划</h2>
     * */
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    /**
     * <h2>删除推广计划</h2>
     * */
    void deleteAdPlan(AdPlanRequest request) throws AdException;
}
