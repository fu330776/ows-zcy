package com.goodsogood.ows.controller;

import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.CacheConfiguration;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.AesEncryptUtils;
import com.goodsogood.ows.helper.HeaderHelper;
import com.goodsogood.ows.helper.MD5Utils;
import com.goodsogood.ows.model.db.*;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

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

    @Autowired
    public LoginController(Errors errors, UsersService userService, AccountsUsersRolesService aurService, LoginService logins, MenusService menus) {
        this.errors = errors;
        this.usersService = userService;
        this.accountsUsersRolesService = aurService;
        this.loginService = logins;
        this.menusService = menus;
    }

    /**
     * 注册账号
     */
    @ApiOperation(value = "注册账号")
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> addUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody UsersForm user, BindingResult bindingResult) {
        log.debug("header[_cl]->{}", headers.get(HeaderHelper.OPERATOR_CHANNEL));
        log.debug("bindingResult->{}", bindingResult);
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }

        Result<Boolean> result = new Result<>(this.usersService.Register(user), errors);
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


}
