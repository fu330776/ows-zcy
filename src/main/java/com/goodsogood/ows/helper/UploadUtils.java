package com.goodsogood.ows.helper;


import com.goodsogood.ows.model.vo.UpLoadVo;
import net.coobird.thumbnailator.Thumbnails;
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
        if (size > 1024 *1024 * 5) {
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

    public UpLoadVo importPicture(MultipartFile file, HttpServletRequest req, String urls) throws IOException {
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
        if (size > 1024 * 1024* 5) {
            vo.setCode(10002);
            vo.setMsg("文件大小不能大于4M");
        }
        //获取文件后缀
        String hz = oldName.substring(oldName.lastIndexOf("."));
        if (hz.equals(".jpg") || hz.equals(".png")) {
            String newName = UUID.randomUUID().toString() + hz;
            file.transferTo(new File(folder, newName));
            double scale = 1.0d ;
            String oldPath=new File(folder, newName).getPath();

            String path=new File(folder, "y"+newName).getPath();
            if(size >= 1 * 1024) {
                if (size > 0) {
                    scale = (1 * 1024f) / size;
                }
                if (size < 1 * 1024) {
                    Thumbnails.of(oldPath).scale(1f).toFile(path);
                } else {
                    Thumbnails.of(oldPath).scale(1f).outputQuality(scale).toFile(path);
                }
            }
            new File(folder, newName).delete();
            //协议，域名，端口号，地址，文件
            String url =req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()+"/"+ realPath + "/y" + newName;
            vo.setSuccess(true);
            vo.setMsg("上传成功");
            vo.setCode(200);
            vo.setData(url);
            return vo;
        } else {
            return vo;
        }

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String format = sdf.format(new Date());
//        UpLoadVo vo = new UpLoadVo();
//        vo.setSuccess(false);
//        vo.setCode(10001);
//        vo.setMsg("文件上传失败");
//        String realPath = urls + "/" + format;
//        String Path = req.getSession().getServletContext().getRealPath("/");
//
//        File folder = new File(Path + realPath);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        //获取文件原名称
//        String oldName = file.getOriginalFilename();
//        //获取文件大小
//        Long size = file.getSize();
//        String newName = UUID.randomUUID().toString();
//        String hz = oldName.substring(oldName.lastIndexOf("."));
//
//        if (size > 1024  * 1) {
//            vo.setCode(10002);
//            vo.setMsg("文件大小不能大于4M");
//        }
//
//        //获取文件后缀
//        if (hz.equals(".jpg") || hz.equals(".png") ) {
////            if(hz.contains(".png"))
////            {
////                hz = hz.replace(".png", ".jpg");
////            }
//            String nesname = newName+hz;
//            file.transferTo(new File(folder, nesname));
//            //协议，域名，端口号，地址，文件
//            String url =req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()+"/"+ realPath + "/" + newName+hz;
//
//            String newUrl=req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()+"/"+ realPath + "/" + newName+"s"+hz;
//            vo.setData(url);
//            double scale = 1.0d ;
//            if(size >= 1 * 1024) {
//                if (size > 0) {
//                    scale = (1  * 1024f) / size;
//                }
//                if (size < 1  * 1024) {
//                    Thumbnails.of(url).scale(1f).toFile(newUrl);
//                } else {
//                    Thumbnails.of(url).scale(1f).outputQuality(scale).toFile(newUrl);
//                }
//                vo.setData(newUrl);
//            }
//            vo.setSuccess(true);
//            vo.setMsg("上传成功");
//            vo.setCode(200);
//            return vo;
//        } else {
//            return vo;
//        }
    }

}
