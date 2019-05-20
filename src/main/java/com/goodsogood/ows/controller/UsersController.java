package com.goodsogood.ows.controller;

import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.UsersEntity;
import com.goodsogood.ows.model.vo.PwdFrom;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.model.vo.UserinfoForm;
import com.goodsogood.ows.service.UsersService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.ResultContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v-users")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "用户信息操作", tags = {"操作用户信息"})
public class UsersController {

    private final Errors errors;
    private final UsersService usersService;

    @Autowired
    public UsersController(Errors error, UsersService users) {
        this.usersService = users;
        this.errors = error;
    }

    /**
     * 修改个人信息
     */
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


}
