package cn.net.susan.service;

import cn.hutool.core.util.ArrayUtil;
import cn.net.susan.dto.FileDTO;
import cn.net.susan.entity.RequestConditionEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.service.upload.UploadService;
import cn.net.susan.util.BetweenTimeUtil;
import cn.net.susan.util.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.net.susan.util.ExcelUtil.TEMP_FILE_PATH;
import static cn.net.susan.util.QiNiuUtil.FILE;


@Slf4j
public abstract class BaseService<K, V> {
    private static final int MAX_EXCEL_FILE_SIZE = 10 * 1024 * 1024;

    @Value("${mall.mgt.exportPageSize:2}")
    private int exportPageSize;

    @Value("${mall.mgt.sheetDataSize:4}")
    private int sheetDataSize;
    @Autowired
    private UploadService uploadService;

    /**
     * 获取BaseMapper
     *
     * @return BaseMapper
     */
    protected abstract BaseMapper getBaseMapper();


    /**
     * 用户自定义导出逻辑
     *
     * @param v 查询条件
     * @return 是否自定义
     */
    public boolean customizeExport(V v) {
        return false;
    }

    /**
     * 通用的分页接口
     *
     * @return 数据
     * @param s   分页请求参数
     * @param <T> 分页返回实体
     */
    public <S extends RequestConditionEntity, T extends Object> ResponsePageEntity<T> searchByPage(S s) {
        BetweenTimeUtil.parseTime(s);
        int count = getBaseMapper().searchCount(s);
        if (count == 0) {
            return ResponsePageEntity.buildEmpty(s);
        }
        List<T> dataList = getBaseMapper().searchByCondition(s);
        return ResponsePageEntity.build(s, count, dataList);
    }

    /**
     * 公共excel导出方法
     *
     * @param v         查询条件
     * @param fileName  文件名称
     * @param clazzName 实体类名称
     * @IOException 异常
     */
    public String export(V v, String fileName, String clazzName) {
        if (customizeExport(v)) {
            return null;
        }

        return doExport(v, fileName, clazzName);
    }

    private String doExport(V v, String fileName, String clazzName) {
        RequestConditionEntity conditionEntity = (RequestConditionEntity) v;
        BetweenTimeUtil.parseTime(conditionEntity);
        //数据总条数
        int totalCount = getBaseMapper().searchCount(conditionEntity);
        //有多少个sheet页
        int sheetCount = totalCount % sheetDataSize == 0 ? totalCount / sheetDataSize : totalCount / sheetDataSize + 1;
        //每个sheet页需要循环的次数
        int loopCount = sheetDataSize / exportPageSize;

        String downloadName = TEMP_FILE_PATH + fileName + ".xlsx";
        File file = new File(downloadName);
        List<List<String>> heads = customizeHeader(conditionEntity);
        ExcelWriter excelWriter = EasyExcel.write(file).build();

        conditionEntity.setPageNo(1);
        conditionEntity.setPageSize(exportPageSize);
        for (int sheetIndex = 1; sheetIndex <= sheetCount; sheetIndex++) {
            List<K> dataEntities = getBaseMapper().searchByCondition(conditionEntity);
            int count = 1;
            while (CollectionUtils.isNotEmpty(dataEntities) && count <= loopCount) {
                WriteSheet writeSheet;
                //自定义excel表头
                if (CollectionUtils.isNotEmpty(heads)) {
                    writeSheet = EasyExcel.writerSheet("Sheet" + sheetIndex).head(heads).build();
                    List<List<Object>> customizeDataList = handleCustomizeData(dataEntities, heads);
                    excelWriter.write(customizeDataList, writeSheet);
                } else {
                    try {
                        writeSheet = EasyExcel.writerSheet("Sheet" + sheetIndex).head(Class.forName(clazzName)).build();
                        excelWriter.write(dataEntities, writeSheet);
                    } catch (ClassNotFoundException e) {
                        log.error("数据导出异常，没有找到:{}", clazzName);
                        throw new BusinessException(String.format("数据导出异常，没有找到:%s", clazzName));
                    }
                }

                conditionEntity.setPageNo(conditionEntity.getPageNo() + 1);
                dataEntities = getBaseMapper().searchByCondition(conditionEntity);

                count++;
            }

        }

        excelWriter.finish();

        try {
            MultipartFile multipartFile = FileUtil.toMultipartFile(fileName, file);
            FileDTO fileDTO = uploadService.upload(multipartFile, FILE, "application/vnd.ms-excel");
            return fileDTO.getDownloadUrl();
        } catch (Exception e) {
            log.info("上传excel文件到oss服务器失败，原因：{}", e);
        }
        return downloadName;
    }


    private List<List<String>> customizeHeader(RequestConditionEntity conditionEntity) {
        List<String> customizeColumnNameList = conditionEntity.getCustomizeColumnNameList();
        if (CollectionUtils.isEmpty(customizeColumnNameList)) {
            return Collections.emptyList();
        }

        return conditionEntity.getCustomizeColumnNameList().stream().map(x -> Lists.newArrayList(x))
                .collect(Collectors.toList());
    }


    private List<List<Object>> handleCustomizeData(List<K> dataEntities, List<List<String>> heads) {
        if (CollectionUtils.isEmpty(dataEntities)) {
            return Collections.emptyList();
        }

        List<List<Object>> customizeDataList = Lists.newArrayList();
        for (K dataEntity : dataEntities) {
            List<Object> rowDataList = Lists.newArrayList();
            for (List<String> head : heads) {
                String columnName = head.get(0);
                Field[] fields = dataEntity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    String fileName;
                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    if (Objects.nonNull(excelProperty) || ArrayUtil.isEmpty(excelProperty.value())) {
                        fileName = excelProperty.value()[0];
                    } else {
                        fileName = field.getName();
                    }
                    if (!fileName.equals(columnName)) {
                        continue;
                    }

                    field.setAccessible(true);
                    try {

                        Object fieldValue = field.get(dataEntity);
                        rowDataList.add(fieldValue);
                    } catch (IllegalAccessException e) {
                        log.error("反射获取字段出现异常，原因：", e);
                        throw new BusinessException("反射获取字段出现异常");
                    }
                }
            }
            customizeDataList.add(rowDataList);
        }
        return customizeDataList;
    }
}
