package com.cheer.ad.index;

import com.alibaba.fastjson.JSON;
import com.cheer.ad.dump.AdCreativeTable;
import com.cheer.ad.dump.AdCreativeUnitTable;
import com.cheer.ad.dump.AdPlanTable;
import com.cheer.ad.dump.AdUnitDistrictTable;
import com.cheer.ad.dump.AdUnitItTable;
import com.cheer.ad.dump.AdUnitKeywordTable;
import com.cheer.ad.dump.AdUnitTable;
import com.cheer.ad.handler.AdLevelDataHandler;
import com.cheer.ad.mysql.constant.OperationType;
import com.cheer.ad.vo.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 读取加载保存的全量索引文件
 *
 * @DependsOn: 控制DataTable.java类优先于此类初始化加载
 *
 * @Created by ljp on 2019/11/21.
 */
@Slf4j
@Component
@DependsOn("dataTable")
public class IndexFileLoader {

    /**
     * 全量数据加载：在IndexFileLoader加载后，init方法执行
     *
     * @PostConstruct： 初始化的操作，等依赖全部加载完再执行，只执行一次
     */
    @PostConstruct
    public void init(){
        // 加载ad_plan.data索引数据
        List<String> planStringList= loadDumpData(String.format(Locale.ROOT, "%s%s",
                CommonConstants.FileConstants.DATA_EXPORT_DIR,
                CommonConstants.FileConstants.AD_PLAN
        ));
        for (String plan : planStringList) {
            AdLevelDataHandler.handleLevel2(
                    JSON.parseObject(plan, AdPlanTable.class),
                    OperationType.ADD
            );
        }

        // 加载ad_unit.data索引数据
        List<String> unitStringList = loadDumpData(String.format(Locale.ROOT, "%s%s",
                CommonConstants.FileConstants.DATA_EXPORT_DIR,
                CommonConstants.FileConstants.AD_UNIT
        ));
        for (String unit : unitStringList) {
            AdLevelDataHandler.handleLevel3(
                    JSON.parseObject(unit, AdUnitTable.class),
                    OperationType.ADD
            );
        }

        // 加载ad_creative.data索引数据
        List<String> creativeStringList = loadDumpData(String.format(Locale.ROOT, "%s%s",
                CommonConstants.FileConstants.DATA_EXPORT_DIR,
                CommonConstants.FileConstants.AD_CREATIVE
        ));
        for (String creative : creativeStringList) {
            AdLevelDataHandler.handleLevel2(
                    JSON.parseObject(creative, AdCreativeTable.class),
                    OperationType.ADD
            );
        }

        // 加载ad_creative_unit.data索引数据
        List<String> creativeUnits = loadDumpData(String.format(Locale.ROOT, "%s%s",
                CommonConstants.FileConstants.DATA_EXPORT_DIR,
                CommonConstants.FileConstants.AD_CREATIVE_UNIT
        ));
        for (String creativeUnit : creativeUnits) {
            AdLevelDataHandler.handleLevel3(
                    JSON.parseObject(creativeUnit, AdCreativeUnitTable.class),
                    OperationType.ADD
            );
        }

        // 加载ad_unit_it.data索引数据
        List<String> unitIts = loadDumpData(String.format(Locale.ROOT, "%s%s",
                CommonConstants.FileConstants.DATA_EXPORT_DIR,
                CommonConstants.FileConstants.AD_UNIT_IT
        ));
        for (String unitIt : unitIts) {
            AdLevelDataHandler.handleLevel4(
                    JSON.parseObject(unitIt, AdUnitItTable.class),
                    OperationType.ADD
            );
        }

        // 加载ad_unit_district.data索引数据
        List<String> unitDistricts = loadDumpData(String.format(Locale.ROOT, "%s%s",
                CommonConstants.FileConstants.DATA_EXPORT_DIR,
                CommonConstants.FileConstants.AD_UNIT_DISTRICT
        ));
        for (String unitDistrict : unitDistricts) {
            AdLevelDataHandler.handleLevel4(
                    JSON.parseObject(unitDistrict, AdUnitDistrictTable.class),
                    OperationType.ADD
            );
        }

        // 加载ad_unit_keyword.data索引数据
        List<String> unitKeywords = loadDumpData(String.format(Locale.ROOT, "%s%s",
                CommonConstants.FileConstants.DATA_EXPORT_DIR,
                CommonConstants.FileConstants.AD_UNIT_KEYWORD
        ));
        for (String unitKeyword : unitKeywords) {
            AdLevelDataHandler.handleLevel4(
                    JSON.parseObject(unitKeyword, AdUnitKeywordTable.class),
                    OperationType.ADD
            );
        }
    }


    /**
     * 读取数据文件
     *
     * @param filePath 文件路径
     * @return 封装数据集合
     */
    private List<String> loadDumpData(String filePath) {
        List<String> dataList = Collections.emptyList();
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            dataList = reader.lines().collect(Collectors.toList());
            reader.close();
        } catch (IOException e) {
            log.error("load file error, filePath:{}", filePath);
            //TODO: 可以试一下抛出自定义的异常是否与@PostConstruct冲突
            //throw new AdException(CommonError.READ_FILE_DATA_ERROR);
            throw new RuntimeException("read index file error");
        }
        return dataList;
    }
}
