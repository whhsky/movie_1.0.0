package com.sysdict.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysDictData)实体类
 *
 * @author makejava
 * @since 2023-07-12 12:45:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class)
public class SysDictData implements Serializable {
    private static final long serialVersionUID = -31968402474508660L;
    
    private Integer dictCode;
    
    private Integer dictSort;
    
    private String dictLabel;
    
    private String dictValue;
    
    private String dictType;
    
    private String cssClass;
    
    private String listClass;
    
    private String isDefault;
    
    private Integer status;
    
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    
    private String remark;


}

