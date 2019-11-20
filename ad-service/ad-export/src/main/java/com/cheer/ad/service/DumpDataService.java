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
import com.cheer.ad.dump.AdPlanTable;
import com.cheer.ad.entity.AdPlan;
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

    private void dumpAdUnitKeywordTable(String filePath) {
        //TODO 待完成(2019年11月20日)
    }

    private void dumpAdUnitItTable(String filePath) {
    }

    private void dumpAdUnitDistrictTable(String filePath) {
    }

    private void dumpAdCreativeUnitTable(String filePath) {
    }

    private void dumpAdCreativeTable(String filePath) {
    }

    private void dumpAdUnitTable(String filePath) {
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
            throw new AdException(CommonError.WRITE_DATA_ERROR);
        }
    }
}
