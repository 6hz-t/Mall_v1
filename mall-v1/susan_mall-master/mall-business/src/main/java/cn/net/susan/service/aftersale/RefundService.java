package cn.net.susan.service.aftersale;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.net.susan.entity.aftersale.RefundPhotoEntity;
import cn.net.susan.entity.aftersale.web.RefundConditionWebEntity;
import cn.net.susan.entity.aftersale.web.RefundWebEntity;
import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.order.TradeConditionEntity;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.entity.order.TradeItemEntity;
import cn.net.susan.entity.order.web.TradeWebEntity;
import cn.net.susan.enums.OrderStatusEnum;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.helper.IdGenerateHelper;
import cn.net.susan.mapper.aftersale.RefundPhotoMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.service.order.TradeItemService;
import cn.net.susan.service.order.TradeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.aftersale.RefundMapper;
import cn.net.susan.entity.aftersale.RefundConditionEntity;
import cn.net.susan.entity.aftersale.RefundEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class RefundService extends BaseService<RefundEntity, RefundConditionEntity> {

    @Autowired
    private RefundMapper refundMapper;
    @Autowired
    private RefundPhotoMapper refundPhotoMapper;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private TradeItemService tradeItemService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private IdGenerateHelper idGenerateHelper;

    /**
     * 根据条件查询当前用户的退货列表
     *
     * @param refundConditionWebEntity 条件
     * @return 退货列表
     */
    public ResponsePageEntity<RefundWebEntity> searchUserRefundByPage(RefundConditionWebEntity refundConditionWebEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        AssertUtil.notNull(currentUserInfo, "请先登录");

        RefundConditionEntity refundConditionEntity = new RefundConditionEntity();
        BeanUtil.copyProperties(refundConditionWebEntity, refundConditionEntity, false);

        ResponsePageEntity<RefundEntity> responsePageEntity = super.searchByPage(refundConditionEntity);
        if (CollectionUtils.isEmpty(responsePageEntity.getData())) {
            return ResponsePageEntity.buildEmpty(refundConditionWebEntity);
        }

        List<RefundWebEntity> refundWebEntities = BeanUtil.copyToList(responsePageEntity.getData(), RefundWebEntity.class);
        return ResponsePageEntity.build(refundConditionWebEntity, responsePageEntity.getTotalCount(), refundWebEntities);
    }

    /**
     * 退货退款
     *
     * @param refundWebEntity 参数
     */
    public void doRefund(RefundWebEntity refundWebEntity) {
        TradeEntity tradeEntity = tradeService.findByCode(refundWebEntity.getTradeCode());
        AssertUtil.notNull(tradeEntity, "该订单不存在");

        TradeItemEntity tradeItemEntity = tradeItemService.findByTradeCodeAndId(refundWebEntity.getTradeCode(), refundWebEntity.getTradeItemId());
        AssertUtil.notNull(tradeEntity, "该订单明细不存在");

        RefundConditionEntity refundConditionEntity = new RefundConditionEntity();
        refundConditionEntity.setTradeId(tradeEntity.getId());
        refundConditionEntity.setProductId(tradeItemEntity.getProductId());
        List<RefundEntity> refundEntities = refundMapper.searchByCondition(refundConditionEntity);
        if (CollectionUtils.isNotEmpty(refundEntities)) {
            throw new BusinessException("该订单的商品已经退货退款，请勿重复操作");
        }

        transactionTemplate.execute((status -> {
            RefundEntity refundEntity = convertRefundEntity(refundWebEntity, tradeEntity, tradeItemEntity);
            refundMapper.insert(refundEntity);

            List<RefundPhotoEntity> refundPhotoEntities = convertRefundPhotoEntity(refundEntity, refundWebEntity);
            if (CollectionUtils.isNotEmpty(refundPhotoEntities)) {
                refundPhotoMapper.batchInsert(refundPhotoEntities);
            }
            return Boolean.TRUE;
        }));

        tradeEntity.setOrderStatus(OrderStatusEnum.REJECT.getValue());
        FillUserUtil.fillUpdateUserInfo(tradeEntity);
        tradeService.update(tradeEntity);
    }


    private RefundEntity convertRefundEntity(RefundWebEntity refundWebEntity, TradeEntity tradeEntity, TradeItemEntity tradeItemEntity) {
        RefundEntity refundEntity = new RefundEntity();
        refundEntity.setId(idGenerateHelper.nextId());
        refundEntity.setTradeId(tradeEntity.getId());
        refundEntity.setTradeCode(tradeEntity.getCode());
        refundEntity.setProductId(tradeItemEntity.getProductId());
        refundEntity.setName(tradeItemEntity.getProductName());
        refundEntity.setModel(tradeItemEntity.getModel());
        refundEntity.setCoverUrl(tradeItemEntity.getCoverUrl());
        refundEntity.setQuantity(tradeItemEntity.getQuantity());
        refundEntity.setTotalAmount(tradeItemEntity.getAmount());
        refundEntity.setRefundAmount(tradeItemEntity.getAmount());
        refundEntity.setAuditStatus(10);
        refundEntity.setRefundStatus(10);
        refundEntity.setContent(refundWebEntity.getContent());
        return refundEntity;
    }

    private List<RefundPhotoEntity> convertRefundPhotoEntity(RefundEntity refundEntity, RefundWebEntity refundWebEntity) {
        List<RefundPhotoEntity> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(refundWebEntity.getImages())) {
            for (String url : refundWebEntity.getImages()) {
                RefundPhotoEntity refundPhotoEntity = new RefundPhotoEntity();
                refundPhotoEntity.setId(idGenerateHelper.nextId());
                FillUserUtil.fillCreateUserInfo(refundPhotoEntity);
                refundPhotoEntity.setRefundId(refundEntity.getId());
                refundPhotoEntity.setUrl(url);
                refundPhotoEntity.setName(url.substring(url.lastIndexOf('/') + 1));
                result.add(refundPhotoEntity);
            }
        }
        return result;
    }


    /**
     * 查询退货单信息
     *
     * @param id 退货单ID
     * @return 退货单信息
     */
    public RefundEntity findById(Long id) {
        return refundMapper.findById(id);
    }

    /**
     * 根据条件分页查询退货单列表
     *
     * @param refundConditionEntity 退货单信息
     * @return 退货单集合
     */
    public ResponsePageEntity<RefundEntity> searchByPage(RefundConditionEntity refundConditionEntity) {
        return super.searchByPage(refundConditionEntity);
    }

    /**
     * 新增退货单
     *
     * @param refundEntity 退货单信息
     * @return 结果
     */
    public int insert(RefundEntity refundEntity) {
        return refundMapper.insert(refundEntity);
    }

    /**
     * 修改退货单
     *
     * @param refundEntity 退货单信息
     * @return 结果
     */
    public int update(RefundEntity refundEntity) {
        return refundMapper.update(refundEntity);
    }

    /**
     * 批量删除退货单
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<RefundEntity> entities = refundMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "退货单已被删除");

        RefundEntity entity = new RefundEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return refundMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return refundMapper;
    }
}
