package com.movie.sysdict.service.impl;
import com.movie.sysdict.mapper.SysDictDataMapper;
import com.movie.sysdict.service.ISysDictService;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import com.sysdict.domain.entity.SysDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ISysDictServiceImpl implements ISysDictService {

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public DataGridView getSysData(String[] dictTypes) {

        /**
         *  电影首页基础数据
         *  @param request null:
         *  @return: 按电影状态/电影类型/电影区域/电影年代/演职人员类型分类
         *
         */
        try {
            List<Map<String, Object>> dataTypes = sysDictDataMapper.getSysDictDataType(dictTypes);
            HashMap<String, Object> resultMap = new HashMap<>();
            for (Map<String, Object> data: dataTypes) {
                List<SysDictData> sysDictDataList = new ArrayList<>();
                List<Object> resDictData = Arrays.asList(data.get("dict_data").toString().split(","));
                for (int i = 0; i < resDictData.size(); i+=2) {

                    SysDictData sysDictData = new SysDictData();
                    sysDictData.setDictLabel((String) resDictData.get(i));
                    sysDictData.setDictCode(Integer.parseInt((String) resDictData.get(i+1)));
                    sysDictDataList.add(sysDictData);
                }
                resultMap.put(data.get("dict_type")+"_list", sysDictDataList);
            }
            return Utils.resSuccess(200,"",resultMap);
        }catch (Exception e){
            return Utils.resFailure(400,"");
        }
    }
}