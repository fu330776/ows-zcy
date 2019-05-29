package com.goodsogood.ows.controller;

import com.goodsogood.log4j2cm.annotation.HttpMonitorLogger;
import com.goodsogood.ows.helper.ExeclUtil;
import com.goodsogood.ows.helper.SmsUtils;
import com.goodsogood.ows.helper.UploadUtils;
import com.goodsogood.ows.model.vo.UpLoadVo;
import com.goodsogood.ows.model.vo.WithdrawsVo;
import com.goodsogood.ows.service.WithdrawsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v-UpLoad")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "工具控制器", tags = {" UpLoad manager"})
public class UpLoadController {

    private final WithdrawsService wservice;

    public UpLoadController(WithdrawsService withdrawsService) {
        this.wservice = withdrawsService;
    }

    @Value("${file.pdf}")
    private String PDF;
    private final String Url = "http://www.ztsms.cn/sendNSms.do";
    private final String pwd = "zcyun2019GS";
    private final String productid = "95533";
    private final String username = "zcyun";

    @HttpMonitorLogger
    @ApiOperation(value = "上传文件")
    @PostMapping("/upLoadPdf")
    public UpLoadVo Up(MultipartFile file, HttpServletRequest request) {
        UpLoadVo vo = new UpLoadVo();
        try {
            UploadUtils uploadUtils = new UploadUtils();
            vo = uploadUtils.importData(file, request, PDF);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vo;
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

    @HttpMonitorLogger
    @ApiOperation(value = "时间加10分钟")
    @GetMapping("/getDate")
    public Date getDate() {
        Date date = new Date();
        date.setTime(date.getTime() + 10 * 60 * 1000);
        return date;

    }

    @HttpMonitorLogger
    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void exportSiteDeclareList(HttpServletResponse response) {
        String[] headArray = {"提现流水号", "提现金额", "提现人", "是否提现", "添加时间", "打款时间"};
        List<WithdrawsVo> vos = this.wservice.GetOut(1);
        List<Object[]> contentList = new ArrayList<>();
        if (vos.size() > 0) {
            for (WithdrawsVo entity : vos) {
                Object[] o = {
                        entity.getWithdrawId(),//"运单号
                        entity.getWithdrawMoney(),
                        entity.getUserName(),
                        entity.getIsWithdraw() == 1 ? "未提现" : "已提现",
                        entity.getAddtime(),
                        entity.getPaytime()
                };
                contentList.add(o);
            }

        }
        try {
            ExeclUtil.ExportExcel(response, headArray, contentList, "SubmenuList.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
