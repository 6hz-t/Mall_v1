package cn.net.susan.util;

import cn.net.susan.exception.BusinessException;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.PutObjectOptions;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static io.minio.GetPresignedObjectUrlArgs.DEFAULT_EXPIRY_TIME;


@Slf4j
@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean bucketExists(String bucketName) throws Exception {
        boolean flag = false;
        flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (flag) {
            return true;
        }
        return false;
    }


    /**
     * 文件上传
     *
     * @param bucketName    存储捅名称
     * @param multipartFile 文件
     * @param filename      文件名
     */
    public void putObject(String bucketName, MultipartFile multipartFile, String filename) throws Exception {
        PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
        putObjectOptions.setContentType(multipartFile.getContentType());
        
        // 使用 try-with-resources 确保输入流被正确关闭，避免临时文件删除失败
        try (InputStream inputStream = multipartFile.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(filename).stream(
                            inputStream, multipartFile.getSize(), -1).contentType(multipartFile.getContentType())
                            .build());
        }
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return String
     */
    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            try {
                url = minioClient.getObjectUrl(bucketName, objectName);
            } catch (Exception e) {
                log.error("getObjectUrl出现异常：", e);
            }
        }
        return url;
    }


    /**
     * 生成一个给HTTP GET请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) throws Exception {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                throw new BusinessException("过期时间不合法");
            }
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expires)
                    .build());
        }
        return url;
    }

}
