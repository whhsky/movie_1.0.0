package com.movie.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataGridView {
    private Integer code = 0;
    private String msg = "";
    private Long count;
    private Object data;  // 普通查询
    private Object rows;  // 普通查询

    public DataGridView() {
    }


    // 查询失败构造
    public DataGridView(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 查询成功构造
    public DataGridView(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 分页查询成功构造
    public DataGridView(Integer code, String msg, Long count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.rows = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
