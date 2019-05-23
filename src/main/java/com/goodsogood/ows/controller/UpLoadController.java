package com.goodsogood.ows.controller;

import com.goodsogood.log4j2cm.annotation.HttpMonitorLogger;
import com.goodsogood.ows.helper.SmsUtils;
import com.goodsogood.ows.helper.UploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/v-UpLoad")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "测试控制器", tags = {" UpLoad manager"})
public class UpLoadController {

    private final String Url = "http://www.ztsms.cn/sendNSms.do";
    private final String pwd = "zcyun2019GS";
    private final String productid = "95533";
    private final String username = "zcyun";

    @HttpMonitorLogger
    @ApiOperation(value = "测试上传文件")
    @PostMapping("/Up")
    public String Up(MultipartFile file, HttpServletRequest request) {
        String Url = null;
        try {
            UploadUtils uploadUtils = new UploadUtils();
            Url = uploadUtils.importData(file, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Url;
    }

    @HttpMonitorLogger
    @ApiOperation(value = "测试验证码发送")
    @PostMapping("/Sms")
    public String Sms() {
        String sms = null;
        String content = "验证码：" + 123456 + "，请不要把验证码泄露给他人，谢谢！【知创云】";
        try {
            sms = SmsUtils.postEncrypt(Url, username, pwd, "17783374871", content, productid);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sms;
    }
}
