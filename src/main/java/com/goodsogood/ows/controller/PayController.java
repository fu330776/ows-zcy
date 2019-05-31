package com.goodsogood.ows.controller;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v-Pay")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "支付", tags = {"Pay manager"})
public class PayController {


}
