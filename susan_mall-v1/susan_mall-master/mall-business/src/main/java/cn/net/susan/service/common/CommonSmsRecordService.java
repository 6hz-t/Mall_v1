package cn.net.susan.service.common;

import java.util.List;

import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.common.CommonSmsRecordMapper;
import cn.net.susan.entity.common.CommonSmsRecordConditionEntity;
import cn.net.susan.entity.common.CommonSmsRecordEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;

 
@Service
public class CommonSmsRecordService extends BaseService<CommonSmsRecordEntity, CommonSmsRecordConditionEntity> {

	@Autowired
	private CommonSmsRecordMapper commonSmsRecordMapper;

	/**
     * 查询短信发送记录信息
     *
     * @param id 短信发送记录ID
     * @return 短信发送记录信息
     */
	public CommonSmsRecordEntity findById(Long id) {
	    return commonSmsRecordMapper.findById(id);
	}

	/**
     * 根据条件分页查询短信发送记录列表
     *
     * @param commonSmsRecordConditionEntity 短信发送记录信息
     * @return 短信发送记录集合
     */
	public ResponsePageEntity<CommonSmsRecordEntity> searchByPage(CommonSmsRecordConditionEntity commonSmsRecordConditionEntity) {
		return super.searchByPage(commonSmsRecordConditionEntity);
	}

    /**
     * 新增短信发送记录
     *
     * @param commonSmsRecordEntity 短信发送记录信息
     * @return 结果
     */
	public int insert(CommonSmsRecordEntity commonSmsRecordEntity) {
	    return commonSmsRecordMapper.insert(commonSmsRecordEntity);
	}

	/**
     * 修改短信发送记录
     *
     * @param commonSmsRecordEntity 短信发送记录信息
     * @return 结果
     */
	public int update(CommonSmsRecordEntity commonSmsRecordEntity) {
	    return commonSmsRecordMapper.update(commonSmsRecordEntity);
	}

	/**
     * 批量删除短信发送记录
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<CommonSmsRecordEntity> entities = commonSmsRecordMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "短信发送记录已被删除");

		CommonSmsRecordEntity entity = new CommonSmsRecordEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return commonSmsRecordMapper.deleteByIds(ids, entity);
	}

    @Override
    protected BaseMapper getBaseMapper() {
        return commonSmsRecordMapper;
    }
}
