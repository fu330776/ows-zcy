package com.goodsogood.ows.controller;

import com.goodsogood.log4j2cm.annotation.HttpMonitorLogger;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.CacheConfiguration;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.MD5Utils;
import com.goodsogood.ows.helper.SmsUtils;
import com.goodsogood.ows.model.db.*;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/v-login")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "登录注册", tags = {"login manager"})
public class LoginController {

    private final Errors errors;
    private final UsersService usersService;
    private final AccountsUsersRolesService accountsUsersRolesService;
    private final LoginService loginService;
    private final MenusService menusService;
    private final String Url = "http://www.ztsms.cn/sendNSms.do";
    private final String pwd = "zcyun2019GS";
    private final String productid = "95533";
    private final String username = "zcyun";
    private Map<String, String> map;

    @Autowired
    public LoginController(Errors errors, UsersService userService, AccountsUsersRolesService aurService, LoginService logins, MenusService menus) {
        this.errors = errors;
        this.usersService = userService;
        this.accountsUsersRolesService = aurService;
        this.loginService = logins;
        this.menusService = menus;
    }

    /**
     * 注册账号(推荐人分享)
     */
    @ApiOperation(value = "注册账号")
    @HttpMonitorLogger
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> addUser(@Valid @RequestBody UsersForm user, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (user.phoneCode.isEmpty()) {
            throw new ApiException("验证码，不可为空！", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(this.usersService.Register(user), errors);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    /**
     * 注册账号(管理员分享)
     */
    @ApiOperation(value = "注册账号")
    @PostMapping("/addAdmin")
    public ResponseEntity<Result<Boolean>> addAdminUser(@Valid @RequestBody UsersForm user, BindingResult bindingResult) {

        log.debug("bindingResult->{}", bindingResult);
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bool = this.usersService.AdminRegister(user);
        if (!bool) {
            throw new ApiException("注册失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 登录校验是否存在多个身份
     */
    @ApiOperation(value = "登录校验是否存在多个身份")
    @GetMapping("/isLoigin")
    public ResponseEntity<Result<List<RoleUserEntity>>> IsLoigin(@Valid @RequestBody LoginForm user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (user.Phone == null || user.Phone.isEmpty()) {
            throw new ApiException("账号不能为空或空字符串", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        List<RoleUserEntity> rlist = null;
        if ((user.code == null || user.code.isEmpty()) && !user.Password.isEmpty()) {
            rlist = this.loginService.getList(user.Phone, MD5Utils.MD5(user.Password));
            if (rlist == null) {
                throw new ApiException("服务器繁忙，登录失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
            }
        }
        if (!user.code.isEmpty() && user.Password.isEmpty()) {
            rlist = this.loginService.getListPhone(user.Phone, user.code);
            if (rlist == null) {
                throw new ApiException("服务器繁忙，登录失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
            }
        }
        Result<List<RoleUserEntity>> result = new Result<>(rlist, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录，返回菜单")
    @GetMapping("/index")
    public ResponseEntity<Result<UserMenusVo>> Login(@Valid @RequestBody Long userid, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        List<MenusEntity> entity = this.menusService.GetMenus(userid);
        UserMenusVo userMenusVo = new UserMenusVo();
        userMenusVo.time = CacheConfiguration.GetDate(60 * 60 * 1000L);
        userMenusVo.token = Md5Crypt.apr1Crypt(userid.toString()) + userMenusVo.time;
        userMenusVo.userId = userid;
        userMenusVo.entity = entity;
        CacheConfiguration.cache.put(userMenusVo.token, userMenusVo);
        Result<UserMenusVo> result = new Result<>(userMenusVo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 身份效验
     */
    @ApiOperation(value = "根据账号及权限ID 查询身份是否存在")
    @GetMapping("/isIdentity")
    public ResponseEntity<Result<Boolean>> IsIdentity(@Valid @RequestBody IdentityForm identityForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        AccountsUsersRolesEntity accountsUsersRolesEntity = this.accountsUsersRolesService.Get(identityForm.Phone, identityForm.RoleId);
        if (accountsUsersRolesEntity != null) {
            throw new ApiException("该账号已存在，请重新选择", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }




    /**
     * 获取验证码
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取验证码")
    @PostMapping(value = "/code")
    public ResponseEntity<Result<SmssEntity>> getCode(@Valid @RequestBody CodeForm entity) throws Exception {
        Date time = new Date();
        getResArgs(entity.getMobile(), "");
        String code = map.get("code");
        String Phone = map.get("phoneNum");
        String content = "验证码：" + code + "，请不要把验证码泄露给他人，谢谢！【知创云】";
        SmssEntity smsEntity = new SmssEntity();
        smsEntity.setSmsPhone(Phone);
        smsEntity.setSmsCode(code);
        smsEntity.setSmsContent(content);
        smsEntity.setAddtime(time);
        String sms = SmsUtils.postEncrypt(Url, username, pwd, Phone, content, productid);
        Integer getCode = Integer.parseInt(sms.split(",")[0]);
        if (getCode == 1) {

            smsEntity.setSmsSendType(1); //短信发送类型 1、文字 2、语音
            smsEntity.setSmsType(1); //短信类型 1、注册账号 2、修改密码
            smsEntity.setSmsSendFrequency(1); //发送次数
            smsEntity.setSmsUse(1);
            smsEntity.setSmsExpireDate(stampToDate(Long.parseLong(sms.split(",")[1])));    //失效时间
            smsEntity = this.usersService.SmsInsert(smsEntity, 1);
        }
        Result<SmssEntity> result = new Result<>(smsEntity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public String getResArgs(String phone, String code) {
        //生成6位数验证码
        String codeNew = String.valueOf(new Random().nextInt(899999) + 100000);
        if (code != "") {
            codeNew = code;
        }
        map = new HashMap<>();
        map.put("code", codeNew);
        map.put("phone", phone);
        return codeNew;
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
