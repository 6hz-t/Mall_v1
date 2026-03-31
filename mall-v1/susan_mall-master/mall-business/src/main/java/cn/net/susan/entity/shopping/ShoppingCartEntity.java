package cn.net.susan.entity.shopping;

import cn.net.susan.entity.mall.UserProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingCartEntity extends UserProductEntity {

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;
}
