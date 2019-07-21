package com.goodsogood.ows.helper;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
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


    public  static  String  SendSms(String Pthone,String SignName,String TemplateCode,String code){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI0lVN9GJGPztt", "Yt9luiJ4qc1vZHdz2L8qyk9H6KTwfF");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", Pthone);
        request.putQueryParameter("SignName", "知创云");
        request.putQueryParameter("TemplateCode", "SMS_171115994");
        request.putQueryParameter("AccessKeyId", "LTAI0lVN9GJGPztt");
        request.putQueryParameter("TemplateParam", "{code:"+code+"}");
        String result=null;
        try {
            CommonResponse response = client.getCommonResponse(request);
            result= response.getData();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return result;
    }


}
