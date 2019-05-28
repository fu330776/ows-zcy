package com.goodsogood.ows.helper;


import com.goodsogood.ows.model.vo.UpLoadVo;
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


    public UpLoadVo importData(MultipartFile file, HttpServletRequest req, String urls) throws IOException {
        UpLoadVo vo = new UpLoadVo();
        vo.setSuccess(false);
        vo.setCode(10001);
        vo.setMsg("文件上传失败");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
//    String realPath = req.getServletContext().getRealPath("/upload") + format;
        String realPath = urls + format;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        Long size = file.getSize();
        if (size > 1024 * 1024 * 4) {
            vo.setCode(10002);
            vo.setMsg("文件大小不能大于4M");
        }
        String hz = oldName.substring(oldName.lastIndexOf("."));
        if (hz.equals(".pdf")) {
            String newName = UUID.randomUUID().toString() + hz;
            file.transferTo(new File(folder, newName));
//            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()  + format + "/" + newName;
            String url = realPath + "/" + newName;
            vo.setSuccess(true);
            vo.setMsg("上传成功");
            vo.setCode(200);
            vo.setData(url);
            return vo;
        } else {
            return vo;
        }
    }

}
