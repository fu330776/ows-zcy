package com.goodsogood.ows.controller;

import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.model.vo.WithdrawSumVo;
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
     * 查询分组信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询分组信息")
    @GetMapping("/getSumUser/{page}")
    public ResponseEntity<Result<PageInfo<WithdrawSumVo>>> GetSumAdmin(@ApiParam(value = "page", required = true)
                                                                       @PathVariable Integer page, Integer pageSize) {
        if (page == null||pageSize==null) {
            page = 1;
            pageSize=10;
        }
        PageInfo<WithdrawSumVo> entity = this.service.GetAdminSum(new PageNumber(page, pageSize));

        Result<PageInfo<WithdrawSumVo>> result = new Result<>(entity, errors);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    /**
     *   根据用户唯一标识修改
     * @param userId 用户唯一标识
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "根据用户唯一标识修改")
    @PostMapping(value = "/setCash")
    public ResponseEntity<Result<Boolean>> SetUpdates(@Valid @RequestBody Long userId, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = null;
        if (userId == null || userId <= 0) {
            result = new Result<>(false, errors);
        } else {
            Boolean b = this.service.SetCash(userId);
            result = new Result<>(b, errors);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


}
