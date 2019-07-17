package com.goodsogood.ows.controller;


import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "WxConfig", tags = {"资金流水记录"})
public class WxConfigController {

    @RequestMapping({"MP_verify_JYvZaVpk5qoIK5io.txt"})
    private String returnConfigFile(HttpServletResponse response) {
        //把MP_verify_xxxxxx.txt中的内容返
        return "JYvZaVpk5qoIK5io";
    }

}