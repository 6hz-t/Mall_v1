package cn.net.susan.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonAreaEntity extends BaseEntity {


	/**
	 * 上一级ID
	 */
	private Long parentId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 拼音
	 */
	private String pinyin;

	/**
	 * 全称
	 */
	private String fullName;

	/**
	 * 行政编码
	 */
	private String code;

	/**
	 * 级别
	 */
	private Integer level;
}
