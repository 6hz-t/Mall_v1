package cn.net.susan.service.sys;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.sys.UserRoleMapper;
import cn.net.susan.entity.sys.UserRoleConditionEntity;
import cn.net.susan.entity.sys.UserRoleEntity;
import cn.net.susan.entity.ResponsePageEntity;


@Service
public class UserRoleService {

	@Autowired
	private UserRoleMapper userRoleMapper;

	/**
     * 查询用户角色关联信息
     * 
     * @param id 用户角色关联ID
     * @return 用户角色关联信息
     */
	public UserRoleEntity findById(Long id) {
	    return userRoleMapper.findById(id);
	}
	
	/**
     * 根据条件分页查询用户角色关联列表
     * 
     * @param userRoleConditionEntity 用户角色关联信息
     * @return 用户角色关联集合
     */
	public ResponsePageEntity<UserRoleEntity> searchByPage(UserRoleConditionEntity userRoleConditionEntity) {
		int count = userRoleMapper.searchCount(userRoleConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(userRoleConditionEntity);
		}
		List<UserRoleEntity> dataList = userRoleMapper.searchByCondition(userRoleConditionEntity);
		return ResponsePageEntity.build(userRoleConditionEntity, count, dataList);
	}
	
    /**
     * 新增用户角色关联
     * 
     * @param userRoleEntity 用户角色关联信息
     * @return 结果
     */
	public int insert(UserRoleEntity userRoleEntity) {
	    return userRoleMapper.insert(userRoleEntity);
	}
	
	/**
     * 修改用户角色关联
     * 
     * @param userRoleEntity 用户角色关联信息
     * @return 结果
     */
	public int update(UserRoleEntity userRoleEntity) {
	    return userRoleMapper.update(userRoleEntity);
	}

	/**
     * 删除用户角色关联对象
     * 
     * @param id 系统ID
     * @return 结果
     */
	public int deleteById(Long id) {
		return userRoleMapper.deleteById(id);
	}
	
}
