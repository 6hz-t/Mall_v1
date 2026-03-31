package cn.net.susan.entity.shopping;

import cn.net.susan.entity.mall.UserProductEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@Data
public class ProductViewRecordEntity extends UserProductEntity {

    /**
     * 访问次数
     */
    @ApiModelProperty("访问次数")
    private int viewCount;
}
