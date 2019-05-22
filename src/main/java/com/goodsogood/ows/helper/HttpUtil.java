package com.goodsogood.ows.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodsogood.ows.configuration.ClientExceptionHandler;
import com.goodsogood.ows.exception.ApiException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class HttpUtil {
    private HttpUtil() {
    }


    public static String postEncrypt(RestTemplate restTemplate, String url, Object body, HttpHeaders httpHeaders, TypeReference typeReference) throws Exception {
        httpHeaders.add("Content-Type", "text/plain");
        //生成hash
        String hash = String.valueOf(Math.abs(HashAlgorithmsUtils.SDBMHash(RandomUtils.getRandomStr(16))));
        httpHeaders.add("_hash", hash);
        //生成key
        log.debug(hash);
        String key = DecipherUtils.appedZero(String.valueOf(hash), 8);
        log.debug(key);
        //生成加密报文
        String encrypt = DESUtilGSG.encryption(new ObjectMapper().writeValueAsString(body), key);
        log.debug(encrypt);
        HttpEntity<String> entity = new HttpEntity<>(encrypt, httpHeaders);
        return send(restTemplate, HttpMethod.POST, url, entity, typeReference);
    }

    private static String send(RestTemplate restTemplate, HttpMethod method, String url, HttpEntity<?> entity, TypeReference typeReference) throws IOException, ApiException {
        restTemplate.setErrorHandler(new ClientExceptionHandler());
        ResponseEntity<String> resultResponseEntity = restTemplate.exchange(url, method, entity, String.class);
        return resultResponseEntity.getBody();

    }
}
