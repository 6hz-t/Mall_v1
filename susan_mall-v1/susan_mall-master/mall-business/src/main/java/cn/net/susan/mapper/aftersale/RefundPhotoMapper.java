package cn.net.susan.mapper.aftersale;

import cn.net.susan.entity.aftersale.RefundPhotoConditionEntity;
import cn.net.susan.entity.aftersale.RefundPhotoEntity;
import java.util.List;

import cn.net.susan.entity.common.CommonSensitiveWordEntity;
import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface RefundPhotoMapper extends BaseMapper<RefundPhotoEntity, RefundPhotoConditionEntity> {
	/**
     * 查询退货单图片信息
     *
     * @param id 退货单图片ID
     * @return 退货单图片信息
     */
	RefundPhotoEntity findById(Long id);

	/**
     * 添加退货单图片
     *
     * @param refundPhotoEntity 退货单图片信息
     * @return 结果
     */
	int insert(RefundPhotoEntity refundPhotoEntity);

	/**
     * 修改退货单图片
     *
     * @param refundPhotoEntity 退货单图片信息
     * @return 结果
     */
	int update(RefundPhotoEntity refundPhotoEntity);

    /**
     * 批量删除退货单图片
     *
     * @param ids id集合
     * @param entity 退货单图片实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") RefundPhotoEntity entity);

    /**
     * 批量查询退货单图片信息
     *
     * @param ids ID集合
     * @return 退货单图片信息
    */
    List<RefundPhotoEntity> findByIds(List<Long> ids);

	/**
	 * 批量添加退货凭证
	 *
	 * @param list 退货凭证
	 * @return 结果
	 */
	int batchInsert(List<RefundPhotoEntity> list);

}
