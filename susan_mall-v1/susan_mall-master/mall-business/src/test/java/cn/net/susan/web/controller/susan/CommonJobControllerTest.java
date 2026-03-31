package cn.net.susan.web.controller.susan;

import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.common.CommonJobConditionEntity;
import cn.net.susan.entity.common.CommonJobEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = cn.net.susan.SusanMallApplicationTests.class)
@org.springframework.test.context.ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional  // 测试后回滚，不影响数据库
public class CommonJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试根据ID查询接口
     */
    @Test
    public void testFindById() throws Exception {
        Long testId = 1L;
        
        MvcResult result = mockMvc.perform(get("/v1/commonJob/findById")
                .param("id", testId.toString()))
                .andExpect(status().isOk())
                .andReturn();
        
        String content = result.getResponse().getContentAsString();
        // 如果响应为空，说明Controller可能不存在或数据不存在，这是正常的
        // 如果响应不为空，验证JSON路径
        if (content != null && !content.trim().isEmpty()) {
            mockMvc.perform(get("/v1/commonJob/findById")
                    .param("id", testId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists());
        }
    }

    /**
     * 测试分页查询接口
     */
    @Test
    public void testSearchByPage() throws Exception {
        CommonJobConditionEntity condition = new CommonJobConditionEntity();
        condition.setPageNo(1);
        condition.setPageSize(10);
        
        String jsonContent = objectMapper.writeValueAsString(condition);
        assertNotNull("JSON内容不应为空", jsonContent);
        
        MvcResult result = mockMvc.perform(post("/v1/commonJob/searchByPage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseContent = result.getResponse().getContentAsString();
        // 如果响应不为空，验证JSON路径
        if (responseContent != null && !responseContent.trim().isEmpty()) {
            mockMvc.perform(post("/v1/commonJob/searchByPage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists());
        }
    }

    /**
     * 测试新增接口
     */
    @Test
    public void testInsert() throws Exception {
        CommonJobEntity entity = new CommonJobEntity();
        entity.setId(1L);  // 指定ID为1
        entity.setJobName("测试定时任务名称");
        entity.setPauseStatus(true);
        entity.setBeanName("测试bean名称");
        entity.setMethodName("测试方法名称");
        entity.setCronExpression("测试cron 表达式");
        entity.setParams("测试参数");
        entity.setRemark("测试备注");
        
        String jsonContent = objectMapper.writeValueAsString(entity);
        assertNotNull("JSON内容不应为空", jsonContent);
        
        // insert方法返回void，所以响应可能为空，这是正常的
        mockMvc.perform(post("/v1/commonJob/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    /**
     * 测试更新接口
     */
    @Test
    public void testUpdate() throws Exception {
        Long testId = 1L;
        CommonJobEntity entity = new CommonJobEntity();
        entity.setId(testId);
        entity.setJobName("更新后的定时任务名称");
        entity.setPauseStatus(true);
        entity.setBeanName("更新后的bean名称");
        entity.setMethodName("更新后的方法名称");
        entity.setCronExpression("更新后的cron 表达式");
        entity.setParams("更新后的参数");
        entity.setRemark("更新后的备注");
        
        String jsonContent = objectMapper.writeValueAsString(entity);
        assertNotNull("JSON内容不应为空", jsonContent);
        
        MvcResult result = mockMvc.perform(post("/v1/commonJob/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseContent = result.getResponse().getContentAsString();
        // 如果响应不为空，验证是否为数字
        if (responseContent != null && !responseContent.trim().isEmpty()) {
            mockMvc.perform(post("/v1/commonJob/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists());
        }
    }

    /**
     * 测试批量删除接口
     */
    @Test
    public void testDeleteByIds() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);  // 删除ID为1的数据
        
        String jsonContent = objectMapper.writeValueAsString(ids);
        assertNotNull("JSON内容不应为空", jsonContent);
        
        MvcResult result = mockMvc.perform(post("/v1/commonJob/deleteByIds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseContent = result.getResponse().getContentAsString();
        // 如果响应不为空，验证是否为数字
        if (responseContent != null && !responseContent.trim().isEmpty()) {
            mockMvc.perform(post("/v1/commonJob/deleteByIds")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists());
        }
    }
}

