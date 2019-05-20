package com.goodsogood.ows.controller;

import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.DemandsEntity;
import com.goodsogood.ows.model.vo.DemandsForm;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.service.DemandsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/v-demands")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "需求操作", tags = {"Demands manager"})
public class DemandsController {
    private final DemandsService service;
    private final Errors errors;

    @Autowired
    public DemandsController(DemandsService demandsService, Errors error) {
        this.service = demandsService;
        this.errors = error;
    }

    @ApiOperation(value = "添加需求")
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> addDemands(@Valid @RequestBody DemandsEntity demandsEntity, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bool = this.service.Insert(demandsEntity);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiModelProperty(value = "修改 是否联系")
    @PostMapping("/EditContact")
    public ResponseEntity<Result<Boolean>> UpdateContact(@Valid @RequestBody DemandsForm demandsForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (demandsForm.isContact != 1 || demandsForm.isContact != 2) {
            throw new ApiException("服务器繁忙,状态码出错", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bool = this.service.UpdateContact(demandsForm.demandId, demandsForm.isContact);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiModelProperty(value = "修改 基本信息")
    @PostMapping("/EditBasic")
    public ResponseEntity<Result<Boolean>> UpdateBasic(@Valid @RequestBody DemandsForm demandsForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (demandsForm.demandName.isEmpty() || demandsForm.demandContent.isEmpty()) {
            throw new ApiException("参数错误,不能为空或空字符串", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bool = this.service.UpdateBasic(demandsForm.demandId, demandsForm.demandName, demandsForm.demandContent);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiModelProperty(value = "用户根据类型查询")
    @GetMapping("/GetTypeById")
    public ResponseEntity<Result<List<DemandsEntity>>> GetTypeById(
            @ApiParam(value = "demandId", required = true)
            @PathVariable
                    Long demandId,
            @ApiParam(value = "type", required = true)
            @PathVariable
                    Integer type,
            @ApiParam(value = "isContact", required = true)
            @PathVariable
                    Integer isContact
    ) {
        List<DemandsEntity> demandsEntityList = this.service.Get(demandId, type, isContact);
        Result<List<DemandsEntity>> result = new Result<>(demandsEntityList, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiModelProperty(value = "管理员根据类型查询所有")
    @GetMapping("/GetAdminType")
    public ResponseEntity<Result<List<DemandsEntity>>> GetAdminByType(
            @ApiParam(value = "type", required = true)
            @PathVariable
                    Integer type
    ) {
        List<DemandsEntity> demandsEntityList = this.service.GetTypeAll(type);
        Result<List<DemandsEntity>> result = new Result<>(demandsEntityList, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiModelProperty(value = "根据唯一标识查询")
    @GetMapping("/GetById")
    public  ResponseEntity<Result<DemandsEntity>> GetById(
            @ApiParam(value = "demandId", required = true)
            @PathVariable
            Long demandId
    ){
        DemandsEntity demandsEntity=this.service.GetByOne(demandId);
        Result<DemandsEntity> result=new Result<>(demandsEntity,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }


}
