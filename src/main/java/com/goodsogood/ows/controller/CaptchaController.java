package com.goodsogood.ows.controller;

import com.goodsogood.ows.helper.VerifyCodeHelper;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author xuliduo
 * @date 2018/5/2
 * @description class CaptchaController
 */
@RestController
@RequestMapping("/")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600, exposedHeaders = {"_captcha"})
@Api(value = "图片验证码", tags = {"captcha"})
public class CaptchaController {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public CaptchaController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/login.{ext}")
    public void captcha(@PathVariable String ext, @RequestParam(required = false) String uuid, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String[] rands = VerifyCodeHelper.getRands();
        String sRand = StringUtils.join(rands);
        uuid = uuid == null ? UUID.randomUUID().toString() : uuid;
        // 写入缓存(5分钟失效)
        stringRedisTemplate.opsForValue().set("LOGIN_CAPTCHA_" + uuid, sRand, 5, TimeUnit.MINUTES);
        log.debug("sRand in controller -> {}", sRand);
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("_captcha", uuid);
        httpServletResponse.setDateHeader("Expires", 0);

        try {
            if (!Strings.isNullOrEmpty(ext) && "gif".equalsIgnoreCase(ext)) {
                httpServletResponse.setContentType("image/gif");
                VerifyCodeHelper.showVerifyCodeGif(httpServletResponse.getOutputStream(), rands, 100, 38);
            } else {
                httpServletResponse.setContentType("image/png");
                VerifyCodeHelper.showVerifyCodePng(httpServletResponse.getOutputStream(), rands, 100, 38);
            }
        } catch (FontFormatException e) {
            log.error(e.getMessage(), e);
        }
    }

}
