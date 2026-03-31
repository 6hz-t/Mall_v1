package cn.net.susan.entity.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@ApiModel("秒杀商品详情实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeckillProductDetailEntity extends ESSeckillProductEntity {

    /**
     * 商品详情
     */
    @ApiModelProperty("商品详情")
    private String detail;

    /**
     * 轮播图
     */
    private List<String> swiper;
}
