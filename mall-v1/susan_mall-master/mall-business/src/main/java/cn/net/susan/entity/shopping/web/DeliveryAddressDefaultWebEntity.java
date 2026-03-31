package cn.net.susan.entity.shopping.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveryAddressDefaultWebEntity {

    /**
     * 系统ID
     */
    @NotNull(message = "系统ID不能为空")
    private Long id;
}
