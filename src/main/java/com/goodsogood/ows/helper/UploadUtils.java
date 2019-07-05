package com.goodsogood.ows.helper;


import com.goodsogood.ows.model.vo.UpLoadVo;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
        //String Path = req.getSession().getServletContext().getRealPath("/");
        String realPath = urls;// + "/" + format Path +
        File folder = new File(realPath);
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
            File newFile=new File(folder, newName);
            file.transferTo(newFile);
            //协议，域名，端口号，地址，文件 req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()+
            String url =realPath+newName;
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
        vo.setMsg("上传失败");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());

       // String Path = req.getSession().getServletContext().getRealPath("root/"+urls+"/");//Path +
        String realPath =urls ; //+ "/" + format
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //获取文件原名称
        String oldName = file.getOriginalFilename();
        //获取文件大小
        Long size = file.getSize();
        if (size > 1024 * 1024* 5) {
            vo.setCode(10002);
            vo.setMsg("文件大小不能大于5M");
        }
        //获取文件后缀
        String hz = oldName.substring(oldName.lastIndexOf("."));
        if (hz.equals(".jpg") || hz.equals(".png")) {
            String newName = UUID.randomUUID().toString() + hz;
            file.transferTo(new File(folder, newName));
            double scale = 1.0d ;
            String oldPath=new File(folder, newName).getPath();

            String path=new File(folder, "y"+newName).getPath();
            String url = realPath+ "/" + newName;
            if(size >= 5 * 1024 * 1024) {
                if (size > 0) {
                    scale = (5 * 1024f *  1024f) / size;
                }
                if (size < 5 * 1024 * 1024) {
                    Thumbnails.of(oldPath).scale(1).toFile(path);
                } else {
                    Thumbnails.of(oldPath).scale(0.5f).outputQuality(scale).toFile(path);
                    new File(folder, newName).delete();
                    url = realPath+ "/y" + newName;
                }
            }
            //协议，域名，端口号，地址，文件 //req.getScheme() +":"+"//" + req.getServerName() + ":" + req.getServerPort()+ "/"+format + "/y" + newName
            vo.setSuccess(true);
            vo.setMsg("上传成功");
            vo.setCode(200);
            vo.setData(url);
            return vo;
        } else {
            return vo;
        }

    }

    public  UpLoadVo downloadImage(String url,HttpServletRequest request, HttpServletResponse response) {
        UpLoadVo vo=new UpLoadVo();
        vo.setSuccess(false);
        vo.setCode(10001);
        vo.setMsg("文件下载失败");
        String Path = request.getSession().getServletContext().getRealPath("/");
        if(!url.isEmpty())
        {
            File file=new File(Path+url);
            String imageUrl = file.getName();
            if(file.exists())
            {
                response.setContentType("application/octet-stream"); //设置强制下载 不打开 force-download
                response.addHeader("Content-Disposition","attachment;fileName="+url);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return  vo;
    }
}
