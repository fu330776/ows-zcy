package com.goodsogood.ows.controller;

import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.MoneyEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.MoneyAddForm;
import com.goodsogood.ows.model.vo.MoneyPutForm;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.service.MoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v-put")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "配置", tags = {" manager"})
public class SetUpController {
    private final Errors errors;
    private final MoneyService service;

    @Autowired
    public SetUpController(MoneyService moneyService, Errors errors) {
        this.service = moneyService;
        this.errors = errors;
    }

    @ApiOperation(value = "配置")
    @PostMapping(value = "/add")
    public ResponseEntity<Result<Boolean>> Add(@Valid @RequestBody MoneyAddForm form, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new com.goodsogood.ows.model.vo.Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        MoneyEntity entity = new MoneyEntity();
        entity.setDel(1);
        entity.setMoney(form.getMoney());
        entity.setName(form.getName());
        entity.setTime(new Date());
        Integer num = this.service.Add(entity);
        Result<Boolean> result = new Result<>(true, errors);
        if (num == null | num == 0) {
            result = new Result<>(false, errors);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/put")
    public ResponseEntity<Result<Boolean>> Add(@Valid @RequestBody MoneyPutForm form, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new com.goodsogood.ows.model.vo.Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Integer num = this.service.Put(form.getId(), form.getMoney());
        Result<Boolean> result = new Result<>(true, errors);
        if (num == 0 | num == null) {
            result = new Result<>(false, errors);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/get/{page}")
    public ResponseEntity<Result<PageInfo<MoneyEntity>>> Get(@ApiParam(value = "page", required = true)
                                                             @PathVariable Integer page, Integer pageSize) {
        if (page == null||pageSize==null) {
            page = 1;
            pageSize=10;
        }
        PageInfo<MoneyEntity> entityPageInfo = this.service.Get(new PageNumber(page, pageSize));
        Result<PageInfo<MoneyEntity>> result = new Result<>(entityPageInfo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "idea 经费查询")
    @PostMapping("/getByIdea")
    public ResponseEntity<Result<MoneyEntity>> GetByIdea() {
        return new ResponseEntity<>(new Result<>(this.service.GetFind("idea"), errors), HttpStatus.OK);
    }

}
