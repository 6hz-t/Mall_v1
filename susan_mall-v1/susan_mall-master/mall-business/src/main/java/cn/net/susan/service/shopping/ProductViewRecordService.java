package cn.net.susan.service.shopping;

import java.util.List;

import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.helper.UserProductHelper;
import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.shopping.ProductViewRecordMapper;
import cn.net.susan.entity.shopping.ProductViewRecordConditionEntity;
import cn.net.susan.entity.shopping.ProductViewRecordEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;

import static cn.net.susan.util.FillUserUtil.getCurrentUserInfo;


@Service
public class ProductViewRecordService extends BaseService<ProductViewRecordEntity, ProductViewRecordConditionEntity> {

    @Autowired
    private ProductViewRecordMapper productViewRecordMapper;
    @Autowired
    private UserProductHelper userProductHelper;

    /**
     * 查询商品浏览记录信息
     *
     * @param id 商品浏览记录ID
     * @return 商品浏览记录信息
     */
    public ProductViewRecordEntity findById(Long id) {
        return productViewRecordMapper.findById(id);
    }

    /**
     * 根据条件分页查询商品浏览记录列表
     *
     * @param productViewRecordConditionEntity 商品浏览记录信息
     * @return 商品浏览记录集合
     */
    public ResponsePageEntity<ProductViewRecordEntity> searchByPage(ProductViewRecordConditionEntity productViewRecordConditionEntity) {
        ResponsePageEntity<ProductViewRecordEntity> responsePageEntity = super.searchByPage(productViewRecordConditionEntity);
        userProductHelper.fillUserProductInfo(responsePageEntity.getData());
        return responsePageEntity;
    }

    /**
     * 新增商品浏览记录
     *
     * @param productViewRecordEntity 商品浏览记录信息
     * @return 结果
     */
    public int insert(ProductViewRecordEntity productViewRecordEntity) {
        userProductHelper.checkParam(productViewRecordEntity);
        return productViewRecordMapper.insert(productViewRecordEntity);
    }

    /**
     * 修改商品浏览记录
     *
     * @param productViewRecordEntity 商品浏览记录信息
     * @return 结果
     */
    public int update(ProductViewRecordEntity productViewRecordEntity) {
        userProductHelper.checkParam(productViewRecordEntity);
        FillUserUtil.fillUpdateUserInfo(productViewRecordEntity);
        return productViewRecordMapper.update(productViewRecordEntity);
    }

    /**
     * 批量删除商品浏览记录
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<ProductViewRecordEntity> entities = productViewRecordMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "商品浏览记录已被删除");

        ProductViewRecordEntity entity = new ProductViewRecordEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return productViewRecordMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return productViewRecordMapper;
    }
}
