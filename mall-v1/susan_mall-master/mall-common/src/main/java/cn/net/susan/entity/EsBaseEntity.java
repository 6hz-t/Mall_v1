package cn.net.susan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class EsBaseEntity implements Serializable {

    /**
     * ID
     */
    private String id;

    /**
     * 数据
     */
    private Map<String, Object> data;
}
