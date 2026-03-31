package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.IndexNoticeConditionEntity;
import cn.net.susan.entity.mall.IndexNoticeEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface IndexNoticeMapper extends BaseMapper<IndexNoticeEntity, IndexNoticeConditionEntity> {
	/**
     * 查询首页公告信息
     *
     * @param id 首页公告ID
     * @return 首页公告信息
     */
	IndexNoticeEntity findById(Long id);

	/**
     * 添加首页公告
     *
     * @param indexNoticeEntity 首页公告信息
     * @return 结果
     */
	int insert(IndexNoticeEntity indexNoticeEntity);

	/**
     * 修改首页公告
     *
     * @param indexNoticeEntity 首页公告信息
     * @return 结果
     */
	int update(IndexNoticeEntity indexNoticeEntity);

    /**
     * 批量删除首页公告
     *
     * @param ids id集合
     * @param entity 首页公告实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") IndexNoticeEntity entity);

    /**
     * 批量查询首页公告信息
     *
     * @param ids ID集合
     * @return 首页公告信息
    */
    List<IndexNoticeEntity> findByIds(List<Long> ids);
}
