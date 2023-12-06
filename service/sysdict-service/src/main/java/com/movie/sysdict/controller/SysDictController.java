package com.movie.sysdict.controller;

import com.movie.sysdict.mapper.SysDictDataMapper;
import com.movie.sysdict.service.ISysDictService;
import com.movie.utils.DataGridView;
import com.sysdict.domain.entity.SysDictData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation("类型控制类")
@RestController
@RequestMapping("/app/sys")
public class SysDictController {

    @Autowired
    private ISysDictService ISysDictService;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @ApiOperation("电影分类型")
    @GetMapping("/sys_data")
    public DataGridView sysData(){

        String[] dictTypes = new String[]{"movie_status", "movie_type", "movie_region", "movie_era", "cast_type"};
        return ISysDictService.getSysData(dictTypes);
    }

    @ApiOperation("影院分类型")
    @GetMapping("/cinema_data")
    public DataGridView cinemaData(){
        String[] dictTypes = new String[]{"cinema_brand", "administrative_district", "special_hall", "cinema_service"};
        return ISysDictService.getSysData(dictTypes);
    }

    /**
     * 基础数据供内部服务调用
     * @param Status
     * @return
     */
    @GetMapping("/inner/Sys_dict_data/{status}")
    public SysDictData SysDictData(@PathVariable("status") Integer Status) {
        return sysDictDataMapper.getSysDictData(Status);
    }
}
