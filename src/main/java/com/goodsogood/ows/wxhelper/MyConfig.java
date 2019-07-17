//package com.goodsogood.ows.wxhelper;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//
//public class MyConfig implements WXPayConfig {
//    private byte[] certData;
//
//    public MyConfig() throws Exception {
//        String certPath = ""; //证书
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
//    }
//
//    //公众账号ID
//    public String getAppID() {
//        return "wx49d161bcc4911a68";
//    }
//
//    //商户号
//    public String getMchID() {
//        return "1542626661";
//    }
//
//    public String getKey() {
//        return "fbba76b3afed8c664990846b487de14e";
//    }
//
//    public InputStream getCertStream() {
//        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
//        return certBis;
//    }
//
//    public int getHttpConnectTimeoutMs() {
//        return 8000;
//    }
//
//    public int getHttpReadTimeoutMs() {
//        return 10000;
//    }
//
//}
