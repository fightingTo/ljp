package com.cheer.ad.service.impl;

import com.cheer.ad.constant.CommonStatus;
import com.cheer.ad.dao.AdPlanRepository;
import com.cheer.ad.dao.AdUserRespository;
import com.cheer.ad.entity.AdPlan;
import com.cheer.ad.entity.AdUser;
import com.cheer.ad.exception.AdException;
import com.cheer.ad.service.IAdPlanService;
import com.cheer.ad.utils.CommonUtils;
import com.cheer.ad.vo.AdPlanGetRequest;
import com.cheer.ad.vo.AdPlanRequest;
import com.cheer.ad.vo.AdPlanResponse;
import com.cheer.ad.vo.CommonError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 广告计划业务实现
 *
 * @Created by ljp on 2019/11/14.
 */
@Slf4j
@Service
public class AdPlanServiceImpl implements IAdPlanService {

    @Autowired
    private AdPlanRepository planRepository;

    @Autowired
    private AdUserRespository userRespository;

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {
        if (request == null || !request.createValidate()) {
            throw new AdException(CommonError.REQUEST_PARAM_ERROR);
        }

        Optional<AdUser> userOptional = userRespository.findById(request.getUserId());
        if (!userOptional.isPresent()) {
            throw new AdException(CommonError.CAN_NOT_FIND_RECORD);
        }

        // 查找是否有用户相关联的相同推广计划，若有抛出异常，若没有创建新的推广计划
        AdPlan oldPlan = planRepository.
                findByUserIdAndPlanName(request.getUserId(), request.getPlanName());
        if (oldPlan != null) {
            throw new AdException(CommonError.SAME_NAME_PLAN_ERROR);
        }

        AdPlan newPlan = planRepository.save(new AdPlan(
                request.getUserId(),
                request.getPlanName(),
                CommonUtils.parseStringDate(request.getStartDate()),
                CommonUtils.parseStringDate(request.getEndDate())
        ));

        return AdPlanResponse.builder()
                .id(newPlan.getId())
                .planName(newPlan.getPlanName())
                .build();
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
        if (request == null || !request.validate()) {
            throw new AdException(CommonError.REQUEST_PARAM_ERROR);
        }
        return planRepository.findAllByIdInAndUserId(request.getIds(), request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {
        if (request == null || !request.updateValidate()) {
            throw new AdException(CommonError.REQUEST_PARAM_ERROR);
        }

        AdPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());

        if (plan == null) {
            throw new AdException(CommonError.CAN_NOT_FIND_RECORD);
        }

        if (request.getPlanName() != null) {
            plan.setPlanName(request.getPlanName());
        }

        if (request.getStartDate() != null) {
            plan.setStartDate(
                    CommonUtils.parseStringDate(request.getStartDate())
            );
        }

        if (request.getEndDate() != null) {
            plan.setEndDate(
                    CommonUtils.parseStringDate(request.getEndDate())
            );
        }

        plan.setUpdateTime(new Date());

        plan = planRepository.save(plan);

        return AdPlanResponse.builder()
                .id(plan.getId())
                .planName(plan.getPlanName())
                .build();
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        if (request == null || !request.deleteValidate()) {
            throw new AdException(CommonError.REQUEST_PARAM_ERROR);
        }

        AdPlan plan = planRepository.findByIdAndUserId(
                request.getId(), request.getUserId()
        );
        if (plan == null) {
            throw new AdException(CommonError.CAN_NOT_FIND_RECORD);
        }

        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());
        // 逻辑删除，将标识更新
        planRepository.save(plan);
    }
}
