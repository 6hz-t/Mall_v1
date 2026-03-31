package cn.net.susan.service.order;

import java.util.List;

import cn.net.susan.service.BaseService;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.order.TradeItemMapper;
import cn.net.susan.entity.order.TradeItemConditionEntity;
import cn.net.susan.entity.order.TradeItemEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;
import org.springframework.util.CollectionUtils;


@Service
public class TradeItemService extends BaseService<TradeItemEntity, TradeItemConditionEntity> {

    @Autowired
    private TradeItemMapper tradeItemMapper;

    /**
     * 根据订单ID集合批量查询订单明细信息
     *
     * @param tradeIdList 订单ID集合
     * @return 订单明细信息
     */
    @DS("sharding")
    public List<TradeItemEntity> findByTradeIdList(List<Long> tradeIdList) {
        TradeItemConditionEntity tradeItemConditionEntity = new TradeItemConditionEntity();
        tradeItemConditionEntity.setTradeIdList(tradeIdList);
        tradeItemConditionEntity.setPageSize(0);
        return tradeItemMapper.searchByCondition(tradeItemConditionEntity);
    }

    /**
     * 查询订单明细信息
     *
     * @param id 订单明细ID
     * @return 订单明细信息
     */
    @DS("sharding")
    public TradeItemEntity findById(Long id) {
        return tradeItemMapper.findById(id);
    }

    /**
     * 根据条件分页查询订单明细列表
     *
     * @param tradeItemConditionEntity 订单明细信息
     * @return 订单明细集合
     */
    @DS("sharding")
    public ResponsePageEntity<TradeItemEntity> searchByPage(TradeItemConditionEntity tradeItemConditionEntity) {
        return super.searchByPage(tradeItemConditionEntity);
    }

    /**
     * 根据订单编码和订单明细ID查询订单明细
     *
     * @param code        订单编码
     * @param tradeItemId 订单明细ID
     * @return 订单明细
     */
    @DS("sharding")
    public TradeItemEntity findByTradeCodeAndId(String code, Long tradeItemId) {
        TradeItemConditionEntity tradeItemConditionEntity = new TradeItemConditionEntity();
        tradeItemConditionEntity.setCode(code);
        tradeItemConditionEntity.setId(tradeItemId);
        List<TradeItemEntity> tradeItemEntities = tradeItemMapper.searchByCondition(tradeItemConditionEntity);
        if (CollectionUtils.isEmpty(tradeItemEntities)) {
            return null;
        }
        return tradeItemEntities.get(0);
    }

    /**
     * 统计商品的销量
     *
     * @param productId   商品ID
     * @param orderStatus 订单状态
     * @return 销量
     */
    @DS("sharding")
    public TradeItemEntity statProductSaleQuantity(Long productId, Integer orderStatus) {
        return tradeItemMapper.statProductSaleQuantity(productId, orderStatus);
    }

    /**
     * 新增订单明细
     *
     * @param tradeItemEntity 订单明细信息
     * @return 结果
     */
    @DS("sharding")
    public int insert(TradeItemEntity tradeItemEntity) {
        return tradeItemMapper.insert(tradeItemEntity);
    }

    /**
     * 修改订单明细
     *
     * @param tradeItemEntity 订单明细信息
     * @return 结果
     */
    @DS("sharding")
    public int update(TradeItemEntity tradeItemEntity) {
        return tradeItemMapper.update(tradeItemEntity);
    }

    /**
     * 批量删除订单明细对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    @DS("sharding")
    public int deleteByIds(List<Long> ids) {
        List<TradeItemEntity> entities = tradeItemMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "订单明细已被删除");

        TradeItemEntity entity = new TradeItemEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return tradeItemMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return tradeItemMapper;
    }

}
