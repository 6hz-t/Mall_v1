package cn.net.susan.service.sys;

import java.util.List;
import java.util.stream.Collectors;

import cn.net.susan.entity.sys.RoleMenuEntity;
import cn.net.susan.helper.IdGenerateHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.sys.RoleMenuMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.BetweenTimeUtil;
import cn.net.susan.util.FillUserUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.sys.RoleMapper;
import cn.net.susan.entity.sys.RoleConditionEntity;
import cn.net.susan.entity.sys.RoleEntity;
import cn.net.susan.entity.ResponsePageEntity;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RoleService extends BaseService<RoleEntity, RoleConditionEntity> {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private IdGenerateHelper idGenerateHelper;

    /**
     * 查询角色信息
     *
     * @param id 角色ID
     * @return 角色信息
     */
    public RoleEntity findById(Long id) {
        return roleMapper.findById(id);
    }

    /**
     * 根据条件分页查询角色列表
     *
     * @param roleConditionEntity 角色信息
     * @return 角色集合
     */
    public ResponsePageEntity<RoleEntity> searchByPage(RoleConditionEntity roleConditionEntity) {
        return super.searchByPage(roleConditionEntity);
    }

    /**
     * 根据查询所有角色
     *
     * @return 所有角色
     */
    public List<RoleEntity> all() {
        RoleConditionEntity roleConditionEntity = new RoleConditionEntity();
        roleConditionEntity.setPageSize(0);
        return roleMapper.searchByCondition(roleConditionEntity);
    }

    /**
     * 新增角色
     *
     * @param roleEntity 角色信息
     * @return 结果
     */
    public int insert(RoleEntity roleEntity) {
        return roleMapper.insert(roleEntity);
    }

    /**
     * 修改角色
     *
     * @param roleEntity 角色信息
     * @return 结果
     */
    @Transactional(rollbackFor = Throwable.class)
    public int update(RoleEntity roleEntity) {
        roleMenuMapper.deleteByRoleIds(Lists.newArrayList(roleEntity.getId()));
        saveRoleMenu(roleEntity);
        return roleMapper.update(roleEntity);
    }

    private void saveRoleMenu(RoleEntity roleEntity) {
        if (CollectionUtils.isEmpty(roleEntity.getMenus())) {
            return;
        }

        List<RoleMenuEntity> roleMenuEntities = roleEntity.getMenus().stream().map(x -> {
            RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
            roleMenuEntity.setId(idGenerateHelper.nextId());
            roleMenuEntity.setRoleId(roleEntity.getId());
            roleMenuEntity.setMenuId(x.getId());
            return roleMenuEntity;
        }).collect(Collectors.toList());

        roleMenuMapper.batchInsert(roleMenuEntities);
    }

    /**
     * 批量删除角色对象
     *
     * @param ids 系统ID
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<RoleEntity> roleEntities = roleMapper.findByIds(ids);
        AssertUtil.notEmpty(roleEntities, "角色已被删除");

        RoleEntity roleEntity = new RoleEntity();
        FillUserUtil.fillUpdateUserInfo(roleEntity);
        return roleMapper.deleteByIds(ids, roleEntity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return roleMapper;
    }
}
