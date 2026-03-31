package cn.net.susan.util;


public class SpringBeanUtil {

    /**
     * 根据名称获取bean实例
     *
     * @param name 名称
     * @param <T>  泛型
     * @return bean实例
     */
    public static <T> T getBean(String name) {
        return (T) SpringUtil.getBean(name);
    }

    /**
     * 根据类型获取bean实例
     *
     * @param requiredType 类型
     * @param <T>          泛型
     * @return bean实例
     */
    public static <T> T getBean(Class<T> requiredType) {
        return SpringUtil.getBean(requiredType);
    }
}
