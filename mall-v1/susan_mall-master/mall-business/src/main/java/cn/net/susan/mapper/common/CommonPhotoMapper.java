package cn.net.susan.mapper.common;

import cn.net.susan.entity.common.CommonPhotoConditionEntity;
import cn.net.susan.entity.common.CommonPhotoEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CommonPhotoMapper extends BaseMapper<CommonPhotoEntity, CommonPhotoConditionEntity> {
	/**
     * 查询图片信息
     *
     * @param id 图片ID
     * @return 图片信息
     */
	CommonPhotoEntity findById(Long id);

	/**
     * 添加图片
     *
     * @param commonPhotoEntity 图片信息
     * @return 结果
     */
	int insert(CommonPhotoEntity commonPhotoEntity);

	/**
     * 修改图片
     *
     * @param commonPhotoEntity 图片信息
     * @return 结果
     */
	int update(CommonPhotoEntity commonPhotoEntity);

	/**
     * 批量删除图片
     *
     * @param ids id集合
     * @param entity 图片实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CommonPhotoEntity entity);

	/**
     * 批量查询图片信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<CommonPhotoEntity> findByIds(List<Long> ids);
}
