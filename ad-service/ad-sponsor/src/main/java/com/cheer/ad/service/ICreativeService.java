package com.cheer.ad.service;

import com.cheer.ad.vo.CreativeRequest;
import com.cheer.ad.vo.CreativeResponse;

/**
 * 广告创意接口定义
 *
 * @Created by ljp on 2019/11/14.
 */
public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request);
}
