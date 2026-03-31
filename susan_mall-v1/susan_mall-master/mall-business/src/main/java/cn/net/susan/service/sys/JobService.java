package cn.net.susan.service.sys;

import java.util.List;

import cn.net.susan.entity.sys.DictConditionEntity;
import cn.net.susan.entity.sys.DictEntity;
import cn.net.susan.entity.sys.MenuEntity;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.BetweenTimeUtil;
import cn.net.susan.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.sys.JobMapper;
import cn.net.susan.entity.sys.JobConditionEntity;
import cn.net.susan.entity.sys.JobEntity;
import cn.net.susan.entity.ResponsePageEntity;


@Service
public class JobService extends BaseService<JobEntity, JobConditionEntity> {

    @Autowired
    private JobMapper jobMapper;

    /**
     * 查询岗位信息
     *
     * @param id 岗位ID
     * @return 岗位信息
     */
    public JobEntity findById(Long id) {
        return jobMapper.findById(id);
    }

    /**
     * 根据条件分页查询岗位列表
     *
     * @param jobConditionEntity 岗位信息
     * @return 岗位集合
     */
    public ResponsePageEntity<JobEntity> searchByPage(JobConditionEntity jobConditionEntity) {
        return super.searchByPage(jobConditionEntity);
    }

    /**
     * 新增岗位
     *
     * @param jobEntity 岗位信息
     * @return 结果
     */
    public int insert(JobEntity jobEntity) {
        return jobMapper.insert(jobEntity);
    }

    /**
     * 修改岗位
     *
     * @param jobEntity 岗位信息
     * @return 结果
     */
    public int update(JobEntity jobEntity) {
        return jobMapper.update(jobEntity);
    }

    /**
     * 删除岗位对象
     *
     * @param ids 系统ID
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<JobEntity> jobEntities = jobMapper.findByIds(ids);
        AssertUtil.notEmpty(jobEntities, "岗位已被删除");

        JobEntity jobEntity = new JobEntity();
        FillUserUtil.fillUpdateUserInfo(jobEntity);
        return jobMapper.deleteByIds(ids, jobEntity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return jobMapper;
    }
}
