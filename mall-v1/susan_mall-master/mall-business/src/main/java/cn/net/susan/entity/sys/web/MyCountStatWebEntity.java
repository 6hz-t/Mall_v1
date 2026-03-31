package cn.net.susan.entity.sys.web;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel("我的数量统计web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyCountStatWebEntity {

    /**
     * 我的收藏数量
     */
    private int favoritesCount;

    /**
     * 我的消息数量
     */
    private int messageCount;

    /**
     * 我的足迹数量
     */
    private int viewRecordCount;
}
