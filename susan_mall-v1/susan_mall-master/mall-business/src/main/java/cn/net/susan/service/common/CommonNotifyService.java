package cn.net.susan.service.common;

import java.util.List;

import cn.net.susan.entity.common.CommonJobConditionEntity;
import cn.net.susan.entity.common.CommonJobEntity;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.common.CommonNotifyMapper;
import cn.net.susan.entity.common.CommonNotifyConditionEntity;
import cn.net.susan.entity.common.CommonNotifyEntity;
import cn.net.susan.entity.ResponsePageEntity;


@Service
public class CommonNotifyService extends BaseService<CommonNotifyEntity, CommonNotifyConditionEntity> {

	@Autowired
	private CommonNotifyMapper commonNotifyMapper;

	/**
     * 查询通知信息
     *
     * @param id 通知ID
     * @return 通知信息
     */
	public CommonNotifyEntity findById(Long id) {
	    return commonNotifyMapper.findById(id);
	}

	/**
     * 根据条件分页查询通知列表
     *
     * @param commonNotifyConditionEntity 通知信息
     * @return 通知集合
     */
	public ResponsePageEntity<CommonNotifyEntity> searchByPage(CommonNotifyConditionEntity commonNotifyConditionEntity) {
		return super.searchByPage(commonNotifyConditionEntity);
	}

    /**
     * 新增通知
     *
     * @param commonNotifyEntity 通知信息
     * @return 结果
     */
	public int insert(CommonNotifyEntity commonNotifyEntity) {
	    return commonNotifyMapper.insert(commonNotifyEntity);
	}

	/**
     * 修改通知
     *
     * @param commonNotifyEntity 通知信息
     * @return 结果
     */
	public int update(CommonNotifyEntity commonNotifyEntity) {
	    return commonNotifyMapper.update(commonNotifyEntity);
	}

	/**
     * 删除通知对象
     *
     * @param id 系统ID
     * @return 结果
     */
	public int deleteById(Long id) {
		return commonNotifyMapper.deleteById(id);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return commonNotifyMapper;
	}
}
