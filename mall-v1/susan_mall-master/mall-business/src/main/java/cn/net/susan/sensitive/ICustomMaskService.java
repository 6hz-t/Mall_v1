package cn.net.susan.sensitive;


public interface ICustomMaskService {
    /**
     * 脱敏方法
     *
     * @param data 数据
     * @return 脱敏后的数据
     */
    String maskData(String data);
}
