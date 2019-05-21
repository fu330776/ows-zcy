package com.goodsogood.ows.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.CacheConfiguration;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.HeaderHelper;
import com.goodsogood.ows.helper.HttpUtil;
import com.goodsogood.ows.model.db.*;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/v-login")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "登录注册", tags = {"user manager"})
public class LoginController {

    private final Errors errors;
    private final UsersService usersService;
    private final AccountsUsersRolesService accountsUsersRolesService;
    private final LoginService loginService;
    private final MenusService menusService;
    private final String secret_key = "";
    private final Integer template_id = 30;
    private final String url = "";
    private Map<String, String> map;


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
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> addUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody UsersForm user, BindingResult bindingResult) {
        log.debug("header[_cl]->{}", headers.get(HeaderHelper.OPERATOR_CHANNEL));
        log.debug("bindingResult->{}", bindingResult);
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
    public ResponseEntity<Result<Boolean>> addAdminUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody UsersForm user, BindingResult bindingResult) {
        log.debug("header[_cl]->{}", headers.get(HeaderHelper.OPERATOR_CHANNEL));
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
            rlist = this.loginService.getList(user.Phone, user.Password);
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

    @ApiOperation(value = "获取验证码")
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
            smsEntity.setSmsType(1); //短信类型 1、注册账号 2、修改密码
            smsEntity.setSmsSendFrequency(1); //发送次数
            smsEntity.setSmsUse(1);
            smsEntity.setSmsExpireDate(stampToDate(verificationCode.getTimestamp()));    //失效时间

        }
        SmssEntity smsEntities = this.usersService.SmsInsert(smsEntity, 1);
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


    /**
     * 维信授权页面
     *
     * @param response
     */
  /*  @ApiOperation(value = "跳转到微信授权页面")
    @RequestMapping("/authorization")
    public void wxAuthorization(HttpServletResponse response) {
        //回调地址
        try {
            String redirect_url = URLEncoder.encode("http://www.baidu.com", "UTF-8");
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid=APPID" +
                    "&redirect_uri=REDIRECT_URI" +
                    "&response_type=code" +
                    "&scope=SCOPE" +
                    "&state=123#wechat_redirect";
            response.sendRedirect(url.replace("APPID", "wx9baa227ef518b9cc").replace("REDIRECT_URL", redirect_url).replace("SCOPE", "snsapi_userinfo"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/


}
