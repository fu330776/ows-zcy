package com.goodsogood.ows.controller;

import com.goodsogood.log4j2cm.annotation.HttpMonitorLogger;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.UserEntity;
import com.goodsogood.ows.model.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author xuliduo
 * @date 08/03/2018
 * @description class UserController
 */
@RestController()
@RequestMapping("/user")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "错误拦截的demo", tags = {"error demo"})
public class ErrorController {

    private final Errors errors;

    @Autowired
    public ErrorController(Errors errors) {
        this.errors = errors;
    }

    @HttpMonitorLogger
    @GetMapping("/su/{id}")
    public ResponseEntity<Result<UserEntity>> getUserById(
            @ApiParam(name = "用户id", required = true) @PathVariable long id) {
        log.debug(id);
        // success
        return new ResponseEntity<>(new Result<>(new UserEntity(), errors), HttpStatus.OK);
    }

    @HttpMonitorLogger
    @GetMapping("/ex1/{id}")
    public ResponseEntity<Result<UserEntity>> getUserByIdEx1(
            @ApiParam(name = "用户id", required = true) @PathVariable long id) {
        log.debug(id);
        Result<UserEntity> userDoResult = new Result<>(errors, Global.Errors.NOT_FOUND, HttpStatus.NOT_FOUND.value(), new String[]{String.valueOf(id), "用户"});
        userDoResult.setPath("/ex1/" + id);
        // one
        return new ResponseEntity<>(userDoResult, HttpStatus.NOT_FOUND);
    }


    @HttpMonitorLogger
    @GetMapping("/ex2/{id}")
    public ResponseEntity<Result<UserEntity>> getUserByIdEx2(
            @ApiParam(name = "用户id", required = true) @PathVariable long id) {
        log.debug(id);
        //two
        throw new ApiException("not found", new Result<>(errors, Global.Errors.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
    }

    @HttpMonitorLogger
    @GetMapping("/ex3/{id}")
    public ResponseEntity<Result<UserEntity>> getUserByIdEx3(
            @ApiParam(name = "用户id", required = true) @PathVariable long id) {
        log.debug(id);
        //tree
        throw new NullPointerException("not found");
    }

}
