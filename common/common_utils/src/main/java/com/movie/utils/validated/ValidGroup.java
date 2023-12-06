package com.movie.utils.validated;

import javax.validation.GroupSequence;

/**
 * 分组校验
 */
public class ValidGroup {

    // 新增使用
    public interface Insert{}

    // 更新使用
    public interface Update{}

    // 属性必须有这两个分组的才验证
    @GroupSequence({Insert.class, Update.class})
    public interface All{}
}
