package com.movie.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一返回格式工具了类
 */
public class Utils {
    public static Map<Integer,String> statusCode = new HashMap<>();
    static {
        statusCode.put(200, "操作成功");
        statusCode.put(201, "对象创建成功");
        statusCode.put(202, "请求已经被接受");
        statusCode.put(204, "操作已经执行成功，但是没有返回数据");
        statusCode.put(301, "资源已被移除");
        statusCode.put(303, "重定向");
        statusCode.put(304, "资源没有被修改");
        statusCode.put(400, "参数列表错误（缺少，格式不匹配)");
        statusCode.put(401, "未授权");
        statusCode.put(403, "访问受限，授权过期");
        statusCode.put(404, "资源，服务未找到");
        statusCode.put(405, "不允许的http方法");
        statusCode.put(409, "资源冲突，或者资源被锁");
        statusCode.put(415, "不支持的数据，媒体类型");
        statusCode.put(500, "系统内部错误");
        statusCode.put(501, "接口未实现");
    }

    // 普通查询成功
    public static DataGridView resSuccess(Integer code, String message,  Object data){
        if(message.equals("")){
            message = statusCode.get(code);
        }
        return new DataGridView(code, message, data);
    }

    // 查询失败
    public static DataGridView resFailure(Integer code, String message){
        if("".equals(message)){
           message = statusCode.get(code);
        }
        return new DataGridView(code,message);
    }

    //分页查询成功
    public static DataGridView pagSuccess(Integer code, String message, Long count, Object data){
        if(message.equals("")){
            message = statusCode.get(code);
        }
        return new DataGridView(code, message, count, data);
    }


}
