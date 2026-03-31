package cn.net.susan.helper;

import cn.hutool.json.JSONUtil;
import cn.net.susan.entity.BaseEntity;
import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;


@Slf4j
@Component
public class TokenHelper extends UserTokenHelper {

    private static final String TOKEN_PREFIX = "token:";
    private static final String USER_PREFIX = "user:";


    @Autowired
    private RedisUtil redisUtil;


    /**
     * 填充修改用户信息
     *
     * @param baseEntity 实体
     */
    public void fillUpdateUserInfo(BaseEntity baseEntity) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AssertUtil.notNull(authentication, "当前登录状态过期");
        JwtUserEntity jwtUserEntity = (JwtUserEntity) authentication.getPrincipal();
        baseEntity.setUpdateUserId(jwtUserEntity.getId());
        baseEntity.setUpdateUserName(jwtUserEntity.getUsername());
    }


    /**
     * 获取当前登录的用户名称
     *
     * @return 用户名称
     */
    public String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AssertUtil.notNull(authentication, "当前登录状态过期");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * 生成token
     *
     * @param userDetails 用户信息
     * @return token
     */
    public String generateToken(UserDetails userDetails) {
        return super.generateToken(userDetails.getUsername(), JSON.toJSONString(userDetails));
    }


    /**
     * 根据用户名称查询用户详情信息
     *
     * @param username 用户名称
     * @return 用户详情
     */
    public UserDetails getUserDetailsFromUsername(String username) {
        String userKey = getUserKey(username);
        String userDetailJson = redisUtil.get(userKey);
        if (!StringUtils.hasLength(userDetailJson)) {
            return null;
        }
        
        try {
            // 确保是字符串格式再解析
            if (userDetailJson instanceof String) {
                return JSON.parseObject(userDetailJson, JwtUserEntity.class);
            } else {
                // 如果不是字符串，先转换为字符串
                String jsonStr = JSON.toJSONString(userDetailJson);
                return JSON.parseObject(jsonStr, JwtUserEntity.class);
            }
        } catch (Exception e) {
            log.error("解析用户详情失败，username: {}, userDetailJson: {}", username, userDetailJson, e);
            return null;
        }
    }


    /**
     * 获取token
     *
     * @param username 用户名称
     * @return token
     */
    public String getToken(String username) {
        return redisUtil.get(getKey(TOKEN_PREFIX, username));
    }

    /**
     * 删除token
     *
     * @param token 用户名称
     */
    public void delToken(String token) {
        String username = getUsernameFromToken(token);
        redisUtil.del(getKey(TOKEN_PREFIX, username));
        redisUtil.del(getKey(USER_PREFIX, username));
    }

    /**
     * 获取用户详情
     *
     * @param username 用户名称
     * @return 用户详情
     */
    public UserDetails getUserDetails(String username) {
        String userJson = redisUtil.get(getKey(USER_PREFIX, username));
        return JSONUtil.toBean(userJson, UserDetails.class);
    }
}
