package com.goodsogood.ows.helper;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UploadUtils {


    public static String importData(MultipartFile file, HttpServletRequest req) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        String realPath = req.getServletContext().getRealPath("/upload") + format;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String hz = oldName.substring(oldName.lastIndexOf("."));
        if (hz == ".pdf") {
            String newName = UUID.randomUUID().toString() + hz;
            file.transferTo(new File(folder, newName));
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/upload" + format + newName;
            return url;
        } else {
            return "";
        }
    }

}
