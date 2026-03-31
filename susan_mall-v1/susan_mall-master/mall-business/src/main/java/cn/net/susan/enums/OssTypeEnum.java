package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum OssTypeEnum {

    /**
     * Minio
     */
    MINIO(1, "Minio"),

    /**
     * 七牛云
     */
    QINIU(2, "七牛云");


    private Integer value;

    private String desc;
}
