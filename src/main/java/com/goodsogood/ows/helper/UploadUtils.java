package com.goodsogood.ows.helper;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UploadUtils {

    @Value("${file.PDF}")
    private  String pdfUrl;


    public  String importData(MultipartFile file, HttpServletRequest req) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
//    String realPath = req.getServletContext().getRealPath("/upload") + format;
       String realPath="D:\\file\\PDF\\"+format;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String hz = oldName.substring(oldName.lastIndexOf("."));
        if (hz.equals(".pdf") ) {
            String newName = UUID.randomUUID().toString() + hz;
            file.transferTo(new File(folder, newName));
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/upload" + format + newName;
            return url;
        } else {
            return "";
        }
    }

}
