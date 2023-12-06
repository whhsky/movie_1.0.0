package com.user.domain.entity;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import com.movie.utils.validated.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * 用户表(User)实体类
 *
 * @author makejava
 * @since 2023-07-10 15:43:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class) // 将下划线映射为驼峰
public class SysUser implements Serializable {
    private static final long serialVersionUID = 313789675264879537L;
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空", groups = {ValidGroup.Insert.class})
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{5,11}$", message = "用户名长度为6-12位且格式为以字母开头,包括字母，数字",groups = {ValidGroup.Insert.class})
    private String userName;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空", groups = {ValidGroup.Insert.class})
    @Pattern(regexp = "^[a-zA-Z0-9_!@#$%^&*-+=]{6,16}$", message = "密码长度为6-16位且格式为以字母开头,包括字母，数字，特殊符号，下划线",groups = {ValidGroup.Insert.class})
    private String password;
    /**
     * 地址
     */
    private String address;
    /**
     * 性别
     */
    @Max(value = 1, message = "性别输入有误", groups = {ValidGroup.Update.class})
    @Min(value = 0, message = "性别输入有误", groups = {ValidGroup.Update.class})
    @NotNull(message = "性别不能为空！！！", groups = {ValidGroup.Update.class})
    private String sex;
    /**
     * 签名
     */
    private String sign;
    /**
     * 邮箱
     */
    @Email(message = "邮箱不合法", groups = {ValidGroup.Update.class})
    private String eMail;
    /**
     * 头像
     */
    private String icon;

    /**
     * 钱包
     */
    private Double overage;

    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "请正确输入手机号", groups = {ValidGroup.All.class})
    private String phone;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "删除时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deleteTime;

}

