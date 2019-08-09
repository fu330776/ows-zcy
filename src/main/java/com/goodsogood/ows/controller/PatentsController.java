package com.goodsogood.ows.controller;


import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.PatentsEntity;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.DataService;
import com.goodsogood.ows.service.PatentsService;
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
import java.util.Date;

@RestController
@RequestMapping("/v-patents")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "我的知识产权", tags = {"Patents manager"})
public class PatentsController {
    private final PatentsService service;
    private final Errors errors;
    private final DataService dataService;
    @Autowired
    public PatentsController(PatentsService patentsService, DataService dataService, Errors errors) {
        this.service = patentsService;
        this.dataService = dataService;
        this.errors = errors;
    }

    /**
     * 专利申请
     */
    @ApiModelProperty(value = "申请专利")
    @PostMapping("/patentAdd")
    public ResponseEntity<Result<Boolean>> PatentApplication(@Valid @RequestBody PatentApplicationForm patent, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        PatentsEntity entity = new PatentsEntity();
        entity.setPatentType(1);
        entity.setPatentMoney(0);
        entity.setAddtime(new Date());
        entity.setIsNeedPay(2);
        entity.setPatentTitle(patent.getTitle());
        entity.setIsPay(0);
        entity.setPatentContent(patent.getContent());
        entity.setUserId(patent.getUserid());
        entity.setPicture(patent.getPicture());
        entity.setState("递交成功");
        Boolean bool;
        bool = this.service.Insert(entity);

        if (bool == false) {
            throw new ApiException("服务器繁忙，申请专利失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        } else {
            Statistics(5);
        }

        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 我的专利
     */
    @ApiModelProperty(value = "我的专利")
    @PostMapping("/myPatentAdd")
    public ResponseEntity<Result<Boolean>> MyPatent(@Valid @RequestBody PatentApplicationForm patent, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        PatentsEntity entity = new PatentsEntity();
        entity.setPatentType(3);
        entity.setPatentMoney(0);
        entity.setIsNeedPay(2);
        entity.setAddtime(new Date());
        entity.setIsPay(0);
        entity.setPatentContent(patent.getContent());
        entity.setPatentTitle(patent.getTitle());
        entity.setUserId(patent.getUserid());
        entity.setState("递交成功");
        entity.setPicture(patent.getPicture());
        Boolean bool = this.service.Insert(entity);
        if (!bool) {
            throw new ApiException("服务器繁忙，申请专利失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 添加确权认证
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "idea确权添加")
    @PostMapping(value = "/ideAdd")
    public ResponseEntity<Result<PatentsEntity>> ideaAdd(@Valid @RequestBody IdeaForm form, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        PatentsEntity entity = new PatentsEntity();
        entity.setAddtime(new Date());
        entity.setIsNeedPay(1);
        entity.setIsPay(1);
        entity.setPatentContent(form.getContent());
        entity.setPatentMoney(form.getMoney());
        entity.setPatentTitle(form.getTitle());
        entity.setPatentType(2);
        entity.setUserId(form.getUserId());
        entity.setPicture(form.getPicture());
        entity.setState("递交成功");
        entity = this.service.InsertIdea(entity);
        Result<PatentsEntity> result = new Result<>(entity, errors);
       if(entity !=null) {
            Statistics(4);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * idea确权统计 与  专利申请
     */
    private void Statistics(Integer type) {
        this.dataService.Update(type);
    }


    /**
     * 修改状态
     * @param dsf
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "修改状态")
    @PostMapping(value = "/state")
    public  ResponseEntity<Result<Boolean>> UpdateState(@Valid @RequestBody DemandStateForm  dsf,BindingResult bindingResult)
    {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(dsf.getState().isEmpty()){
           throw new ApiException("状态不可为空", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bl=this.service.UpdateState(dsf.getDemandId(),dsf.getState());
        Result<Boolean> result=new Result<>(bl,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     * idea 查询
     *
     * @param isPay    1:未支付 2：已支付
     * @param page
     * @param title
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "idea 查询")
    @GetMapping(value = "/getIdea/{isPay}")
    public ResponseEntity<Result<PageInfo<PatentsVo>>> GetIdea(@ApiParam(value = "isPay", required = true)
                                                               @PathVariable Integer isPay, Integer page, String title, Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 11;
        }
        PageInfo<PatentsVo> patentsEntityPageInfo = this.service.GetIdeaALL(2, isPay, title, new PageNumber(page, pageSize));
        Result<PageInfo<PatentsVo>> result = new Result<>(patentsEntityPageInfo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 用户根据类型查询
     *
     * @param type
     * @param userId   用户唯一标识
     * @param title    标题
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "用户根据类型查询")
    @GetMapping("/get/{type}")
    public ResponseEntity<Result<PageInfo<PatentsVo>>> get(@ApiParam(value = "type", required = true)
                                                           @PathVariable Integer type, Long userId, String title, Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 12;
        }
        PageInfo<PatentsVo> patentsEntityPageInfo = this.service.Get(userId, type, title, new PageNumber(page, pageSize));
        Result<PageInfo<PatentsVo>> result = new Result<>(patentsEntityPageInfo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 根据类型查询
     *
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据类型查询")
    @GetMapping("/getAll/{type}")
    public ResponseEntity<Result<PageInfo<PatentsVo>>> getByAll(@ApiParam(value = "type", required = true)
                                                                @PathVariable Integer type, String title, Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 10;
        }
        PageInfo<PatentsVo> pageInfo = this.service.GetAll(type, title, new PageNumber(page, pageSize));
        Result<PageInfo<PatentsVo>> result = new Result<>(pageInfo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 根据唯一标识查询
     *
     * @param pid
     * @return
     */
    @ApiOperation(value = "唯一标识查询")
    @GetMapping("/getFind/{pid}")
    public ResponseEntity<Result<PatentsVo>> getFind(@ApiParam(value = "pid", required = true)
                                                     @PathVariable Long pid) {
        PatentsVo entity = this.service.GetFind(pid);
        Result<PatentsVo> result = new Result<>(entity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     *  根据唯一标识删除，未审核
     * @param patentId
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "删除")
    @PostMapping(value = "/del")
    public  ResponseEntity<Result<LoginResult>> Del(@Valid @RequestBody Long patentId, BindingResult bindingResult)
    {
        if (bindingResult.hasFieldErrors() || patentId==null || patentId<=0) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult=this.service.Del(patentId);
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }
}
