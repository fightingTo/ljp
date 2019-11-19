package com.cheer.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cheer.ad.service.ICreativeService;
import com.cheer.ad.vo.CreativeRequest;
import com.cheer.ad.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创意
 *
 * @Created by ljp on 2019/11/19.
 */
@Slf4j
@RestController
public class CreativeController {

    @Autowired
    private ICreativeService creativeService;

    @PostMapping("/create/creative")
    public CreativeResponse createCreative(@RequestBody CreativeRequest request) {
        log.info("ad-sponsor: createCreative -> {}", JSON.toJSONString(request));
        return creativeService.createCreative(request);
    }
}
