package com.goodsogood.ows.controller;

import com.github.pagehelper.Page;
import com.goodsogood.log4j2cm.annotation.HttpMonitorLogger;
import com.goodsogood.ows.annotation.RepeatedCheck;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.HeaderHelper;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.UserEntity;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.model.vo.UserForm;
import com.goodsogood.ows.service.UserService;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * @author xuliduo
 * @date 15/03/2018
 * @description class ValidateController
 */
@RestController
@RequestMapping("/v-user")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "带通用验证的用户管理", tags = {"user manager"})
// 如果需要对请求参数PathVariable进行验证，需要加上@Validated注解
// 如果要使用 @NotBlank4Channel 那么不能使用 @Validated，只能自己在方法体里面处理对象
//@Validated
public class UserController {

    private final Errors errors;
    private final UserService userService;


    @Autowired
    public UserController(Errors errors, UserService userService) {
        this.errors = errors;
        this.userService = userService;
    }

    @HttpMonitorLogger
    @PutMapping("/test")
    public ResponseEntity<Result<UserEntity>> userForm(@Valid @ModelAttribute("user") UserForm userForm, BindingResult bindingResult) {
        log.debug("bindingResult -> {}", bindingResult);
        UserEntity userDo = new UserEntity();
        userDo.setUserId(1L);
        userDo.setName(userForm.getName());
        userDo.setPassword("123456");
        return new ResponseEntity<>(new Result<>(userDo, errors), HttpStatus.OK);
    }

    @HttpMonitorLogger
    @GetMapping("/get/{id}")
    public ResponseEntity<Result<UserEntity>> getOne(
            @ApiParam(value = "用户id", required = true)
            @PathVariable
            // 对请求参数进行验证
            @Min(value = 1, message = "{Min.user.id}")
                    Long id
    ) {
        UserEntity userDo;
        try {
            userDo = Preconditions.checkNotNull(this.userService.getOne(id));
        } catch (NullPointerException npe) {
            throw new ApiException("not found", new Result<>(errors, Global.Errors.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "用户"));
        }
        Result<UserEntity> result = new Result<>(userDo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 添加方法的demo
     *
     * @param user
     * @param bindingResult 注意，如果要使用通用验证框架，必须在方法中接收BindingResult对象
     * @return
     */
    @HttpMonitorLogger
    @PutMapping("/add")
    public ResponseEntity<Result<UserEntity>> addUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserForm user, BindingResult bindingResult) {
        log.debug("header[_cl]->{}", headers.get(HeaderHelper.OPERATOR_CHANNEL));
        log.debug("bindingResult->{}", bindingResult);
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        UserEntity userDo = new UserEntity();
        userDo.setName(user.getName());
        userDo.setPassword("12345");
        userDo.setAge(user.getAge());
        userDo.setGender(user.getGender());
        userDo.setPhone(user.getPhone());

        userDo = userService.insert(userDo);
        log.debug(userDo.getUserId());
        Result<UserEntity> result = new Result<>(userDo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @HttpMonitorLogger
    @GetMapping("")
    public ResponseEntity<Result<Page<UserEntity>>> getAll(@RequestParam(required = false) Integer page) {
        // 处理下页数
        if (page == null) {
            page = 0;
        }
        Page<UserEntity> users = userService.getAll(new PageNumber(page));
        log.debug("users-> pn:{},ps:{},pz:{},sr:{},er:{},total:{}"
                , users.getPageNum()
                , users.getPages()
                , users.getPageSize()
                , users.getStartRow()
                , users.getEndRow()
                , users.getTotal()
        );

        Result<Page<UserEntity>> result = new Result<>(users, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 更新数据
     *
     * @param userEntity    需要更新的对象
     * @param bindingResult bindingResult 注意，如果要使用通用验证框架，必须在方法中接收BindingResult对象
     * @return 更新后的结果
     */
    @HttpMonitorLogger
    @PostMapping("/update")
    @RepeatedCheck(check = true, time = 1)
    public ResponseEntity<Result<Boolean>> update(@Valid UserEntity userEntity, BindingResult bindingResult) {
        int i = this.userService.update(userEntity);
        log.debug("更新结果-> {}", i);
        Result<Boolean> result = new Result<>(i > 0, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
