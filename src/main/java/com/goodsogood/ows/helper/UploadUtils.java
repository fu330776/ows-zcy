package com.goodsogood.ows.helper;


import com.goodsogood.ows.model.vo.UpLoadVo;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
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
        String Path = req.getSession().getServletContext().getRealPath("/");
        String realPath = urls + "/" + format;
        File folder = new File(Path + realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //获取文件原名称
        String oldName = file.getOriginalFilename();
        //获取文件大小
        Long size = file.getSize();
        if (size > 1024 * 1024 * 4) {
            vo.setCode(10002);
            vo.setMsg("文件大小不能大于4M");
        }
        //获取文件后缀
        String hz = oldName.substring(oldName.lastIndexOf("."));
        if (hz.equals(".pdf")) {
            String newName = UUID.randomUUID().toString() + hz;
            file.transferTo(new File(folder, newName));
            //协议，域名，端口号，地址，文件
            String url =req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()+"/"+ realPath + "/" + newName;
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
