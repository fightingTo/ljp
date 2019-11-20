package com.cheer.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用索引服务实现类获取(缓存)
 * 目的: 避免多次注入（@Autowired, @Resource...）
 *
 * @Created by ljp on 2019/11/20.
 */
public class DataTable implements ApplicationContextAware, PriorityOrdered {
    /**
     * 保存所有的index索引服务(plan, unit, creative...)
     * key: 索引服务类型
     * value: 具体的索引类
     */
    private static final Map<Class, Object> dataTableMap = new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;

    /**
     * 获取所有索引服务
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T> T of(Class<T> clazz) {
        T instance = (T) dataTableMap.get(clazz);

        // 如果应该存在，则直接返回（只初始化一次）
        if (instance != null) {
            return instance;
        }
        dataTableMap.put(clazz, getBean(clazz));
        return (T) dataTableMap.get(clazz);
    }


    @Override
    @SuppressWarnings("all")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }

    /**
     * 应用中bean优先级排序: 此类应在索引服务初始化前加载
     *
     * @return 排序
     */
    @Override
    public int getOrder() {
        // 最高优先级
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    /**
     * 根据bean的名称获取Spring管理的bean对象
     */
    @SuppressWarnings("all")
    private static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }
    /**
     * 根据bean的字节码类型获取Spring管理的bean对象
     */
    @SuppressWarnings("all")
    private static <T> T getBean(Class clazz) {
        return (T) applicationContext.getBean(clazz);
    }

}
