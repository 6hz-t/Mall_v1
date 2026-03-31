package cn.net.susan.helper;

import cn.net.susan.util.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Component
public class IpWhiteListHelper {

    @Value("#{'${mall.mgt.api.ipWhiteList:127.0.0.1}'.split(',')}")
    private List<String> ipWhiteList;

    /**
     * 校验ip是白名单中
     *
     * @return 是否
     */
    public boolean checkIp() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = IpUtil.getIpAddr(httpServletRequest);
        return ipWhiteList.contains(ip);
    }
}
