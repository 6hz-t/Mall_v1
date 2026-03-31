package cn.net.susan.mapper.order;

import cn.net.susan.entity.order.TradeConditionEntity;
import cn.net.susan.entity.order.TradeEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface TradeMapper extends BaseMapper<TradeEntity, TradeConditionEntity> {
    /**
     * 查询订单信息
     *
     * @param id 订单ID
     * @return 订单信息
     */
    TradeEntity findById(Long id);

    /**
     * 添加订单
     *
     * @param tradeEntity 订单信息
     * @return 结果
     */
    int insert(TradeEntity tradeEntity);

    /**
     * 修改订单
     *
     * @param tradeEntity 订单信息
     * @return 结果
     */
    int update(TradeEntity tradeEntity);

    /**
     * 批量删除订单
     *
     * @param ids    id集合
     * @param entity 订单实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") TradeEntity entity);

    /**
     * 批量更新订单状态
     *
     * @param ids         id集合
     * @param entity      订单实体
     * @param orderStatus 订单状态
     * @return 结果
     */
    int updateOrderStatusByIds(@Param("ids") List<Long> ids, @Param("entity") TradeEntity entity, @Param("orderStatus") Integer orderStatus);

    /**
     * 批量查询订单信息
     *
     * @param ids ID集合
     * @return 部门信息
     */
    List<TradeEntity> findByIds(List<Long> ids);
}
