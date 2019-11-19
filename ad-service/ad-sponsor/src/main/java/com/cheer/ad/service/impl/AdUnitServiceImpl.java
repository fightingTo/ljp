package com.cheer.ad.service.impl;

import com.cheer.ad.dao.AdPlanRepository;
import com.cheer.ad.dao.AdUnitRepository;
import com.cheer.ad.dao.CreativeRepository;
import com.cheer.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.cheer.ad.dao.unit_condition.AdUnitItRepository;
import com.cheer.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.cheer.ad.dao.unit_condition.CreativeUnitRepository;
import com.cheer.ad.entity.AdPlan;
import com.cheer.ad.entity.AdUnit;
import com.cheer.ad.entity.Creative;
import com.cheer.ad.entity.unit_condition.AdUnitDistrict;
import com.cheer.ad.entity.unit_condition.AdUnitIt;
import com.cheer.ad.entity.unit_condition.AdUnitKeyword;
import com.cheer.ad.entity.unit_condition.CreativeUnit;
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
import com.cheer.ad.vo.CommonError;
import com.cheer.ad.vo.CreativeUnitRequest;
import com.cheer.ad.vo.CreativeUnitResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 广告单元业务实现
 *
 * @Created by ljp on 2019/11/14.
 */
@Slf4j
@Service
public class AdUnitServiceImpl implements IAdUnitService {
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitKeywordRepository keywordRepository;
    @Autowired
    private AdUnitItRepository itRepository;
    @Autowired
    private AdUnitDistrictRepository districtRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;


    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {

        if (request == null || !request.createValidate()) {
            throw new AdException(CommonError.REQUEST_PARAM_ERROR);
        }

        // 查找新增单元是否有对应的广告投放计划
        Optional<AdPlan> planOptional = planRepository.findById(request.getPlanId());
        if (!planOptional.isPresent()) {
            throw new AdException(CommonError.CAN_NOT_FIND_RECORD);
        }

        // 查找广告计划中是否含有与新增单元相同的广告投放单元
        AdUnit oldUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (oldUnit != null) {
            throw new AdException(CommonError.SAME_NAME_UNIT_ERROR);
        }

        // 创建新的广告单元
        AdUnit adUnit = unitRepository.save(new AdUnit(
                request.getPlanId(),
                request.getUnitName(),
                request.getPositionType(),
                request.getBudget()
        ));

        return AdUnitResponse.builder()
                .id(adUnit.getId())
                .unitName(adUnit.getUnitName())
                .build();
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        if (request == null || CollectionUtils.isEmpty(request.getUnitKeywords())) {
            log.error("createUnitKeyword request param is empty");
            return new AdUnitKeywordResponse();
        }
        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());

        // 判断请求参数中关键词关联的广告单元个数与数据库中的数据是否一致
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(CommonError.REQUEST_DATA_NUMBER_ERROR);
        }

        // 获取请求参数获取对应的单元关键词集合 adUnitKeywords
        List<AdUnitKeyword> adUnitKeywords = new ArrayList<>();
        request.getUnitKeywords().forEach(i -> adUnitKeywords.add(
                new AdUnitKeyword(i.getUnitId(), i.getKeyword())
        ));

        // 保存所有的关键词广告数据
        List<Long> ids = keywordRepository.saveAll(adUnitKeywords).stream()
                .map(AdUnitKeyword::getId)
                .collect(Collectors.toList());

        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(CommonError.REQUEST_DATA_NUMBER_ERROR);
        }

        List<AdUnitIt> unitIts = new ArrayList<>();
        request.getUnitIts().forEach(i ->
                unitIts.add(new AdUnitIt(i.getUnitId(), i.getItTag()))
        );

        List<Long> ids = itRepository.saveAll(unitIts)
                .stream()
                .map(AdUnitIt::getId)
                .collect(Collectors.toList());

        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request)
            throws AdException {
        List<Long> unitIds = request.getUnitDistricts()
                .stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(CommonError.REQUEST_DATA_NUMBER_ERROR);
        }
        List<AdUnitDistrict> districts = new ArrayList<>();
        request.getUnitDistricts().forEach(i ->
                districts.add(new AdUnitDistrict(i.getUnitId(), i.getProvince(), i.getCity()))
        );

        List<Long> ids = districtRepository.saveAll(districts)
                .stream().map(AdUnitDistrict::getId)
                .collect(Collectors.toList());

        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request)
            throws AdException {
        List<Long> creativeIds = request.getCreativeUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .collect(Collectors.toList());
        List<Long> unitIds = request.getCreativeUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedCreativeExist(creativeIds) || !isRelatedUnitExist(unitIds)) {
            throw new AdException(CommonError.REQUEST_DATA_NUMBER_ERROR);
        }

        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getCreativeUnitItems().forEach(i ->
                creativeUnits.add(new CreativeUnit(i.getUnitId(), i.getCreativeId()))
        );

        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits).stream()
                .map(CreativeUnit::getId)
                .collect(Collectors.toList());

        return new CreativeUnitResponse(ids);
    }

    private boolean isRelatedUnitExist(List<Long> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }

        // 查找对应关联关键词的广告单元
        List<AdUnit> adUnits = unitRepository.findAllById(unitIds);
        return adUnits.size() == new HashSet<>(unitIds).size();
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIds){
        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }

        // 查找对应关联创意的广告单元
        List<Creative> creatives = creativeRepository.findAllById(creativeIds);

        // 传进来去重后的创意个数应该与对应数据库关联广告单元的创意个数相同
        return creatives.size() == new HashSet<>(creativeIds).size();
    }
}
