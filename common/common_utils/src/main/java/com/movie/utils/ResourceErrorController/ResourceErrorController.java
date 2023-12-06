package com.movie.utils.ResourceErrorController;

import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ResourceErrorController {

    @RequestMapping("/resource_not_found")
    public DataGridView resource_not_found(){
        return Utils.resFailure(404, "资源未知！");
    }

    @RequestMapping("/resource_500")
    public DataGridView resource_500(){
        return Utils.resFailure(500, "服务器内部错误！");
    }
}
