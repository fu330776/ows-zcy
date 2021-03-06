package com.goodsogood.ows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.SmsUtils;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.SmssEntity;
import com.goodsogood.ows.model.db.UsersEntity;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.UsersService;
import com.goodsogood.ows.service.WithdrawsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/v-users")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "Users", tags = {"操作用户信息"})
public class UsersController {

    private final Errors errors;
    private final UsersService usersService;
    private final WithdrawsService withdrawsService;
    private final String secret_key = "";
    private final Integer template_id = 30;
    private final String url = "";
    private Map<String, String> map;

    @Autowired
    public UsersController(Errors error, UsersService users, WithdrawsService withdrawsService) {
        this.usersService = users;
        this.errors = error;
        this.withdrawsService = withdrawsService;
    }

    /**
     * 修改个人信息
     */
    @ApiOperation(value = "修改个人信息")
    @PostMapping(value = "/alterData")
    public ResponseEntity<Result<Boolean>> UpdateData(@Valid @RequestBody UserinfoForm user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        //注册资料
        UsersEntity entity = new UsersEntity();
        entity.setUserName(user.getUserName());
        entity.setAddtime(new Date());
        entity.setCompanyCode(user.getCompanyCode());
        entity.setCompanyName(user.getCompanyName());
        entity.setEnable(user.getEnable());
        entity.setUserCardholderIdcard(user.getUserCardholderIdcard());
        entity.setUserCardholderName(user.getUserCardholderName());
        entity.setUserCardholderPhone(user.getUserCardholderPhone());
        entity.setIsReferrer(user.getIsReferrer());
        entity.setReferrer(user.getReferrer());
        entity.setReview(user.getReview());
        entity.setOrganizationCode(user.getOrganizationCode());
        entity.setOrganizationName(user.getOrganizationName());
        entity.setUpdatetime(new Date());
        entity.setUserBankCardNumber(user.getUserBankCardNumber());
        entity.setUserDepartment(user.getUserDepartment());
        entity.setUserHospital(user.getUserHospital());
        entity.setUserPosition(user.getUserPosition());
        entity.setUserEmail(user.getUserEmail());
        entity.setCode(user.getCode());
        entity.setUserId(user.getUserId());
        entity.setBusiness_license(user.getBusiness_license());
        entity.setDetailed_address(user.getDetailed_address());
        entity.setContacts(user.getContacts());
        entity.setContact_phone(user.getContact_phone());
        entity.setTitle(user.getTitle());
        entity.setNature(user.getNature());
        entity.setGrade(user.getGrade());
        entity.setDistricts(user.getDistricts());
        entity.setProvinces(user.getProvinces());
        entity.setIssub(user.getIssub());
        entity.setMunicipalities(user.getMunicipalities());
        Integer num = this.usersService.Update(entity);
        if (num <= 0) {
            throw new ApiException("个人信息修改失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "忘记密码（修改密码）")
    @PostMapping(value = "/alterPassWord")
    public ResponseEntity<Result<Boolean>> UpdatePassWord(@Valid @RequestBody PwdFrom pwd, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Integer isNum = this.usersService.UpdatePassWord(pwd.getPhone(), pwd.getPwd(), pwd.getCode());
        Result<Boolean> result;
        if (isNum == null) {
            result = new Result<>(false, errors);
        } else {
            result = new Result<>(true, errors);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 登录后修改密码
     */
    @ApiOperation(value = "登录后修改密码（修改密码）")
    @PostMapping(value = "/alterPwd")
    public ResponseEntity<Result<Boolean>> UpdatePwd(@Valid @RequestBody PwdFrom pwd, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Integer isNum = this.usersService.UpdatePassWord(pwd.getUserId(), pwd.getPwd());
        if (isNum <= 0) {
            throw new ApiException("服务器繁忙，修改失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    /**
     * 获取验证码（修改密码）
     */
    @ApiOperation(value = "获取验证码（修改密码）")
    @PostMapping(value = "/code")
    public ResponseEntity<Result<SmssEntity>> getCode(@Valid @RequestBody LoginVo entity, BindingResult bindingResult) throws Exception {
        Date time = new Date();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

//        String entity_v = HttpUtil.postEncrypt(restTemplate, url, getResArgs(entity.getMobile(), ""), headers, new TypeReference<String>() {
//        });

        ObjectMapper mapper = new ObjectMapper();
//        VerificationCode verificationCode = mapper.readValue(entity_v, VerificationCode.class);
        String code = map.get("code");
        SmssEntity smsEntity = this.usersService.SmsGet(entity.getMobile(), 2);
        if (smsEntity == null) {
            smsEntity = new SmssEntity();
            smsEntity.setSmsPhone(map.get("phoneNum"));
            smsEntity.setSmsCode(code);
            smsEntity.setAddtime(time);
        } else {
            code = smsEntity.getSmsCode();
        }
        String sms = "【知创云】验证码：" + code + "，请不要把验证码泄露给他人，谢谢！";
        smsEntity.setSmsContent(sms);
        String smsq = SmsUtils.SendSms(entity.getMobile(), "知创云", "SMS_171115994", code);
        SmsForm getCodes = mapper.readValue(smsq, SmsForm.class);
        Result<SmssEntity> result = null;
        if (getCodes.getMessage().equals("OK")) {
            smsEntity.setSmsSendType(1); //短信发送类型 1、文字 2、语音
            smsEntity.setSmsType(2); //短信类型 1、注册账号 2、修改密码
            smsEntity.setSmsSendFrequency(1); //发送次数
            smsEntity.setSmsUse(1);
            smsEntity.setSmsExpireDate(stampToDate());    //失效时间
            SmssEntity smsEntities = this.usersService.SmsInsert(smsEntity, 2);
            result = new Result<>(smsEntities, errors);
        } else {
            result = new Result<>(null, errors);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
//    /**
//     * 获取验证码
//     *
//     * @param entity
//     * @return
//     * @throws Exception
//     */
//    @ApiOperation(value = "获取验证码")
//    @PostMapping(value = "/code")
//    public ResponseEntity<Result<SmssEntity>> getCode(@Valid @RequestBody CodeForm entity) throws Exception {
//        Date time = new Date();
//        getResArgs(entity.getMobile(), "");
//        String code = map.get("code");
//        String Phone = map.get("phone");
//        String content = "验证码：" + code + "，请不要把验证码泄露给他人，谢谢！【知创云】";
//        SmssEntity smsEntity  = this.usersService.SmsGet(Phone,2);
//        if(smsEntity == null)
//        {
//            smsEntity= new SmssEntity();
//            smsEntity.setSmsPhone(Phone);
//            smsEntity.setSmsContent(content);
//            smsEntity.setAddtime(time);
//        }else{
//            code=smsEntity.getSmsCode();
//        }
//        String sms = SmsUtils.SendSms(Phone,"知创云","SMS_171115994",code);
//        ObjectMapper mapper = new ObjectMapper(); //转换器
//        SmsForm getCodes=mapper.readValue(sms,SmsForm.class);
//        if (getCodes.getMessage().equals("OK")) {
//            smsEntity.setSmsSendType(1); //短信发送类型 1、文字 2、语音
//            smsEntity.setSmsType(entity.getType()); //短信类型 1、注册账号 2、修改密码
//            smsEntity.setSmsSendFrequency(1); //发送次数
//            smsEntity.setSmsUse(1);
//            smsEntity.setSmsExpireDate(stampToDate());    //失效时间
//            smsEntity = this.usersService.SmsInsert(smsEntity, entity.getType());
//        }
//        smsEntity.setSmsCode("123456");
//        Result<SmssEntity> result = new Result<>(smsEntity, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    public Map<String, Object> getResArgs(String phoneNum, String code) throws Exception {
        //生成6位数验证码
        String codeNew = String.valueOf(new Random().nextInt(899999) + 100000);
        if (code != "") {
            codeNew = code;
        }
        map = new HashMap<>();
        map.put("code", codeNew);
        map.put("phoneNum", phoneNum);
        //组装数据
        List<Map<String, String>> sms_placeholders = new ArrayList<>();
        Map<String, String> sms_placeholder = new HashMap<>();
        sms_placeholder.put("name", "code");
        sms_placeholder.put("value", codeNew);
        sms_placeholders.add(sms_placeholder);
        List<Map<String, Object>> sms_thirds = new ArrayList<>();
        Map<String, Object> sms_third = new HashMap<>();
        sms_third.put("third_id", phoneNum);
        sms_third.put("sms_placeholders", sms_placeholders);
        sms_thirds.add(sms_third);
        Map<String, Object> args = new HashMap<>();
        //模板 id
        args.put("template_id", template_id);
        //消息结果回调地址
//        args.put("notify_url","");
        //消息 id 指定了消息中心不会新增一个消息,会从之前的批次累积
//        if(!StringUtils.isBlank(recordId)||recordId!=null)args.put("record_id",recordId);
        //密匙
        args.put("secret_key", secret_key);
        //占位符和手机号
        args.put("sms_thirds", sms_thirds);
        return args;
    }

    /*
     * 将时间戳转换为时间并加10分钟
     */
    public Date stampToDate() {
        Date date = new Date();
        date.setTime(date.getTime() + 10 * 60 * 1000);
        return date;
    }

//    /**
//     * 提现
//     *
//     * @param wids
//     * @param bindingResult
//     * @return
//     */
//    @ApiOperation(value = "个人金额提现")
//    @PostMapping("/cash")
//    public ResponseEntity<Result<Boolean>> userCash(@Valid @RequestBody List<Long> wids, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        if (wids.size() <= 0) {
//            throw new ApiException("服务器繁忙，提现失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Boolean bool = false;
//        if (wids.size() == 1) {
//            bool = this.withdrawsService.cashOut(wids.get(0));
//        } else {
//            bool = this.withdrawsService.cashOuts(wids);
//        }
//        Result<Boolean> result = new Result<>(bool, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 获取当前登录用户 金额
//     *
//     * @param userId        用户唯一标识
//     * @param bindingResult
//     * @return
//     */
//    @ApiOperation("获取当前用户 金额信息")
//    @PostMapping("/getMoney")
//    public ResponseEntity<Result<UserMoneyVo>> getSumMoney(@Valid @RequestBody Long userId, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        UserMoneyVo vo = new UserMoneyVo();
//        vo.setNoMoney(this.withdrawsService.getSum(userId));
//        vo.setTooMoney(this.withdrawsService.getSumToo(userId));
//        Result<UserMoneyVo> result = new Result<>(vo, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }


    /**
     * 获取当前用户信息
     *
     * @param uId           用户唯一标识
     * @param bindingResult
     * @return
     */
    @ApiOperation("获取当前用户信息")
    @PostMapping("/getUsers")
    public ResponseEntity<Result<UserInfoVo>> getUserInfo(@Valid @RequestBody Long uId, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }

        UserInfoVo vo = this.usersService.GetByUser(uId);
        Result<UserInfoVo> result = new Result<>(vo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     *  获取所有账户信息
     * @return
     */
    @ApiOperation(value = "获取所有账户信息")
    @GetMapping("/getAll/{page}")
    public ResponseEntity<Result<PageInfo<UserInfoVo>>> getByAll(@ApiParam(value = "page", required = true)
                                                                 @PathVariable Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 10;
        }
        PageInfo<UserInfoVo> userInfoVos = this.usersService.GetAll(new PageNumber(page, pageSize));
        Result<PageInfo<UserInfoVo>> result = new Result<>(userInfoVos, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     *  根据角色获取 用户信息
     * @return
     */
    @ApiOperation(value = "根据角色获取 用户信息")
    @GetMapping("/getByRole/{roleId}")
    public ResponseEntity<Result<PageInfo<UserInfoVo>>> getByRoleAll(@ApiParam(value = "roleId", required = true)
                                                                     @PathVariable Long roleId, UserRoleForm user, Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 11;
        }
        PageInfo<UserInfoVo> userInfoVos = this.usersService.GetByRole(roleId, user.getName(), user.getProvinces(), user.getMunicipalities(), user.getDistricts(), user.getGrade(), user.getNature(), user.getKeyword(), user.getReview(), user.getEnable(), new PageNumber(page, pageSize));
        Result<PageInfo<UserInfoVo>> result = new Result<>(userInfoVos, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 根据角色查询用户信息  无分页
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "根据角色获取 用户信息")
    @GetMapping("/getByRoleNoPage/{roleId}")
    public ResponseEntity<Result<List<UserInfoVo>>> getByRoleNoPage(@ApiParam(value = "roleId", required = true)
                                                                    @PathVariable Long roleId) {
        List<UserInfoVo> vos = this.usersService.GetByRoleNoPage(roleId);
        Result<List<UserInfoVo>> result = new Result<>(vos, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /***
     *   获取子管理员信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @ApiOperation(value = "获取子管理员信息")
    @GetMapping("/getAdministrator")
    public ResponseEntity<Result<PageInfo<UserInfoVo>>> getAdministrator(Integer page, Integer pageSize, String name) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        PageInfo<UserInfoVo> pageInfo = this.usersService.GetAdministrator(name, new PageNumber(page, pageSize));
        Result<PageInfo<UserInfoVo>> result = new Result<>(pageInfo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @ApiOperation(value = "子管理 获取本医院下的用户信息")
    @GetMapping("/getIssue/{uid}")
    public ResponseEntity<Result<PageInfo<UserInfoVo>>> GetIssue(
            @ApiParam(value = "uid", required = true)
            @PathVariable Long uid
            , UserRoleForm user
            , Integer page
            , Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 12;
        }
        PageInfo<UserInfoVo> userInfoVos = this.usersService.GetByIssue(uid, user.getProvinces(), user.getMunicipalities(), user.getDistricts(), user.getGrade(), user.getNature(), user.getKeyword(), user.getReview(), user.getEnable(), new PageNumber(page, pageSize));
        Result<PageInfo<UserInfoVo>> result = new Result<>(userInfoVos, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 封禁账号（isdel=2） 解封账号 （isdel=1）
     *
     * @param user
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "封装or解封")
    @PostMapping("/close")
    public ResponseEntity<Result<Boolean>> isClose(@Valid @RequestBody UserCloseForm user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
//        UserInfoVo vo = this.usersService.GetByUser(user.getUserid());
//        if (vo == null) {
//            throw new ApiException("用户不存在或该账号已被封禁", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
        this.usersService.sealByUser(user.getUserid(), user.getIsdel());
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     *  账号删除
     * @param userId   用户ID
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "删除账号")
    @PostMapping("/delete")
    public ResponseEntity<Result<LoginResult>> Delete(@Valid @RequestBody Long userId, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(userId == null || userId <=0)
        {
            throw new ApiException("必填参数不可为空", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult=  this.usersService.DeleteHealthCare(userId);
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     * 医护审核人员身份 变更
     * @param auditorForm
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "设定or取消 医护审核身份")
    @PostMapping("/isAuditor")
    public  ResponseEntity<Result<LoginResult>>  IsAuditor(@Valid @RequestBody UserAuditorForm auditorForm,BindingResult bindingResult)
    {
        if (bindingResult.hasFieldErrors() || auditorForm.getIsNum() !=1 || auditorForm.getIsNum()  != 2) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(auditorForm.getUserId() == null || auditorForm.getIsNum() == null) {
            throw new ApiException("必填参数不可为空", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult =this.usersService.IsAuditors(auditorForm.getUserId(),auditorForm.getIsNum());
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);

    }


    /***
     *  医护审核员 查询 受邀请的人
     * @param userId
     * @param user
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "医护审核员 查询 受邀请的人")
    @GetMapping("/getByInvitation/{userId}")
    public  ResponseEntity<Result<PageInfo<UserInfoVo>>> GetByInvitation(@ApiParam(value = "userId", required = true)
                                                                         @PathVariable Long userId , UserRoleForm user
                                                                        , Integer page, Integer pageSize){
        if(page == null || pageSize == null){
            page = 1;
            pageSize = 20;
        }
        PageInfo<UserInfoVo> userInfoVos = this.usersService.GetByInvitation(userId,user,new PageNumber(page,pageSize));
        Result<PageInfo<UserInfoVo>> result = new Result<>(userInfoVos,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     *  查询 提示框是否需提示
     * @param num
     * @return
     */
    @ApiOperation(value = "是否需要提示框")
    @GetMapping("/getIsTips/{num}")
    public  ResponseEntity<Result<LoginResult>> GetIsTips(@ApiParam(value = "num", required = true)
                                                          @PathVariable Long num ){
        LoginResult loginResult =this.usersService.GetIsTips(num);
        Result<LoginResult> result = new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     * 修改提示框 是否显示
     * @param form
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "修改提示框 是否显示")
    @PostMapping("/alterIsTips")
    public  ResponseEntity<Result<LoginResult>> AlterIsTips(@Valid @RequestBody TipsForm form,BindingResult bindingResult){
        if (bindingResult.hasFieldErrors() || form.getUserId() == null || form.getIsNum() < 0) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult = this.usersService.AlterIsTips(form.getUserId(),form.getIsNum());
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }



    /**
     * 账号审核
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "账号审核")
    @PostMapping("/toExamine")
    public ResponseEntity<Result<Boolean>> ToExamine(@Valid @RequestBody ExamineForm form, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean b = this.usersService.ExamineService(form.getUserId(), form.getIsDel());
        if (b) {
            return new ResponseEntity<>(new Result<>(true, errors), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result<>(false, errors), HttpStatus.OK);
    }

    /**
     * 初始化密码 默认密码为 123456
     *
     * @param userId        账号唯一标识
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "初始化密码")
    @PostMapping("/InitPwd")
    public ResponseEntity<Result<Boolean>> Initialization(@Valid @RequestBody Long userId, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Integer isb = this.usersService.UpdatePassWord(userId, "123456");
        Boolean b = true;
        if (isb == 0 || isb == null) {
            b = false;
        }
        Result<Boolean> result = new Result<>(b, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
