package com.goodsogood.ows.controller;


import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.PatentsEntity;
import com.goodsogood.ows.model.vo.IdeaForm;
import com.goodsogood.ows.model.vo.PatentApplicationForm;
import com.goodsogood.ows.model.vo.PatentsVo;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.service.PatentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
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

    public PatentsController(PatentsService patentsService, Errors errors) {
        this.service = patentsService;
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
        Boolean bool = false;
        bool = this.service.Insert(entity);

        if (bool == false) {
            throw new ApiException("服务器繁忙，申请专利失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
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
        entity.setPatentContent(patent.content);
        entity.setPatentTitle(patent.title);
        entity.setUserId(patent.userid);
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
    @PostMapping(value = "/ideadd")
    public ResponseEntity<Result<Boolean>> ideaAdd(@Valid @RequestBody IdeaForm form, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        PatentsEntity entity = new PatentsEntity();
        entity.setAddtime(new Date());
        entity.setIsNeedPay(1);
        entity.setIsPay(form.getIsPay());
        entity.setPatentContent(form.getContent());
        entity.setPatentMoney(form.getMoney());
        entity.setPatentTitle(form.getTitle());
        entity.setPatentType(2);
        entity.setUserId(form.getUserId());
        Boolean bool = this.service.Insert(entity);
        Result<Boolean> result = new Result<>(true, errors);
        if (bool == false) {
            result = new Result<>(false, errors);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 用户根据类型查询
     *
     * @param type
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "用户根据类型查询")
    @GetMapping("/get/{type}")
    public ResponseEntity<Result<PageInfo<PatentsVo>>> get(@ApiParam(value = "type", required = true)
                                                               @PathVariable Integer type, Long userId, Integer page, Integer pageSize) {
        if (page == null||pageSize==null) {
            page = 1;
            pageSize=10;
        }
        PageInfo<PatentsVo> patentsEntityPageInfo = this.service.Get(userId, type, new PageNumber(page, pageSize));
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
                                                                    @PathVariable Integer type, Integer page, Integer pageSize) {
        if (page == null||pageSize==null) {
            page = 1;
            pageSize=10;
        }
        PageInfo<PatentsVo> pageInfo = this.service.GetAll(type, new PageNumber(page, pageSize));
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
}
