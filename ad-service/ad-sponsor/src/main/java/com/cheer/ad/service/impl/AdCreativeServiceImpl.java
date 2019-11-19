package com.cheer.ad.service.impl;

import com.cheer.ad.dao.CreativeRepository;
import com.cheer.ad.entity.Creative;
import com.cheer.ad.service.ICreativeService;
import com.cheer.ad.vo.CreativeRequest;
import com.cheer.ad.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创意服务功能实现
 *
 * @Created by ljp on 2019/11/19.
 */
@Slf4j
@Service
public class AdCreativeServiceImpl implements ICreativeService {

    @Autowired
    private CreativeRepository creativeRepository;

    @Override
    public CreativeResponse createCreative(CreativeRequest request) {
        Creative creative = request.convertToEntity();
        Creative saveCreative = creativeRepository.save(creative);
        return new CreativeResponse(saveCreative.getId(), saveCreative.getName());
    }
}
