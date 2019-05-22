package com.goodsogood.ows.controller;

import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.DemandsEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.DemandAddForm;
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
    public ResponseEntity<Result<Boolean>> addDemands(@Valid @RequestBody DemandAddForm addForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        DemandsEntity entity = new DemandsEntity();
        entity.setAddtime(new Date());
        entity.setDemandContent(addForm.getDemandContent());
        entity.setDemandName(addForm.getDemandName());
        entity.setDemandType(addForm.getDemandType());
        entity.setIsContact(addForm.getIsContact());
        entity.setUserId(addForm.getUserId());
        Boolean bool = this.service.Insert(entity);
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
    @GetMapping("/GetTypeById/{id}")
    public ResponseEntity<Result<PageInfo<DemandsEntity>>> GetTypeById(
            @ApiParam(value = "id", required = true)
            @PathVariable
                    Long id,
            Integer type,
            Integer isContact, Integer page, Integer pageSize
    ) {
        if (page == null) {
            page = 0;
        }
        PageInfo<DemandsEntity> demandsEntityList = this.service.Get(id, type, isContact, new PageNumber(page, pageSize));
        Result<PageInfo<DemandsEntity>> result = new Result<>(demandsEntityList, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiModelProperty(value = "管理员根据类型查询所有")
    @GetMapping("/GetAdminType/{type}")
    public ResponseEntity<Result<PageInfo<DemandsEntity>>> GetAdminByType(
            @ApiParam(value = "type", required = true)
            @PathVariable
                    Integer type, Integer IsCount, Integer page, Integer pageSize

    ) {
        if (page == null) {
            page = 0;
        }
        PageInfo<DemandsEntity> demandsEntityList = this.service.GetTypeAll(type, IsCount, new PageNumber(page, pageSize));
        Result<PageInfo<DemandsEntity>> result = new Result<>(demandsEntityList, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiModelProperty(value = "根据唯一标识查询")
    @GetMapping("/GetById/{id}")
    public ResponseEntity<Result<DemandsEntity>> GetById(
            @ApiParam(value = "id", required = true)
            @PathVariable
                    Long id
    ) {
        DemandsEntity demandsEntity = this.service.GetByOne(id);
        Result<DemandsEntity> result = new Result<>(demandsEntity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
