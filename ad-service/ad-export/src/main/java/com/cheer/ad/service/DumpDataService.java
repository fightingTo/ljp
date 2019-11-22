package com.cheer.ad.service;

import com.alibaba.fastjson.JSON;
import com.cheer.ad.constant.CommonStatus;
import com.cheer.ad.dao.AdPlanRepository;
import com.cheer.ad.dao.AdUnitRepository;
import com.cheer.ad.dao.CreativeRepository;
import com.cheer.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.cheer.ad.dao.unit_condition.AdUnitItRepository;
import com.cheer.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.cheer.ad.dao.unit_condition.CreativeUnitRepository;
import com.cheer.ad.dump.AdCreativeTable;
import com.cheer.ad.dump.AdCreativeUnitTable;
import com.cheer.ad.dump.AdPlanTable;
import com.cheer.ad.dump.AdUnitDistrictTable;
import com.cheer.ad.dump.AdUnitItTable;
import com.cheer.ad.dump.AdUnitKeywordTable;
import com.cheer.ad.dump.AdUnitTable;
import com.cheer.ad.entity.AdPlan;
import com.cheer.ad.entity.AdUnit;
import com.cheer.ad.entity.Creative;
import com.cheer.ad.entity.unit_condition.AdUnitDistrict;
import com.cheer.ad.entity.unit_condition.AdUnitIt;
import com.cheer.ad.entity.unit_condition.AdUnitKeyword;
import com.cheer.ad.entity.unit_condition.CreativeUnit;
import com.cheer.ad.exception.AdException;
import com.cheer.ad.vo.CommonConstants;
import com.cheer.ad.vo.CommonError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据导出实现
 *
 * @Created by ljp on 2019/11/20.
 */
@Service
@Slf4j
public class DumpDataService {
    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository districtRepository;
    @Autowired
    private AdUnitItRepository itRepository;
    @Autowired
    private AdUnitKeywordRepository keywordRepository;

    public void dumpAdTableData() throws AdException{
        dumpAdPlanTable(
                String.format("%s%s", CommonConstants.FileConstants.DATA_EXPORT_DIR,
                        CommonConstants.FileConstants.AD_PLAN)
        );
        
        dumpAdUnitTable(
                String.format("%s%s", CommonConstants.FileConstants.DATA_EXPORT_DIR,
                        CommonConstants.FileConstants.AD_UNIT)
        );

        dumpAdCreativeTable(
                String.format("%s%s", CommonConstants.FileConstants.DATA_EXPORT_DIR,
                        CommonConstants.FileConstants.AD_CREATIVE)
        );

        dumpAdCreativeUnitTable(
                String.format("%s%s", CommonConstants.FileConstants.DATA_EXPORT_DIR,
                        CommonConstants.FileConstants.AD_CREATIVE_UNIT)
        );

        dumpAdUnitDistrictTable(
                String.format("%s%s", CommonConstants.FileConstants.DATA_EXPORT_DIR,
                        CommonConstants.FileConstants.AD_UNIT_DISTRICT)
        );

        dumpAdUnitItTable(
                String.format("%s%s", CommonConstants.FileConstants.DATA_EXPORT_DIR,
                        CommonConstants.FileConstants.AD_UNIT_IT)
        );

        dumpAdUnitKeywordTable(
                String.format("%s%s", CommonConstants.FileConstants.DATA_EXPORT_DIR,
                        CommonConstants.FileConstants.AD_UNIT_KEYWORD)
        );
    }

    private void dumpAdPlanTable(String filePath) throws AdException {
        // 获取有效广告计划数据
        List<AdPlan> adPlanList = planRepository.findAllByPlanStatus(CommonStatus.VALID.getStatus());
        if (CollectionUtils.isEmpty(adPlanList)) {
            throw new AdException(CommonError.GET_DATA_EMPTY);
        }

        // 将获取到的广告数据字段转化为AdPlanTable对应字段
        List<AdPlanTable> planTableList = new ArrayList<>();
        for (AdPlan adPlan : adPlanList) {
            planTableList.add(new AdPlanTable(
                    adPlan.getId(),
                    adPlan.getUserId(),
                    adPlan.getPlanStatus(),
                    adPlan.getStartDate(),
                    adPlan.getEndDate()
            ));
        }

        // 将数据写入到文件
        Path path = Paths.get(filePath);
        try(BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdPlanTable planTable : planTableList) {
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
            writer.close();
        }catch (IOException e) {
            log.error("dumpAdPlanTable error, fileName:{}", path.getFileName());
            throw new AdException(CommonError.WRITE_FILE_DATA_ERROR);
        }
    }

