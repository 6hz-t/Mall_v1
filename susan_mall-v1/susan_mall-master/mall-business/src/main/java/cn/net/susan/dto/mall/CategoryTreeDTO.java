package cn.net.susan.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.compress.utils.Lists;

import java.util.Date;
import java.util.List;
import java.util.Objects;



@ApiModel("分类树实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryTreeDTO {

    /**
     * 系统ID
     */
    @ApiModelProperty("系统ID")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 前端页面显示用的标签名称
     */
    @ApiModelProperty("标签名称")
    private String label;

    /**
     * 父分类ID
     */
    @ApiModelProperty("父分类ID")
    private Long pid;

    /**
     * 有效状态 1:有效 0:无效
     */
    @ApiModelProperty("有效状态 1:有效 0:无效")
    private Integer validStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 是否叶子节点
     */
    @ApiModelProperty("是否叶子节点")
    private Boolean leaf;


    /**
     * 是否有下一级
     */
    @ApiModelProperty("是否有下一级")
    private Boolean hasChildren;

    /**
     * 下一级数量
     */
    @ApiModelProperty("下一级数量")
    private Integer subCount;

    /**
     * 层级
     */
    @ApiModelProperty("层级")
    private Integer level;

    /**
     * 子部门
     */
    private List<CategoryTreeDTO> children;

    /**
     * 增加添加子部门的方法
     *
     * @param deptTreeDTO 子部门
     */
    public void addChildren(CategoryTreeDTO deptTreeDTO) {
        if (Objects.isNull(children)) {
            children = Lists.newArrayList();
        }
        children.add(deptTreeDTO);
    }
}
