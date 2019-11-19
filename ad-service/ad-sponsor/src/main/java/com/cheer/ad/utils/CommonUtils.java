package com.cheer.ad.utils;

import com.cheer.ad.exception.AdException;
import com.cheer.ad.vo.CommonError;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * 投放工程通用工具类
 *
 * @Created by ljp on 2019/11/14.
 */
public class CommonUtils {

    private final static String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
    };

    /**
     * md5加盐
     * @param value
     * @return
     */
    public static String md5(String value) {
        return DigestUtils.md5Hex(value).toUpperCase();
    }

    /**
     * 将字符串转化为特定格式的日期格式
     * @param dateString
     * @return
     * @throws AdException
     */
    public static Date parseStringDate(String dateString) throws AdException {
        try {
            return DateUtils.parseDate(dateString, PARSE_PATTERNS);
        } catch (Exception ex) {
            throw new AdException(CommonError.PARSER_DATE_ERROR);
        }
    }
}
