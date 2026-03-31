package cn.net.susan.controller.web;

import cn.net.susan.annotation.Limit;
import cn.net.susan.annotation.NoLogin;
import cn.net.susan.dto.web.CityDTO;
import cn.net.susan.enums.LimitTypeEnum;
import cn.net.susan.helper.GeoIpHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "geoip操作", description = "geoip操作")
@RestController
@RequestMapping("/v1/web/geoip")
@Validated
public class GeoIpController {

    @Autowired
    private GeoIpHelper geoIpHelper;

    /**
     * 根据ip获取所在城市
     *
     * @param ip ip地址
     * @return 城市
     */
    @Limit(key = "getCity", permitsPerSecond = 20, timeOut = 60, limitType = LimitTypeEnum.IP)
    @NoLogin
    @ApiOperation(notes = "根据ip获取所在城市", value = "根据ip获取所在城市")
    @GetMapping("/getCity")
    public CityDTO getCity(@RequestParam(value = "ip") String ip) {
        return geoIpHelper.getCity(ip);
    }
}
