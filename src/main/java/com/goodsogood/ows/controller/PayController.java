package com.goodsogood.ows.controller;

import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.configuration.WxPayConfig;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.HttpRequestUtils;
import com.goodsogood.ows.helper.IpUtil;
import com.goodsogood.ows.model.db.ApiResult;
import com.goodsogood.ows.model.db.PaymentEntity;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.PayService;
import com.goodsogood.ows.service.PayServiceImpl;
import com.goodsogood.ows.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v-Pay")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "支付", tags = {"PayController"})
public class PayController {
    private final PayService service;
    private final Errors errors;
    private final PayServiceImpl payService;
    private final UsersService usersService;
    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    public PayController(PayServiceImpl payService, PayService service, UsersService usersService, Errors errors) {
        this.payService = payService;
        this.service = service;
        this.usersService = usersService;
        this.errors = errors;
    }

    /**
     *  获取用户openid
     * @param id
     * @return openid
     */
    public String getOpenId(@Valid @RequestBody Long id) {
        return this.usersService.getOpenId(id);
    }


    /**
     * 创建订单
     *
     * @param form
     * @param bindingResult
     * @return
     */

    public ResponseEntity<Result<PaymentEntity>> CreateOrder(@Valid @RequestBody CreateOrderForm form, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        PaymentEntity entity = new PaymentEntity();
        ApiResult apiResult = null;
        Result<PaymentEntity> result = null;
        try {

            switch (form.getType()) {
                case 1:
                    entity = this.service.InsertPatents(form.getGid());
                    break;
                case 2:
                    entity = this.service.InsertTasks(form.getGid());
                    break;
            }
            //创建订单
            if (entity.getPaymentId() > 0) {
                PayBean bean = new PayBean();
                bean.setAmount(String.valueOf(entity.getRealMoney()));
                bean.setOrderNo(entity.getOrderNo());
                bean.setPayType(22);
                bean.setOpenId(form.getOpenId());
                bean.setIp(IpUtil.getIpAddr(request));
                apiResult = payService.createPayOrder(bean);
            }
            if (apiResult != null) {
                result = new Result<>(entity, errors);
            } else {
                this.service.Del(entity.getPaymentId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 支付成功
     *
     * @param order
     * @param bindingResult
     * @return
     */
    public ResponseEntity<Result<Boolean>> PayOrder(@Valid @RequestBody PayOrderForm order, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        this.service.Update(order.getOrderNo(), "");

        Result<Boolean> result = new Result<>(false, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 创建支付订单
     *
     * @param payBean 订单信息( json 格式数据)
     * @return
     */
    @ApiModelProperty(value = "创建支付订单")
    @PostMapping(value = "/createOrder")
    public ApiResult createPayOrder(@Valid @RequestBody PayBean payBean) {

        ApiResult apiResult = null;

        try {
            apiResult = payService.createPayOrder(payBean);

        } catch (Exception e) {
            log.error("支付订单创建失败", e);
        }

        return apiResult;
    }

    /**
     * 查询订单支付结果
     *
     * @param payBean 订单信息( json 格式数据)
     * @return
     */
    @ApiModelProperty(value = "查询订单支付结果")
    @PostMapping(value = "/getPayResult")
    public ApiResult getPayResult(@RequestBody PayBean payBean) {

        ApiResult apiResult = null;

        try {
            apiResult = payService.getPayResult(payBean);
        } catch (Exception e) {
            log.error("订单支付结果查询失败", e);
        }

        return apiResult;
    }

    /**
     * 微信支付结果异步通知
     *
     * @param request 微信支付回调请求
     * @return
     */

    @ApiModelProperty(value = "微信支付结果异步通知")
    @PostMapping(value = "/wxPayNotify")
    public String WXPayNotify(HttpServletRequest request) {

        log.debug("WxPay notify");
        String result = null;
        try {
            result = payService.wxPayNotify(request);
        } catch (Exception e) {
            log.error("微信支付结果通知解析失败", e);
            return "FAIL";
        }

        log.debug(result);
        return result;
    }

    @ApiModelProperty(value = "获取openId")
    @GetMapping(value = "/getOpenId/{code}")
    public String GetOpen(@ApiParam(value = "page", required = true)
                              @PathVariable String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        String AppId = wxPayConfig.getPublicAppId();
        String Key = "7e46056922b5b4540e035ef04a058eda";
        log.debug(AppId);
        log.debug(Key);
        String paras = "appid=" + AppId + "&secret=7e46056922b5b4540e035ef04a058eda&code=" + code + "&grant_type=authorization_code";
        String result = HttpRequestUtils.sendPost(url, paras);
        return result;

    }

}
