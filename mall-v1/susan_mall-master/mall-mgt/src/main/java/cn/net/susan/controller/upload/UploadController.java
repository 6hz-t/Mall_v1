package cn.net.susan.controller.upload;

import cn.net.susan.annotation.NoLogin;
import cn.net.susan.dto.FileDTO;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.service.upload.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import static cn.net.susan.util.QiNiuUtil.FILE;
import static cn.net.susan.util.QiNiuUtil.IMAGE;


@Slf4j
@Api(tags = "上传文件", description = "上传文件")
@RestController
@RequestMapping("/v1")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "批量上传图片接口")
    @PostMapping(value = "/image/batchUpload")
    public FileDTO batchUpload(@RequestParam("file") MultipartFile[] files) {
        try {
            return uploadService.batchUpload(files);
        } catch (Exception e) {
            log.info("批量上传图片失败，原因：", e);
            throw new BusinessException("批量上传图片失败，请稍后重试");
        }
    }

    @NoLogin
    @ApiOperation(value = "上传文件接口")
    @PostMapping(value = "/file/upload")
    public FileDTO fileUpload(MultipartFile file) throws Exception {
        return uploadService.upload(file, FILE, null);
    }

    @NoLogin
    @ApiOperation(value = "上传图片接口")
    @PostMapping(value = "/image/upload")
    public FileDTO imageUpload(MultipartFile file) throws Exception {
        return uploadService.upload(file, IMAGE, null);
    }
}
