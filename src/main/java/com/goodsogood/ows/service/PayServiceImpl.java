package com.goodsogood.ows.service;

import com.goodsogood.ows.configuration.WxPayConfig;
import com.goodsogood.ows.helper.*;
import com.goodsogood.ows.model.db.ApiResult;
import com.goodsogood.ows.model.db.ResponseCode;
import com.goodsogood.ows.model.vo.PayBean;
import com.goodsogood.ows.model.vo.PayTypeConst;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service
@Log4j2
public class PayServiceImpl {
    @Autowired
    private WxPayConfig wxPayConfig;

    /**
     * 创建支付订单
     *
     * @param payBean json 格式参数
     * @return
     */

    public ApiResult createPayOrder(PayBean payBean) throws Exception {
        // 微信支付金额换算
        int amountWxPay = CalculateUtil.multiply(Double.valueOf(payBean.getAmount()), 100, 2).intValue();
        // 返回结果
        Map<String, String> resultMap = new HashMap<>(16);
        // 创建支付订单
        switch (payBean.getPayType())
        {
            case PayTypeConst.ORDER_PAY_TYPE_WX_NATIVE:
                // 微信 NATIVE 支付(二维码)
                Map<String,String> wxPayNativeMap = WxPayManager.createNativeOrder(wxPayConfig, payBean.getOrderNo(),
                        amountWxPay, payBean.getIp());
                if (wxPayNativeMap != null &&
                        Objects.equals(wxPayNativeMap.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    resultMap.put("prePayOrderInfo",wxPayNativeMap.get("code_url"));
                    return ApiResult.success(resultMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_JSAPI:
                // 微信 JsAPI 支付(公众号)
                if (StringUtils.isEmpty(payBean.getOpenId())) {
                    return ApiResult.failure(ResponseCode.PAY_SUBMIT_ERROR);
                }
                Map<String, String> wxPayJsAPIMap = WxPayManager.createJsAPIOrder(wxPayConfig, payBean.getOrderNo(),
                        amountWxPay, payBean.getIp(), payBean.getOpenId());
                log.debug(wxPayJsAPIMap);
                if (wxPayJsAPIMap != null &&
                        Objects.equals(wxPayJsAPIMap.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    return ApiResult.success(wxPayJsAPIMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_H5:
                // 微信 H5 支付
                Map<String, String> wxPayH5Map = WxPayManager.createH5Order(wxPayConfig, payBean.getOrderNo(),
                        amountWxPay, payBean.getIp());
                if (wxPayH5Map != null &&
                        Objects.equals(wxPayH5Map.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    resultMap.put("prePayOrderInfo",wxPayH5Map.get("mweb_url"));
                    return ApiResult.success(resultMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_APP:
                // 微信 APP 支付
                Map<String, String> wxPayAppMap = WxPayManager.createAppOrder(wxPayConfig, payBean.getOrderNo(),
                        amountWxPay, payBean.getIp());
                if (wxPayAppMap != null &&
                        Objects.equals(wxPayAppMap.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    return ApiResult.success(wxPayAppMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_MINI:
                // 微信 小程序 支付
                if (StringUtils.isEmpty(payBean.getOpenId())) {
                    return ApiResult.failure(ResponseCode.PAY_SUBMIT_ERROR);
                }
                Map<String, String> wxPayMiniMap = WxPayManager.createJsAPIOrder(wxPayConfig, payBean.getOrderNo(),
                        amountWxPay, payBean.getIp(), payBean.getOpenId());
                if (wxPayMiniMap != null &&
                        Objects.equals(wxPayMiniMap.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    return ApiResult.success(wxPayMiniMap);
                }
                break;
            default:
                return ApiResult.failure(ResponseCode.PAY_TYPE_ERROR);
        }
        return ApiResult.failure(ResponseCode.PAY_SUBMIT_ERROR);
    }

    /**
     * (主动)获取支付结果
     *
     * @param payBean 订单信息(json 格式参数)
     * @return
     */

    public ApiResult getPayResult(PayBean payBean) throws Exception {
        // 返回结果
        Map<String, String> resultMap;
        switch (payBean.getPayType()) {
            case PayTypeConst.ORDER_PAY_TYPE_WX:
                resultMap = WxPayManager.getPayResult(wxPayConfig, payBean.getOrderNo());
                break;
            default:
                return ApiResult.failure(ResponseCode.PAY_TYPE_ERROR);
        }
        if (MapUtil.isEmpty(resultMap)) {
            return ApiResult.failure(ResponseCode.PAY_STATUS_ERROR);
        }

        return ApiResult.success(resultMap);
    }

    /**
     * 微信支付结果通知
     *
     * @param request 微信支付回调请求
     * @return 支付结果
     */

    public String wxPayNotify(HttpServletRequest request) {
        String result = null;
        try {
            InputStream inputStream = request.getInputStream();
            /**
             * 读取通知参数
             */
            String strXML = FileUtils.getStringFromStream(inputStream);
            Map<String,String> reqMap = MapUtil.xml2Map(strXML);
            log.debug("==================================================支付回调===================================================");
            log.debug(strXML);
            log.debug(reqMap);
            if(MapUtil.isEmpty(reqMap)){
                log.warn("request param is null");
                return wxPayConfig.getResponseFail();
            }
            /**
             * 校验签名
             */
            if(!SignUtil.signValidate(reqMap, wxPayConfig.getKey(), wxPayConfig.getFieldSign())){
                return wxPayConfig.getResponseFail();
            }

            Map<String, String> resultMap = new HashMap<>(16);
            resultMap.put("return_code",wxPayConfig.getResponseSuccess());
            resultMap.put("return_msg","OK");
            result = MapUtil.map2Xml(resultMap);
        } catch (IOException e) {
            log.error("get request inputStream error",e);
            return wxPayConfig.getResponseFail();
        } catch (Exception e) {
            log.error("resolve request param error",e);
            return wxPayConfig.getResponseFail();
        }
        return result;
    }



}
