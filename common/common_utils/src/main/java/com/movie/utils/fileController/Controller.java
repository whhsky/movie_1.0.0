package com.movie.utils.fileController;

import com.movie.utils.AppFileUtils;
import com.movie.utils.DataGridView;
import com.movie.utils.RandomUtils;
import com.movie.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RequestMapping("/app/file")
@RestController
public class Controller {

    @GetMapping("/download_show_file")
    public ResponseEntity<Object> downloadShowFile(String path, HttpServletResponse resp){
        return AppFileUtils.downloadFile(resp,path, "");
    }

    @ResponseBody
    @PostMapping("/upload_file")
    public DataGridView uploadFile(MultipartFile file) {
        try {


            String uploadPath = AppFileUtils.PATH; // 获取上传目录
            String dirName = RandomUtils.getCurrentDateForString();
            File dirFile = new File(uploadPath, dirName);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            String oldName = file.getOriginalFilename();//获取源文件名称
            String newName = RandomUtils.createFileNameUseTime(oldName, "_temp"); //获取随机新文件名
            File desc = new File(dirFile, newName); // 创建文件对象
            file.transferTo(desc); // 上传文件

            Map<String, Object> map = new HashMap<>();
            map.put("src", dirName+"/"+newName);
            return Utils.resSuccess(200, "", map);
        } catch (IOException e) {
            return Utils.resFailure(400, "上传失败！");
        } catch (IllegalStateException e) {
            return Utils.resFailure(400, "上传失败！");
        }
    }
}
