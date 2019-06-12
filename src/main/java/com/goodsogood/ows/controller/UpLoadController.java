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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.util.Map;

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
    @Value("${file.pdfs}")
    private String PDFs;
    private final String Url = "http://www.ztsms.cn/sendNSms.do";
    private final String pwd = "zcyun2019GS";
    private final String productid = "95533";
    private final String username = "zcyun";

    @HttpMonitorLogger
    @ApiOperation(value = "上传PDF文件")
    @PostMapping("/upLoadPdf")
    public UpLoadVo Ups(MultipartFile file, HttpServletRequest request) {
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
    @ApiOperation(value = "上传PDF文件 新技术 使用")
    @PostMapping("/upLoad")
    public UpLoadVo Up(MultipartFile file, HttpServletRequest request) {
        UpLoadVo vo = new UpLoadVo();
        try {
            UploadUtils uploadUtils = new UploadUtils();
            vo = uploadUtils.importData(file, request, PDFs);
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

    @HttpMonitorLogger
    @ApiOperation(value = "导入")
    @PostMapping("/importStore")
    public void importStore(MultipartFile file)throws IOException{
        String fileName = file.getOriginalFilename();
        //文件名后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());

        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new RuntimeException("文件格式不正确");
        }
        //如果文件名后缀为xls
        if(suffix.equals("xls")){
            //创建HSSFWorkbook对象
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            //获取有多少个sheet
            int sheetNum = workbook.getNumberOfSheets();

            for (int i = 0; i < sheetNum; i++) {
                //
                HSSFSheet sheet = workbook.getSheetAt(i);
                //获取sheet中一共有多少行
                int rowNum = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rowNum; j++) {
                    //获取行对象
                    HSSFRow row = sheet.getRow(j);
                    //第一行是表头，跳过
                    if(j == 0){
                        continue;
                    }
                    //获取值，后续省略
                    row.getCell(0).getStringCellValue();

					//获取行对象的单元格数量
					int cellNum = row.getPhysicalNumberOfCells();
					for (int k = 0; k < cellNum; k++) {
						HSSFCell cell = row.getCell(k);
						System.out.println("----------"+cell);

					}
                }
            }
        }
        //如果文件名后缀为xlsx
        if(suffix.equals("xlsx")){
            System.out.println("come on");

            //创建XSSFWorkbook对象
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            //获取sheet页数
            int sheetNum = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetNum; i++) {
                //获取XSSFSheet对象
                XSSFSheet sheet = workbook.getSheetAt(i);
                //获取sheet中一共有多少行
                int rowNum = sheet.getPhysicalNumberOfRows();

                for (int j = 0; j < rowNum; j++) {
                    //获取行对象
                    XSSFRow row = sheet.getRow(j);
                    if(j == 0){
                        continue;
                    }
                    //获取值，后续省略
                    row.getCell(0).getStringCellValue();
                }
            }

        }

    }


}
