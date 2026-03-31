package cn.net.susan.service.log;

import cn.net.susan.dto.web.CityDTO;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.log.BizLogConditionEntity;
import cn.net.susan.entity.log.BizLogEntity;
import cn.net.susan.helper.GeoIpHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.log.BizLogMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class BizLogService extends BaseService<BizLogEntity, BizLogConditionEntity> {

    @Autowired
    private BizLogMapper bizLogMapper;
    @Autowired
    private GeoIpHelper geoIpHelper;

    /**
     * 查询业务日志信息
     *
     * @param id 业务日志ID
     * @return 业务日志信息
     */
    public BizLogEntity findById(Long id) {
        return bizLogMapper.findById(id);
    }

    /**
     * 根据条件分页查询业务日志列表
     *
     * @param bizLogConditionEntity 业务日志信息
     * @return 业务日志集合
     */
    public ResponsePageEntity<BizLogEntity> searchByPage(BizLogConditionEntity bizLogConditionEntity) {
       return super.searchByPage(bizLogConditionEntity);
    }

    /**
     * 新增业务日志
     *
     * @param bizLogEntity 业务日志信息
     * @return 结果
     */
    public void save(BizLogEntity bizLogEntity) {
        FillUserUtil.fillCreateUserInfo(bizLogEntity);
        CityDTO cityDTO = geoIpHelper.getCity(bizLogEntity.getRequestIp());
        if (Objects.nonNull(cityDTO)) {
            bizLogEntity.setCity(cityDTO.getCity());
        }
        bizLogMapper.insert(bizLogEntity);
    }

    /**
     * 修改业务日志
     *
     * @param bizLogEntity 业务日志信息
     * @return 结果
     */
    public int update(BizLogEntity bizLogEntity) {
        return bizLogMapper.update(bizLogEntity);
    }

    /**
     * 删除业务日志对象
     *
     * @param id 系统ID
     * @return 结果
     */
    public int deleteById(Long id) {
        return bizLogMapper.deleteById(id);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return bizLogMapper;
    }
}
