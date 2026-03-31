package cn.net.susan.controller;

import cn.net.susan.exception.BusinessException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static cn.net.susan.filter.JwtTokenFilter.FILTER_ERROR;
import static cn.net.susan.filter.JwtTokenFilter.FILTER_ERROR_PATH;


@Slf4j
@RestController
public class FilterExceptionController {

    @ApiOperation(value = "过滤器异常处理", produces = "application/json")
    @RequestMapping(FILTER_ERROR_PATH)
    public void handleException(HttpServletRequest request) {
        Object exception = request.getAttribute(FILTER_ERROR);
        if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            throw businessException;
        }
        throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误，请联系系统管理员！");
    }
}
