package com.goodsogood.ows.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodsogood.ows.model.db.TokenResult;
import com.goodsogood.ows.model.vo.UserMenusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Configuration
@WebFilter
public class TokenFilterConfiguration extends OncePerRequestFilter {

    private List<String> urls = new ArrayList<String>() {{
        add("/v-UpLoad/");
//        add("/v-login/");
        add("/v-put/");
        add("/v-article/");
        add("/v-demands/");
        add("/v-funds/");
        add("/v-patents/");
        add("/v-task/");
        add("/v-users/");
        add("/v-cash/");
    }};

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        TokenResult result = new TokenResult();
        //获取url
        String url = httpServletRequest.getRequestURI();
        Boolean bl = true;
        for (String u : urls) {
            if (url.contains(u)) {
                bl = false;
                break;
            }
        }
        String method = httpServletRequest.getMethod();
        //过滤与请求头
        if("OPTIONS".equals(method))
        {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

        if (bl) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            //获取请求头内token
            String token = httpServletRequest.getHeader("Authorization");
            if (null == token) {
                result.setCode(401);
            } else {
                UserMenusVo entity = (UserMenusVo) CacheConfiguration.cache.get(token);
                if (entity == null) {
                    result.setCode(401);
                } else {
                    Date dt = new Date();
                    if (entity.time.getTime() < dt.getTime()) {
                        result.setCode(401);
                        CacheConfiguration.cache.remove(token);
                    } else {
                        filterChain.doFilter(httpServletRequest, httpServletResponse);
                    }
                }
            }
            try {
                responseOutWithJson(httpServletResponse, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void responseOutWithJson(HttpServletResponse response, Object responseObject) throws Exception {
        //将实体对象转换为JSON Object转换
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

