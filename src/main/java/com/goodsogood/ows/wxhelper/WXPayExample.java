//package com.goodsogood.ows.wxhelper;
//
//import com.github.wxpay.sdk.WXPay;
//import com.github.wxpay.sdk.WXPayConstants;
//import com.github.wxpay.sdk.WXPayUtil;
//import com.goodsogood.ows.helper.HttpRequestUtils;
//import com.unboundid.util.json.JSONObject;
//
//import javax.servlet.http.HttpServletResponse;
//import java.text.MessageFormat;
//import java.util.HashMap;
//import java.util.Map;
//
//public class WXPayExample {
//    /***
//     *  统一下单 DEMO
//     * @param args
//     * @throws Exception
//     */
//    public static void main(String[] args) throws Exception {
//
//        MyConfig config = new MyConfig();
//        WXPay wxpay = new WXPay(config);
//
//        Map<String, String> data = new HashMap<String, String>();
//        data.put("body", "腾讯充值中心-QQ会员充值"); //商品描述
//        data.put("out_trade_no", "2016090910595900000012"); //商户订单号
//        data.put("device_info", "WEB"); //设备号
//        data.put("fee_type", "CNY"); //标价币种
//        data.put("total_fee", "1"); //标价金额
//        data.put("spbill_create_ip", "123.12.12.123"); //终端IP
//        data.put("notify_url", "http://www.example.com/wxpay/notify"); //回调地址
//        data.put("trade_type", "JSAPI");  // 交易类型
//
//        try {
//            Map<String, String> resp = wxpay.unifiedOrder(data);
//
//            System.out.println(resp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
////    private final String AppID = "";
////    private final String AppSecret = "";
//
////    /**
////     * 第一步：用户同意授权，获取code
////     *
////     * @return
////     */
////    public void getCode() {
////        String Url = "https://open.weixin.qq.com/connect/oauth2/authorize";
////        String redirect_uri = ""; //回调地址
////        String scope = "snsapi_userinfo"; //应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
////        String Param = MessageFormat.format("appid={0}&redirect_uri={1}&response_type=code&scope={2}&state=123#wechat_redirect", AppID, redirect_uri, scope);
////        HttpRequestUtils.sendGet(Url, Param);
////    }
////
////
////    /**
////     * 第二步：通过code换取网页授权access_token
////     */
////    private Map<String, String> getAccess_tokenByCode(String code) {
////        Map<String, String> data = new HashMap<String, String>();
////        String requestUrlMessageFormat = "https://api.weixin.qq.com/sns/oauth2/access_token";
////        String param = "appid={0}&secret={1}&code={2}&grant_type=authorization_code";
////        String requestUrl = MessageFormat.format(param, AppID, AppSecret, code);
////        String access_token = HttpRequestUtils.sendGet(requestUrlMessageFormat, requestUrl);
////        return data;
////    }
////
////    /**
////     * 第三步：刷新access_token（如果需要）
////     *
////     * @param refresh_token
////     * @return
////     */
////    public String GetAccessCode(String refresh_token) {
////        String Url = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
////        String Param = MessageFormat.format("appid={0}&grant_type=refresh_token&refresh_token={1}", AppID, refresh_token);
////        String msg = HttpRequestUtils.sendGet(Url, Param);
////        return msg;
////    }
////
////    /**
////     * 第四步：拉取用户信息(需scope为 snsapi_userinfo)
////     *
////     * @param access_token
////     * @param OpenId
////     * @return
////     */
////    public String GetUserInfo(String access_token, String OpenId) {
////        String url = " https://api.weixin.qq.com/sns/userinfo";
////        String param = MessageFormat.format("access_token={0}&openid={1}&lang=zh_CN", access_token, OpenId);
////        return HttpRequestUtils.sendGet(url, param);
////    }
////
////    /**
////     * 检验授权凭证（access_token）是否有效
////     *
////     * @param access_token
////     * @param OpenId
////     * @return
////     */
////    public String GetTest(String access_token, String OpenId) {
////        String url = "https://api.weixin.qq.com/sns/auth";
////        String param = MessageFormat.format("access_token={0}&openid={1}", access_token, OpenId);
////        return HttpRequestUtils.sendGet(url, param);
////    }
////
//
//}
