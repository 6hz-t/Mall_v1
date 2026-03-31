package cn.net.susan.entity.mall.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndexNoticeWebEntity {

	/**
	 * 系统编号
	 */
	private Long id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 发布时间
	 */
	private String createTime;

	/**
	 * 排序
	 */
	private Integer sort;
}
