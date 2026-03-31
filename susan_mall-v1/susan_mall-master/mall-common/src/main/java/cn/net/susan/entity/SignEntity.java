package cn.net.susan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignEntity implements Serializable {

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 签名
     */
    private String sign;
}
