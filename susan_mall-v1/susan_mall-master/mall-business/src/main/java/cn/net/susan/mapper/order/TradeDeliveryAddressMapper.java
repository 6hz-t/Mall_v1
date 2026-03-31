package cn.net.susan.mapper.order;

import cn.net.susan.entity.order.TradeDeliveryAddressConditionEntity;
import cn.net.susan.entity.order.TradeDeliveryAddressEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface TradeDeliveryAddressMapper extends BaseMapper<TradeDeliveryAddressEntity, TradeDeliveryAddressConditionEntity> {
	/**
     * 查询订单收货地址信息
     *
     * @param id 订单收货地址ID
     * @return 订单收货地址信息
     */
	TradeDeliveryAddressEntity findById(Long id);

	/**
     * 添加订单收货地址
     *
     * @param tradeDeliveryAddressEntity 订单收货地址信息
     * @return 结果
     */
	int insert(TradeDeliveryAddressEntity tradeDeliveryAddressEntity);

	/**
     * 修改订单收货地址
     *
     * @param tradeDeliveryAddressEntity 订单收货地址信息
     * @return 结果
     */
	int update(TradeDeliveryAddressEntity tradeDeliveryAddressEntity);

    /**
     * 批量删除订单收货地址
     *
     * @param ids id集合
     * @param entity 订单收货地址实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") TradeDeliveryAddressEntity entity);

    /**
     * 批量查询订单收货地址信息
     *
     * @param ids ID集合
     * @return 订单收货地址信息
    */
    List<TradeDeliveryAddressEntity> findByIds(List<Long> ids);
}
