package com.goodsogood.ows.controller;

import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.DemandsEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.DataService;
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
    private final DataService dataService;

    @Autowired
    public DemandsController(DemandsService demandsService, DataService dataService, Errors error) {
        this.service = demandsService;
        this.dataService = dataService;
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
        entity.setPicture(addForm.getPicture());
        entity.setState("递交成功");
        Boolean bool = this.service.Insert(entity);

        /**
         * 统计代码
         */
        if (bool) {
            Statistics(addForm.getDemandType());
        }

        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 统计代码
     * 统计 创新与百宝箱
     *
     * @param DemandType
     */
    private void Statistics(Integer DemandType) {
        Integer type;
        switch (DemandType) {
            case 1: //创新量
                type = 1;
                break;
            case 2:
            case 3:
                type = 2;
                break;
            default:
                type = null;
                break;
        }
        if (type != null) {
            this.dataService.Update(type);
        }
    }


    /**
     * 是否联系
     *
     * @param demandsForm
     * @param bindingResult
     * @return
     */
    @ApiModelProperty(value = "修改 是否联系")
    @PostMapping("/EditContact")
    public ResponseEntity<Result<Boolean>> UpdateContact(@Valid @RequestBody DemandsForm demandsForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
//        if (demandsForm.getIsContact() != 1 || demandsForm.getIsContact() != 2) {
//            throw new ApiException("服务器繁忙,状态码出错", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
        Boolean bool = this.service.UpdateContact(demandsForm.getDemandId(), demandsForm.getIsContact());
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 修改基本信息
     *
     * @param demandsForm
     * @param bindingResult
     * @return
     */
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

    /**
     * 修改需求
     *
     * @param demandsForm
     * @param bindingResult
     * @return
     */
    @ApiModelProperty(value = "修改")
    @PostMapping("/Edit")
    public ResponseEntity<Result<Boolean>> Update(@Valid @RequestBody DemandsForm demandsForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (demandsForm.getDemandName().isEmpty() || demandsForm.getDemandContent().isEmpty() || demandsForm.getIsContact() == null) {
            throw new ApiException("参数错误,不能为空或空字符串", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bool = this.service.Update(demandsForm.getDemandId(), demandsForm.getDemandName(), demandsForm.getDemandContent(), demandsForm.getIsContact(),demandsForm.getPicture());
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    /**
     *  修改状态
     * @param state
     * @param bindingResult
     * @return
     */
    @ApiModelProperty(value = "修改状态")
    @PostMapping("/state")
    public  ResponseEntity<Result<Boolean>> UpdateState(@Valid @RequestBody DemandStateForm state,BindingResult bindingResult)
    {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(state.getState().isEmpty())
        {
            throw new ApiException("参数错误,不能为空或空字符串", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bl=this.service.UpdateState(state.getDemandId(),state.getState());
        Result<Boolean> result=new Result<>(bl,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /***
     *  撤销
     * @param id
     * @param bindingResult
     * @return
     */
    @ApiModelProperty(value = "未受理需求 撤销")
    @PostMapping("/Revoke")
    public  ResponseEntity<Result<LoginResult>> UpdateRevoke(@Valid @RequestBody Long id,BindingResult bindingResult  )
    {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(id == null)
        {
            throw new ApiException("必传参数不可为空", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult=this.service.UpdateRevoke(id);
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);

    }


    /**
     * 根据用户类型查询
     *
     * @param id        用户唯一标识
     * @param type      类型
     * @param isContact 是否联系 1：已联系
     * @param name      标题名称
     * @param page
     * @param pageSize
     * @return
     */
    @ApiModelProperty(value = "用户根据类型查询")
    @GetMapping("/GetTypeById/{id}")
    public ResponseEntity<Result<PageInfo<DemandsVo>>> GetTypeById(
            @ApiParam(value = "id", required = true)
            @PathVariable
                    Long id,
            Integer type,
            Integer isContact, String name, Integer page, Integer pageSize
    ) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 11;
        }
        PageInfo<DemandsVo> demandsEntityList = this.service.Get(id, type, isContact, name, new PageNumber(page, pageSize));
        Result<PageInfo<DemandsVo>> result = new Result<>(demandsEntityList, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiModelProperty(value = "管理员根据类型查询所有")
    @GetMapping("/GetAdminType/{type}")
    public ResponseEntity<Result<PageInfo<DemandsVo>>> GetAdminByType(
            @ApiParam(value = "type", required = true)
            @PathVariable
                    Integer type, Integer IsCount, String name,String state, Integer page, Integer pageSize
    ) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 10;
        }
        PageInfo<DemandsVo> demandsEntityList = this.service.GetTypeAll(type, IsCount, name,state,new PageNumber(page, pageSize));
        Result<PageInfo<DemandsVo>> result = new Result<>(demandsEntityList, errors);
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
