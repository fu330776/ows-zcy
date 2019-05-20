package com.goodsogood.ows.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.HttpUtil;
import com.goodsogood.ows.model.db.SmssEntity;
import com.goodsogood.ows.model.db.UsersEntity;
import com.goodsogood.ows.model.db.VerificationCode;
import com.goodsogood.ows.model.vo.LoginVo;
import com.goodsogood.ows.model.vo.PwdFrom;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.model.vo.UserinfoForm;
import com.goodsogood.ows.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.ResultContext;
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
@Api(value = "用户信息操作", tags = {"操作用户信息"})
public class UsersController {

    private final Errors errors;
    private final UsersService usersService;
    private final String secret_key = "";
    private final Integer template_id = 30;
    private final String url = "";
    private Map<String, String> map;

    @Autowired
    public UsersController(Errors error, UsersService users) {
        this.usersService = users;
        this.errors = error;
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
        entity.setUserName(user.userName);
        entity.setAddtime(new Date());
        entity.setCompanyCode(user.companyCode);
        entity.setCompanyName(user.companyName);
        entity.setEnable(user.enable);
        entity.setIsReferrer(user.isReferrer);
        entity.setOrganizationCode(user.organizationCode);
        entity.setOrganizationName(user.organizationName);
        entity.setReferrer(user.referrer);
        entity.setReview(user.review);
        entity.setUpdatetime(new Date());
        entity.setUserBankCardNumber(user.userBankCardNumber);
        entity.setUserCardholderIdcard(user.userCardholderIdcard);
        entity.setUserCardholderName(user.userCardholderName);
        entity.setUserCardholderPhone(user.userCardholderPhone);
        entity.setUserDepartment(user.userDepartment);
        entity.setUserEmail(user.userEmail);
        entity.setUserHospital(user.userHospital);
        entity.setUserPosition(user.userPosition);
        entity.setCode(user.code);
        entity.setUserId(user.userId);
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
        Integer isNum = this.usersService.UpdatePassWord(pwd.Phone, pwd.NewPwd, pwd.code);
        if (isNum <= 0) {
            throw new ApiException("服务器繁忙，修改失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(true, errors);
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
        Integer isNum = this.usersService.UpdatePassWord(pwd.userId, pwd.NewPwd);
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

        String entity_v = HttpUtil.postEncrypt(restTemplate, url, getResArgs(entity.getMobile(), ""), headers, new TypeReference<String>() {
        });
        ObjectMapper mapper = new ObjectMapper();
        VerificationCode verificationCode = mapper.readValue(entity_v, VerificationCode.class);
        String code = map.get("code");
        String sms = "【知创云】验证码：" + code + "，请不要把验证码泄露给他人，谢谢！";
        SmssEntity smsEntity = new SmssEntity();
        smsEntity.setSmsPhone(map.get("phoneNum"));
        smsEntity.setSmsCode(code);
        smsEntity.setSmsContent(sms);
        smsEntity.setAddtime(time);

        if (verificationCode.getCode() == 0) {

            smsEntity.setSmsSendType(1); //短信发送类型 1、文字 2、语音
            smsEntity.setSmsType(2); //短信类型 1、注册账号 2、修改密码
            smsEntity.setSmsSendFrequency(1); //发送次数
            smsEntity.setSmsUse(1);
            smsEntity.setSmsExpireDate(stampToDate(verificationCode.getTimestamp()));    //失效时间

        }
        SmssEntity smsEntities = this.usersService.SmsInsert(smsEntity, 2);
        Result<SmssEntity> result = new Result<>(smsEntities, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


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
    public Date stampToDate(Long s) {
        Date date = new Date(s);
        date.setTime(date.getTime() + 10 * 60 * 1000);
        return date;
    }

}
