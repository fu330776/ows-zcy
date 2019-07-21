package com.goodsogood.ows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.goodsogood.log4j2cm.annotation.HttpMonitorLogger;
import com.goodsogood.ows.helper.ExeclUtil;
import com.goodsogood.ows.helper.SmsUtils;
import com.goodsogood.ows.helper.UploadUtils;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.UsersService;
import com.goodsogood.ows.service.WithdrawsService;
import com.unboundid.util.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    private  final UsersService service;

    public UpLoadController(WithdrawsService withdrawsService,UsersService service) {
        this.wservice = withdrawsService;this.service=service;
    }

    @Value("${file.picture}")
    private String PDF;
    @Value("${file.picture}")
    private String PDFs;
    @Value(value = "${file.picture}")
    private String picture;

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
    @ApiOperation(value = "图片上传")
    @PostMapping("/uploadPicture")
    public  UpLoadVo UploadPictures(MultipartFile file, HttpServletRequest request)
    {
        UpLoadVo vo = new UpLoadVo();
        try {
            UploadUtils uploadUtils = new UploadUtils();
            vo = uploadUtils.importPicture(file, request, picture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vo;
    }
    @HttpMonitorLogger
    @ApiOperation(value = "图片下载")
    @PostMapping("/down")
    public  UpLoadVo DownPDF(String url,HttpServletRequest request,HttpServletResponse response)
    {
        UploadUtils uploadUtils = new UploadUtils();
        return  uploadUtils.downloadImage(url,request,response);
    }


    @HttpMonitorLogger
    @ApiOperation(value = "测试验证码发送")
    @PostMapping("/Sms")
    public String Sms() throws IOException {
        String sms = null;
        //String content = "验证码：" + 123456 + "，请不要把验证码泄露给他人，谢谢！【知创云】";
        sms= SmsUtils.SendSms("17783374871","知创云","SMS_171115994","123456");
     ObjectMapper mapper = new ObjectMapper(); //转换器
        SmsForm getCodes=mapper.readValue(sms,SmsForm.class);
        return getCodes.getMessage();
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
    @ApiOperation(value = "用户导出")
    @PostMapping("/exportStore")
    public  void exportStore (@Valid @RequestBody UserRoleForm user, HttpServletResponse response)
    {
        String [] headArray={};
        List<Object[]> contentList = new ArrayList<>();
        String[] headArraygr  = {"名称", "医院", "科室", "职位", "职称", "电话","邮箱","等级","性质","银行卡号","持卡人姓名","持卡人身份证","省","市","区"};
        String [] headArrayqy = {"姓名","账号","企业名称","企业代码","联系人","联系电话"}; // 医院 企业
        String [] headArrayjg = {"账号名称","账号","机构名称","机构代码","联系人","联系电话","详细地址"};
        List<UserInfoVo> entitys = this.service.GetByExport(user.getRoleId(),user.getName(),user.getProvinces(),user.getMunicipalities(),user.getDistricts(),user.getGrade(),user.getNature(),user.getKeyword()
        ,user.getReview(),user.getEnable());
      int rid=user.getRoleId().intValue();
        switch (rid)
        {
            case  2 :
                headArray=headArraygr;
                contentList=GetAssemble(entitys);
                break;
            case  3 :
                headArray=headArrayqy;
                contentList=GetAssembleyy(entitys);
                break;
            case  4 :
                headArray=headArrayjg;
                contentList=GetAssemblejg(entitys);
                break;
        }
        try {
            ExeclUtil.ExportExcel(response, headArray, contentList, "user.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *  个人
     * @param entitys
     * @return
     */
    private   List<Object[]> GetAssemble( List<UserInfoVo> entitys)
    {
        List<Object[]> contentList =new ArrayList<>();
        if (entitys.size() > 0) {
            for (UserInfoVo entity : entitys) {
                Object[] o = {
                        entity.getUserName(),//名称
                        entity.getUserHospital(),//医院
                        entity.getUserDepartment(),//科室
                        entity.getUserPosition()  ,//职位
                        entity.getTitle(), //职称
                        entity.getPhone(), //电话
                        entity.getUserEmail(), //邮箱
                        entity.getGrade(), //等级
                        entity.getNature(), //性质
                        entity.getUserBankCardNumber(), //银行卡号
                        entity.getUserCardholderName(), //持卡人姓名
                        entity.getUserCardholderIdcard(),//持卡人身份证
                        entity.getProvinces(), //省
                        entity.getMunicipalities(),//市
                        entity.getDistricts() //区
                };
                contentList.add(o);
            }
        }
            return  contentList;

    }

    /**
     *  医院
     * @param entitys
     * @return
     */
    private  List<Object[]> GetAssembleyy(List<UserInfoVo> entitys)
    {
        List<Object[]> contentList =new ArrayList<>();
        if (entitys.size() > 0) {
            for (UserInfoVo entity : entitys) {
                Object[] o = {
                        entity.getUserName(),//名称
                        entity.getPhone(), //账号
                        entity.getCompanyName(), //企业名称
                        entity.getCompanyCode(), //企业代码
                        entity.getContacts(), //联系人
                        entity.getContact_phone() //联系电话
                };
                contentList.add(o);
            }
        }
        return  contentList;
    }

    /**
     *  医疗机构
     * @param entitys
     * @return
     */
    private  List<Object[]> GetAssemblejg(List<UserInfoVo> entitys)
    {
        List<Object[]> contentList =new ArrayList<>();
        if (entitys.size() > 0) {
            for (UserInfoVo entity : entitys) {
                Object[] o = {
                        entity.getUserName(),//名称
                        entity.getPhone(), //账号
                        entity.getOrganizationName(), //企业名称
                        entity.getOrganizationCode(), //企业代码
                        entity.getContacts(), //联系人
                        entity.getContact_phone(), //联系电话
                        entity.getDetailed_address() //详细地址
                };
                String [] headArrayjg={"账号名称","账号","机构名称","机构代码","联系人","联系电话","详细地址"};
                contentList.add(o);
            }
        }
        return  contentList;
    }

    @HttpMonitorLogger
    @ApiOperation(value = "用户导入")
    @PostMapping("/importStore")
    public  Boolean importStore(int type,MultipartFile file)throws IOException{
        log.debug("type:"+type);
        String fileName = file.getOriginalFilename();
        //文件名后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());

        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new RuntimeException("文件格式不正确");
        }
        List<UsersForm>  entities=new ArrayList<>();

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
                    UsersForm user=new UsersForm();
                    switch (type){
                        case  1:
                            user=GetHSSFCell(row);
                            if(user==null)
                            {
                                return  false;
                            }
                            //获取值，后续省略
                            entities.add(user);
                            break;
                        case 2: //医院
                            user=GetHospitalHSSFCell(row);
                            if(user==null)
                            {
                                return  false;
                            }
                            entities.add(user);
                            break;
                        case 3: //医疗机构
                            user=GetMedicalHSSFCell(row);
                            if(user==null)
                            {
                                return  false;
                            }
                            entities.add(user);
                            break;
                    }

                }
            }
        }
        //如果文件名后缀为xlsx
        if(suffix.equals("xlsx")){
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
                    UsersForm user=new UsersForm();
                            switch (type){
                        case  1:
                            //获取值，后续省略
                             user=  GetXSSFCell(row);
                            if(user ==null)
                            {
                            return  false;
                            }
                            entities.add(user);
                            break;
                        case 2: //医院

                            user= GetHospitalXSSFCell(row);
                            if(user ==null)
                            {
                                return  false;
                            }
                            entities.add(user);
                            break;
                        case 3: //医疗机构
                            user=GetMedicalXSSFCell(row);
                            if(user==null)
                            {
                                return  false;
                            }
                            entities.add(user);
                            break;
                    }
               }
            }
        }

        Boolean bool= this.service.ImportAdminExecl(entities);
        return  bool;
    }


    /**
     *   个人医护
     * @param row
     * @return
     */
    private   UsersForm GetXSSFCell(XSSFRow row)
    {
        UsersForm entity=new UsersForm();
        if(row.getCell(2)==null || row.getCell(3)==null || row.getCell(7)==null)
        {
            return  null;
        }
        if(row.getCell(5)==null || row.getCell(0)==null || row.getCell(1)==null)
        {
            return  null;
        }

        if(row.getCell(8)==null || row.getCell(4)==null || row.getCell(6)==null)
        {
            return  null;
        }
        entity.setRoleId(2L);
        entity.setIssub(0);
        entity.setReview(1);
        entity.setEnable(2);
        entity.setIsReferrer(1);
        entity.setRoleName("个人科研人员（医护）");
        entity.setUserName(row.getCell(0).toString()); //名称
        entity.setUserHospital( row.getCell(1).toString());//所属医院
        entity.setUserDepartment( row.getCell(2).toString()); //所属科室
        entity.setUserPosition( row.getCell(3).toString()); //所属职位
        entity.setUserBankCardNumber(row.getCell(9) !=null?row.getCell(9).toString():row.createCell(9).toString()); //银行卡号
        entity.setUserCardholderIdCard(row.getCell(12) !=null?row.getCell(12).toString():row.createCell(12).toString()); //持卡人身份证
        entity.setProvinces(row.getCell(13) !=null?row.getCell(13).toString():row.createCell(13).toString());//省
        entity.setMunicipalities(row.getCell(14) !=null?row.getCell(14).toString():row.createCell(14).toString()); //市
         entity.setUserCardholderName(row.getCell(10) !=null?row.getCell(10).toString():row.createCell(10).toString());//持卡人姓名
        entity.setUserCardholderPhone(row.getCell(11) !=null?row.getCell(11).toString():row.createCell(11).toString()); //持卡人电话
        entity.setDistricts(row.getCell(15) !=null?row.getCell(15).toString():row.createCell(15).toString()); //区
        entity.setPhone(row.getCell(5).toString()); //电话
        entity.setGrade(row.getCell(7).toString());//等级
        entity.setNature(row.getCell(8).toString());//性质
        entity.setTitle(row.getCell(4).toString());//职称
        entity.setUserEmail(row.getCell(6).toString()); //邮箱
        entity.setPassword("123456");
      return  entity;
    }
    private  UsersForm GetHSSFCell(HSSFRow row)
    {
        UsersForm entity=new UsersForm();
        entity.setReview(1);
        entity.setEnable(2);
        entity.setIsReferrer(1);
        entity.setPassword("123456");
        if(row.getCell(5)==null || row.getCell(0)==null || row.getCell(1)==null)
        {
            return  null;
        }
        if(row.getCell(2)==null || row.getCell(3)==null || row.getCell(7)==null)
        {
            return  null;
        }
        if(row.getCell(8)==null || row.getCell(4)==null || row.getCell(6)==null)
        {
            return  null;
        }

        entity.setPhone( row.getCell(5).toString()); //电话
        entity.setUserName( row.getCell(0).toString()); //名称
        entity.setUserHospital( row.getCell(1).toString());//所属医院
        entity.setUserDepartment( row.getCell(2).toString()); //所属科室
        entity.setUserPosition( row.getCell(3).toString()); //所属职位
        entity.setGrade(row.getCell(7).toString());//等级
        entity.setNature(row.getCell(8).toString());//性质
        entity.setTitle(row.getCell(4).toString());//职称
        entity.setUserEmail(row.getCell(6).toString()); //邮箱
        entity.setUserBankCardNumber(row.getCell(9) !=null?row.getCell(9).toString():row.createCell(9).toString()); //银行卡号
        entity.setUserCardholderName(row.getCell(10) !=null?row.getCell(10).toString():row.createCell(10).toString());//持卡人姓名
        entity.setUserCardholderPhone(row.getCell(11) !=null?row.getCell(11).toString():row.createCell(11).toString()); //持卡人电话
        entity.setUserCardholderIdCard(row.getCell(12) !=null?row.getCell(12).toString():row.createCell(12).toString()); //持卡人身份证
        entity.setProvinces(row.getCell(13) !=null?row.getCell(13).toString():row.createCell(13).toString());//省
        entity.setMunicipalities(row.getCell(14) !=null?row.getCell(14).toString():row.createCell(14).toString()); //市
        entity.setDistricts(row.getCell(15) !=null?row.getCell(9).toString():row.createCell(15).toString()); //区
        entity.setRoleId(2L);
        entity.setIssub(0);
        entity.setRoleName("个人科研人员（医护）");
        return  entity;
    }

    /***
     *  医院
     */
    private UsersForm GetHospitalXSSFCell(XSSFRow row)
    {
        if(row.getCell(0) ==null || row.getCell(1)==null || row.getCell(5)==null)
        {
            return  null;
        }
        if(row.getCell(2) ==null || row.getCell(3)==null|| row.getCell(4)==null)
        {
            return  null;
        }

        UsersForm entity=new UsersForm();
        entity.setRoleId(3L);
        entity.setIssub(0);
        entity.setRoleName("经营或制造企业投资机构");
        entity.setUserName(row.getCell(0).toString());
        entity.setPhone(row.getCell(1).toString());
        entity.setPassword("123456");
        entity.setCompanyName(row.getCell(2).toString());
        entity.setCompanyCode(row.getCell(3).toString());
        entity.setContacts(row.getCell(4).toString());
        entity.setContact_phone(row.getCell(5).toString());
        entity.setReview(1);
        entity.setEnable(2);
        entity.setIsReferrer(1);
        return  entity;
    }
    private UsersForm GetHospitalHSSFCell(HSSFRow row)
    {
        if(row.getCell(2) ==null || row.getCell(3)==null|| row.getCell(4)==null){
            return  null;
        }
        if(row.getCell(0) ==null || row.getCell(1)==null || row.getCell(5)==null){
            return  null;
        }
        UsersForm entity=new UsersForm();
        entity.setUserName(row.getCell(0).toString());
        entity.setPhone(row.getCell(1).toString());
        entity.setPassword("123456");
        entity.setCompanyName(row.getCell(2).toString());
        entity.setCompanyCode(row.getCell(3).toString());
        entity.setContact_phone(row.getCell(4).toString());
        entity.setContacts(row.getCell(5).toString());
        entity.setRoleId(3L);
        entity.setIssub(0);
        entity.setRoleName("经营或制造企业投资机构");
        entity.setReview(1);
        entity.setEnable(2);
        entity.setIsReferrer(1);
        return  entity;
    }

    /**
     *  医疗机构
     * @param row
     * @return
     */
    private  UsersForm GetMedicalXSSFCell(XSSFRow row){

        if(row.getCell(2) ==null || row.getCell(3)==null|| row.getCell(4)==null){
            return  null;
        }
        UsersForm entity=new UsersForm();
        if(row.getCell(0) ==null || row.getCell(1)==null || row.getCell(5)==null){
            return  null;
        }
        entity.setRoleId(4L);
        entity.setRoleName("医疗机构");
        entity.setPassword("123456");
        entity.setIssub(0);
        entity.setReview(1);
        entity.setEnable(2);
        entity.setIsReferrer(1);
        entity.setUserName(row.getCell(0).toString());
        entity.setPhone(row.getCell(1).toString());
        entity.setOrganizationName(row.getCell(2).toString());
        entity.setOrganizationCode(row.getCell(3).toString());
        entity.setContacts(row.getCell(4).toString());
        entity.setContact_phone(row.getCell(5).toString());
        entity.setDetailed_address(row.getCell(6)!=null?row.getCell(6).toString():"");
        return entity;
    }
    private  UsersForm GetMedicalHSSFCell(HSSFRow row){

        if(row.getCell(0) ==null || row.getCell(1)==null || row.getCell(5)==null){
            return  null;
        }
        UsersForm entity=new UsersForm();
        if(row.getCell(2) ==null || row.getCell(3)==null|| row.getCell(4)==null){
            return  null;
        }
        entity.setUserName(row.getCell(0).toString());
        entity.setPhone(row.getCell(1).getCellStyle().toString());
        entity.setContacts(row.getCell(4).toString());
        entity.setContact_phone(row.getCell(5).toString());
        entity.setOrganizationName(row.getCell(2).toString());
        entity.setOrganizationCode(row.getCell(3).toString());
        entity.setDetailed_address(row.getCell(6)!=null?row.getCell(6).toString():"");
        entity.setRoleId(4L);
        entity.setRoleName("医疗机构");
        entity.setPassword("123456");
        entity.setIssub(0);
        entity.setReview(1);
        entity.setEnable(2);
        entity.setIsReferrer(1);
        return entity;
    }
}
