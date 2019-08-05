package com.goodsogood.ows.controller;

import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.WithdrawsEntity;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.WithdrawsService;
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

@RestController
@RequestMapping("/v-cash")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "Withdraws", tags = {"资金流水记录"})
public class WithdrawsController {

    private final Errors errors;
    private final WithdrawsService service;

    @Autowired
    public WithdrawsController(Errors errors, WithdrawsService withdrawsService) {
        this.errors = errors;
        this.service = withdrawsService;
    }

    /**
     * 管理员查询资金流水(提现记录)
     *
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "管理员查询资金流水(提现记录)")
    @GetMapping("/getPageAdmin")
    public ResponseEntity<Result<PageInfo<WithdrawsSonVo>>> GetPageAdmin(Integer page, Integer pageSize,Integer status,String keyValue) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 10;
        }
        PageInfo<WithdrawsSonVo> entity = this.service.GetPageAdmin(status,keyValue,new PageNumber(page, pageSize));

        Result<PageInfo<WithdrawsSonVo>> result = new Result<>(entity, errors);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    @ApiOperation(value = "提现请求")
    @PostMapping(value = "/ForCash")
    public   ResponseEntity<Result<LoginResult>> ApplicationForCash(@Valid @RequestBody ApplicationForCashForm form,BindingResult bindingResult)
    {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(form.getUserId()<0 || form.getMoney() <0)
        {
            throw new ApiException("必填参数不可为空", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }

        WithdrawsEntity entity =new WithdrawsEntity();
        entity.setUserId(form.getUserId());
        entity.setWithdrawMoney(form.getMoney());
        LoginResult result1=this.service.Insert(entity);
        Result<LoginResult> result=new Result<>(result1,errors);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    /***
     * 用户查询个人资金流水记录
     * @param userId
     * @param status 1：未受理，2：已受理  4：提现完成',
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "用户查询个人资金流水记录")
    @GetMapping("/getPageUser/{userId}")
    public   ResponseEntity<Result<PageInfo<WithdrawsSonVo>>> GetPageUser(@ApiParam(value = "userId", required = true)
                                                 @PathVariable  Long userId ,Integer status,Integer page,Integer pageSize)
    {
        if(page == null  )
        {
            page=1;
            pageSize=100;
        }

        PageInfo<WithdrawsSonVo>  son =  this.service.GetPageUser(userId,status,new PageNumber(page,pageSize));
        Result<PageInfo<WithdrawsSonVo>> result =new Result<>(son,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }
    /***
     *  个人平台资金合计
     */
    @ApiOperation(value = "用户查询个人资金流水记录")
    @GetMapping("/getSumUser/{userId}")
    public  ResponseEntity<Result<UserMoneyVo>> GetSumUser( @ApiParam(value = "userId", required = true)
                                                            @PathVariable Long userId)
    {
        UserMoneyVo vo= this.service.GetSumUser(userId);
        Result<UserMoneyVo>  result =new Result<>(vo,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    @ApiOperation(value = "修改提现状态")
    @PostMapping(value = "/UpdateStatus")
     public ResponseEntity<Result<LoginResult>> UpdateStatus(@Valid @RequestBody ithdrawsStatusForm f,BindingResult bindingResult)
    {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult=this.service.UpdateStatus(f.getUserId(),f.getWithdraw_id(),f.getStatus());
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     *  修改提现金额
     */
    @ApiOperation(value = "修改提现金额")
    @PostMapping(value = "/UpdateMoney")
    public ResponseEntity<Result<LoginResult>>  UpdateMoney(@Valid @RequestBody ithdrawsStatusForm f,BindingResult bindingResult )
    {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult=this.service.UpdateMoney(f.getUserId(),f.getWithdraw_id(),f.getMoney());
        Result<LoginResult> result = new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);

    }




//    /**
//     * 根据用户唯一标识修改
//     *
//     * @param userId        用户唯一标识
//     * @param bindingResult
//     * @return
//     */
//    @ApiOperation(value = "根据用户唯一标识修改")
//    @PostMapping(value = "/setCash")
//    public ResponseEntity<Result<Boolean>> SetUpdates(@Valid @RequestBody Long userId, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Result<Boolean> result = null;
//        if (userId == null || userId <= 0) {
//            result = new Result<>(false, errors);
//        } else {
//            Boolean b = this.service.SetCash(userId);
//            result = new Result<>(b, errors);
//        }
//        return new ResponseEntity<>(result, HttpStatus.OK);
//
//    }
//
//    @ApiOperation(value = "查询所有未提现的流水")
//    @GetMapping("/get/{userId}")
//    public ResponseEntity<Result<PageInfo<WithdrawsVo>>> Get(
//            @ApiParam(value = "userId", required = true)
//            @PathVariable Long userId, Integer is,
//            Integer page, Integer pageSize) {
//        if (page == null || pageSize == null) {
//            page = 1;
//            pageSize = 12;
//        }
//        PageInfo<WithdrawsVo> entity = this.service.Get(userId, is, new PageNumber(page, pageSize));
//        Result<PageInfo<WithdrawsVo>> result = new Result<>(entity, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//
//    }

}
