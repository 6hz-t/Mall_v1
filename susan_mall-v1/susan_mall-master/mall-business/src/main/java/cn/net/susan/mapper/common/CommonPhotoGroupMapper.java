package cn.net.susan.mapper.common;

import cn.net.susan.entity.common.CommonPhotoGroupConditionEntity;
import cn.net.susan.entity.common.CommonPhotoGroupEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CommonPhotoGroupMapper extends BaseMapper<CommonPhotoGroupEntity, CommonPhotoGroupConditionEntity> {
	/**
     * 查询图片分组信息
     *
     * @param id 图片分组ID
     * @return 图片分组信息
     */
	CommonPhotoGroupEntity findById(Long id);

	/**
     * 添加图片分组
     *
     * @param commonPhotoGroupEntity 图片分组信息
     * @return 结果
     */
	int insert(CommonPhotoGroupEntity commonPhotoGroupEntity);

	/**
     * 修改图片分组
     *
     * @param commonPhotoGroupEntity 图片分组信息
     * @return 结果
     */
	int update(CommonPhotoGroupEntity commonPhotoGroupEntity);

	/**
     * 批量删除图片分组
     *
     * @param ids id集合
     * @param entity 图片分组实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CommonPhotoGroupEntity entity);

	/**
     * 批量查询图片分组信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<CommonPhotoGroupEntity> findByIds(List<Long> ids);
}
