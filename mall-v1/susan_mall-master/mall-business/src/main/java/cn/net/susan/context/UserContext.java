package cn.net.susan.context;

import cn.net.susan.entity.sys.UserEntity;
import com.alibaba.ttl.TransmittableThreadLocal;


public class UserContext {

    private static final TransmittableThreadLocal<UserEntity> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取当前用户信息
     *
     * @return 当前用户信息
     */
    public static UserEntity getCurrentUser() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置当前用户信息
     *
     * @param userEntity 当前用户信息
     */
    public static void setCurrentUser(UserEntity userEntity) {
        THREAD_LOCAL.set(userEntity);
    }

    /**
     * 清空当前用户信息
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
