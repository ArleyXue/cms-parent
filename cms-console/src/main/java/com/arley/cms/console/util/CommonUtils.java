package com.arley.cms.console.util;

import java.util.Collection;

/**
 * @author XueXianlei
 * @Description: 常用工具类
 * @date 2018/9/23 15:00
 */
public class CommonUtils {

    /**
     * 判断集合非空
     * @param collection
     * @return
     */
    public static boolean isNotEmptyCollection (Collection collection) {
        return null != collection && collection.size() > 0;
    }
}
