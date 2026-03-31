package cn.net.susan.entity.mall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndexNoticeEntity extends BaseEntity {


	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 排序
	 */
	private Integer sort;
}
