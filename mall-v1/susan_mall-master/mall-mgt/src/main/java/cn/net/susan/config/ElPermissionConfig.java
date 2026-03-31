package cn.net.susan.config;


import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.util.FillUserUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service(value = "el")
public class ElPermissionConfig {

    /**
     * 检查用户权限
     *
     * @param permissions 指定的权限
     * @return 用户是否有权限
     */
    public Boolean checkPermission(String... permissions) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        if (Objects.nonNull(currentUserInfo)) {
            List<String> userAllPermissions = currentUserInfo.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return userAllPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(userAllPermissions::contains);
        }
        return false;
    }
}
