package cn.net.susan.filter;

import cn.net.susan.exception.BusinessException;
import cn.net.susan.wrapper.MallHttpServletRequestWrapper;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static cn.net.susan.filter.JwtTokenFilter.FILTER_ERROR;
import static cn.net.susan.filter.JwtTokenFilter.FILTER_ERROR_PATH;


@Slf4j
//@WebFilter(urlPatterns = "/*", filterName = "sqlInjectFilter")
//@Configuration
public class SqlInjectFilter implements Filter {

    /**
     * 过滤掉的sql关键字，特殊字符前面需要加\\进行转义
     */
    private static final String BAD_SQL_KEYWORD = "\\b(and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%)";
    private static final String POST_METHOD = "POST";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.debug("SqlInjectFilter requestURI:{}", httpServletRequest.getRequestURI());
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String sql = "";

        if (POST_METHOD.equals(httpServletRequest.getMethod().toUpperCase())) {
            MallHttpServletRequestWrapper httpServletRequestWrapper = new MallHttpServletRequestWrapper(httpServletRequest);
            String body = httpServletRequestWrapper.getBody();
            Map<String, Object> paramsMap = JSONObject.parseObject(body, TreeMap.class);
            if (MapUtils.isNotEmpty(paramsMap)) {
                for (Object value : paramsMap.values()) {
                    sql += value.toString();
                }
            }
            httpServletRequest = httpServletRequestWrapper;
        } else {
            //获得所有请求参数名
            Enumeration<String> names = httpServletRequest.getParameterNames();

            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String[] values = httpServletRequest.getParameterValues(name);
                for (int i = 0; i < values.length; i++) {
                    sql += values[i];
                }
            }
        }

        if (sqlValidate(sql)) {
            httpServletRequest.setAttribute(FILTER_ERROR, new BusinessException("请求参数非法"));
            httpServletRequest.getRequestDispatcher(FILTER_ERROR_PATH).forward(request, response);
        } else {
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private boolean sqlValidate(String sql) {
        //使用正则表达式进行匹配
        Pattern pattern = Pattern.compile(BAD_SQL_KEYWORD);
        return pattern.matcher(sql.toLowerCase()).find();
    }
}

