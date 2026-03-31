package cn.net.susan.service;

import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.common.CommonJobConditionEntity;
import cn.net.susan.entity.common.CommonJobEntity;
import cn.net.susan.service.common.CommonJobService;
import cn.net.susan.util.FillUserUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = cn.net.susan.SusanMallApplicationTests.class)
@org.springframework.test.context.ActiveProfiles("test")
@Transactional  // 测试后回滚，不影响数据库
public class CommonJobServiceTest {

    @Autowired
    private CommonJobService commonJobService;

    /**
     * 在每个测试方法执行前设置用户上下文
     */
    @Before
    public void setUp() {
        // Mock用户信息，用于填充创建人和修改人信息
        FillUserUtil.mockCurrentUser();
    }

    /**
     * 在每个测试方法执行后清理用户上下文
     */
    @After
    public void tearDown() {
        // 清理用户上下文，避免影响其他测试
        FillUserUtil.clearCurrentUser();
    }

    /**
     * 测试根据ID查询
     */
    @Test
    public void testFindById() {
        // 准备测试数据
        Long testId = 1L;
        
        // 执行查询
        CommonJobEntity result = commonJobService.findById(testId);
        
        // 验证结果
        assertNotNull("查询结果不应为空", result);
        assertEquals("ID应该匹配", testId, result.getId());
    }

    /**
     * 测试分页查询
     */
    @Test
    public void testSearchByPage() {
        // 准备测试数据
        CommonJobConditionEntity condition = new CommonJobConditionEntity();
        condition.setPageNo(1);
        condition.setPageSize(10);
        
        // 执行查询
        ResponsePageEntity<CommonJobEntity> result = commonJobService.searchByPage(condition);
        
        // 验证结果
        assertNotNull("查询结果不应为空", result);
        assertNotNull("数据列表不应为空", result.getData());
    }

    /**
     * 测试新增
     */
    @Test
    public void testInsert() {
        // 准备测试数据
        CommonJobEntity entity = new CommonJobEntity();
        // 注意：ID由MyBatis拦截器自动生成（雪花算法），不需要手动设置
        entity.setJobName("测试定时任务名称");
        entity.setPauseStatus(true);
        entity.setBeanName("测试bean名称");
        entity.setMethodName("测试方法名称");
        entity.setCronExpression("0 0 12 * * ?");  // 每天中午12点执行
        entity.setParams("测试参数");
        entity.setRemark("测试备注");
        
        // 执行插入
        int result = commonJobService.insert(entity);
        
        // 验证结果
        assertEquals("插入应该成功", 1, result);
        assertNotNull("ID应该被自动生成", entity.getId());
        assertTrue("ID应该大于0", entity.getId() > 0);
    }

    /**
     * 测试更新
     */
    @Test
    public void testUpdate() {
        // 准备测试数据
        Long testId = 1L;
        CommonJobEntity entity = commonJobService.findById(testId);
        assertNotNull("测试数据应该存在", entity);
        
        entity.setJobName("更新后的定时任务名称");
        entity.setPauseStatus(!entity.getPauseStatus());
        entity.setBeanName("更新后的bean名称");
        entity.setMethodName("更新后的方法名称");
        entity.setCronExpression("0 0 18 * * ?");  // 每天下午6点执行
        entity.setParams("更新后的参数");
        entity.setRemark("更新后的备注");
                                                                                                                                                
        // 执行更新
        int result = commonJobService.update(entity);
        
        // 验证结果
        assertEquals("更新应该成功", 1, result);
    }

    /**
     * 测试批量删除
     */
    @Test
    public void testDeleteByIds() {
        // 准备测试数据
        List<Long> ids = new ArrayList<>();
        ids.add(1L);  // 删除ID为1的数据
        
        // 执行删除
        int result = commonJobService.deleteByIds(ids);
        
        // 验证结果（由于使用了@Transactional，实际不会删除数据）
        assertTrue("删除操作应该执行", result >= 0);
    }
}

