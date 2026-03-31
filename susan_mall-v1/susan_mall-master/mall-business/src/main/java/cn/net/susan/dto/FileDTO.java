package cn.net.susan.dto;

import lombok.Data;


@Data
public class FileDTO {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 下载地址
     */
    private String downloadUrl;
}
