package com.goodsogood.ows.configuration;

import com.goodsogood.ows.model.db.TokenResult;
import com.goodsogood.ows.model.vo.UserMenusVo;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class TokenFilterConfiguration extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        TokenResult result = new TokenResult();
        //获取url
        String Url = httpServletRequest.getRequestURI();
        //获取头 内token
        String token = httpServletRequest.getHeader("Authorization");

        if (token == null) {
            result.Code = 401;
        } else {
            UserMenusVo entity = (UserMenusVo) CacheConfiguration.cache.get(token);
            if (entity == null) {
                result.Code = 402;
            } else {
                Date dt = new Date();
                if (entity.time.getTime() < dt.getTime()) {
                    result.Code = 403;
                    CacheConfiguration.cache.remove(token);
                } else {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                }


            }


        }

    }
}

