package com.movie.sysdict.mapper;
import com.sysdict.domain.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface SysDictDataMapper {
    SysDictData getSysDictData(Integer dictCode);
    List<Map<String, Object>> getSysDictDataType(String[] dictType);

}