    private void dumpAdUnitKeywordTable(String filePath) throws AdException {
        // 获取有效数据
        List<AdUnitKeyword> adUnitKeywords = keywordRepository.findAll();
        if (CollectionUtils.isEmpty(adUnitKeywords)) {
            throw new AdException(CommonError.GET_DATA_EMPTY);
        }

        // 映射数据字段为AdUnitKeywordTable字段
        List<AdUnitKeywordTable> keywordTableList = new ArrayList<>();
        for (AdUnitKeyword unitKeyword : adUnitKeywords) {
            keywordTableList.add(new AdUnitKeywordTable(
                    unitKeyword.getUnitId(),
                    unitKeyword.getKeyword()
            ));
        }

        // 数据写入指定目录文件
       writeData(filePath, keywordTableList);
    }

    private void dumpAdUnitItTable(String filePath) throws AdException {
        // 获取数据
        List<AdUnitIt> adUnitIts = itRepository.findAll();
        if (CollectionUtils.isEmpty(adUnitIts)) {
            throw new AdException(CommonError.GET_DATA_EMPTY);
        }

        // 映射数据字段
        List<AdUnitItTable> itTableList = new ArrayList<>();
        for (AdUnitIt adUnitIt : adUnitIts) {
            itTableList.add(new AdUnitItTable(
                    adUnitIt.getUnitId(),
                    adUnitIt.getItTag()
            ));
        }

        // 写入数据
        writeData(filePath, itTableList);
    }

    private void dumpAdUnitDistrictTable(String filePath) throws AdException {
        // 获取数据
        List<AdUnitDistrict> districts = districtRepository.findAll();
        if (CollectionUtils.isEmpty(districts)) {
            throw new AdException(CommonError.GET_DATA_EMPTY);
        }

        // 映射字段数据
        List<AdUnitDistrictTable> districtTableList = new ArrayList<>();
        for (AdUnitDistrict district : districts) {
            districtTableList.add(new AdUnitDistrictTable(
                    district.getUnitId(),
                    district.getProvince(),
                    district.getCity()
            ));
        }

        // 写入数据
        writeData(filePath, districtTableList);
    }

    private void dumpAdCreativeUnitTable(String filePath) throws AdException {
        // 获取数据
        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();
        if (CollectionUtils.isEmpty(creativeUnits)) {
            throw new AdException(CommonError.GET_DATA_EMPTY);
        }

        // 映射字段数据
        List<AdCreativeUnitTable> creativeUnitList = new ArrayList<>();
        for (CreativeUnit creativeUnit : creativeUnits) {
            creativeUnitList.add(new AdCreativeUnitTable(
                    creativeUnit.getCreativeId(),
                    creativeUnit.getUnitId()
            ));
        }

        // 写入数据
        writeData(filePath, creativeUnitList);
    }

    private void dumpAdCreativeTable(String filePath) throws AdException {
        // 获取数据
        List<Creative> creatives = creativeRepository.findAll();
        if (CollectionUtils.isEmpty(creatives)) {
            throw new AdException(CommonError.GET_DATA_EMPTY);
        }

        // 映射字段数据
        List<AdCreativeTable> creativeTableList = new ArrayList<>();
        for (Creative creative : creatives) {
            creativeTableList.add(new AdCreativeTable(
                    creative.getId(),
                    creative.getName(),
                    creative.getType(),
                    creative.getMaterialType(),
                    creative.getHeight(),
                    creative.getWidth(),
                    creative.getAuditStatus(),
                    creative.getUrl()
            ));
        }

        // 写入数据
        writeData(filePath, creativeTableList);
    }

    private void dumpAdUnitTable(String filePath) throws AdException {
        // 获取数据
        List<AdUnit> adUnits = unitRepository.findAllByUnitStatus(CommonStatus.VALID.getStatus());
        if (CollectionUtils.isEmpty(adUnits)) {
            throw new AdException(CommonError.GET_DATA_EMPTY);
        }

        // 映射字段数据
        List<AdUnitTable> unitTableList = new ArrayList<>();
        for (AdUnit unit : adUnits) {
            unitTableList.add(new AdUnitTable(
                    unit.getId(),
                    unit.getUnitStatus(),
                    unit.getPositionType(),
                    unit.getPlanId()
            ));
        }

        // 写入数据
        writeData(filePath, unitTableList);
    }

    private <T> void writeData(String filePath, List<T> dataList) throws AdException {
        Path path = Paths.get(filePath);
        try(BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (T data : dataList) {
                writer.write(JSON.toJSONString(data));
                writer.newLine();
            }
            writer.close();
        }catch (IOException e) {
            log.error("write data error");
            throw new AdException(CommonError.WRITE_FILE_DATA_ERROR);
        }
    }
}
