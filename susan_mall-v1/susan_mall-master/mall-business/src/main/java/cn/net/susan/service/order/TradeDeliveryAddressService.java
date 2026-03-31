package cn.net.susan.service.order;

import java.util.List;

import cn.net.susan.service.BaseService;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.order.TradeDeliveryAddressMapper;
import cn.net.susan.entity.order.TradeDeliveryAddressConditionEntity;
import cn.net.susan.entity.order.TradeDeliveryAddressEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;


@Service
public class TradeDeliveryAddressService extends BaseService<TradeDeliveryAddressEntity, TradeDeliveryAddressConditionEntity> {

    @Autowired
    private TradeDeliveryAddressMapper tradeDeliveryAddressMapper;

    /**
     * 查询订单收货地址信息
     *
     * @param tradeId 订单ID
     * @return 订单收货地址信息
     */
    @DS("sharding")
    public List<TradeDeliveryAddressEntity> findByTradeId(Long tradeId) {
        TradeDeliveryAddressConditionEntity tradeDeliveryAddressConditionEntity = new TradeDeliveryAddressConditionEntity();
        tradeDeliveryAddressConditionEntity.setTradeId(tradeId);
        return tradeDeliveryAddressMapper.searchByCondition(tradeDeliveryAddressConditionEntity);
    }

    /**
     * 查询订单收货地址信息
     *
     * @param id 订单收货地址ID
     * @return 订单收货地址信息
     */
    @DS("sharding")
    public TradeDeliveryAddressEntity findById(Long id) {
        return tradeDeliveryAddressMapper.findById(id);
    }

    /**
     * 根据条件分页查询订单收货地址列表
     *
     * @param tradeDeliveryAddressConditionEntity 订单收货地址信息
     * @return 订单收货地址集合
     */
    @DS("sharding")
    public ResponsePageEntity<TradeDeliveryAddressEntity> searchByPage(TradeDeliveryAddressConditionEntity tradeDeliveryAddressConditionEntity) {
        return super.searchByPage(tradeDeliveryAddressConditionEntity);
    }

    /**
     * 新增订单收货地址
     *
     * @param tradeDeliveryAddressEntity 订单收货地址信息
     * @return 结果
     */
    @DS("sharding")
    public int insert(TradeDeliveryAddressEntity tradeDeliveryAddressEntity) {
        return tradeDeliveryAddressMapper.insert(tradeDeliveryAddressEntity);
    }

    /**
     * 修改订单收货地址
     *
     * @param tradeDeliveryAddressEntity 订单收货地址信息
     * @return 结果
     */
    @DS("sharding")
    public int update(TradeDeliveryAddressEntity tradeDeliveryAddressEntity) {
        return tradeDeliveryAddressMapper.update(tradeDeliveryAddressEntity);
    }

    /**
     * 批量删除订单收货地址
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    @DS("sharding")
    public int deleteByIds(List<Long> ids) {
        List<TradeDeliveryAddressEntity> entities = tradeDeliveryAddressMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "订单收货地址已被删除");

        TradeDeliveryAddressEntity entity = new TradeDeliveryAddressEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return tradeDeliveryAddressMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return tradeDeliveryAddressMapper;
    }
}
