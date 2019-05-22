package com.goodsogood.ows.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.goodsogood.ows.configuration.ClientExceptionHandler;
import com.goodsogood.ows.exception.ApiException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsUtils {

    private SmsUtils() {
    }

    public static String postEncrypt(String Url, String username, String pwd, String Phone, String content, String Productid) throws UnsupportedEncodingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String tkey = sdf.format(new Date());
        String password = MD5Utils.MD5(MD5Utils.MD5(pwd) + tkey);
        content = URLEncoder.encode(content, "UTF-8");
        String param = "username=" + username + "&password=" + password + "&tkey=" + tkey + "&mobile=" + Phone + "&content=" + content + "&productid=" + Productid + "&xh=";
        return HttpRequestUtils.sendPost(Url, param);
    }


}
