package com.order.domain.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(List.class)
public class MyJsonTypeHandler extends BaseTypeHandler<List<List<Integer>>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<List<Integer>> parameter, JdbcType jdbcType) throws SQLException {
        // 将 List<List<Integer>> 转换成字符串后，写入数据库
        String json = convertToJson(parameter);
        ps.setString(i, json);
    }

    @Override
    public List<List<Integer>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从数据库中读取字符串，然后将其转换为 List<List<Integer>> 类型
        String json = rs.getString(columnName);
        return convertToList(json);
    }

    @Override
    public List<List<Integer>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return convertToList(json);
    }

    @Override
    public List<List<Integer>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return convertToList(json);
    }

    // 将 List<List<Integer>> 转换成 JSON 字符串
    public String convertToJson(List<List<Integer>> list) {
        // 在此实现将 list 转换成 JSON 字符串的方法
        return JSON.toJSONString(list); // 示例，需要替换成实际的转换逻辑
    }

    // 将 JSON 字符串转换为 List<List<Integer>> 类型
    public List<List<Integer>> convertToList(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<List<Integer>> twoDimensionalList = null;
        try {
            twoDimensionalList = objectMapper.readValue(jsonString, new TypeReference<List<List<Integer>>>() {});
            // 输出结果
            for (List<Integer> list : twoDimensionalList) {
                System.out.println(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 解析 JSON 字符串，将其转换为 List<List<Integer>> 类型
        // 示例代码，实际需要解析 JSON 字符串
        return twoDimensionalList;
    }

    public static List<List<Integer>> StringToList(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<List<Integer>> twoDimensionalList;
        try {
            twoDimensionalList = objectMapper.readValue(jsonString, new TypeReference<List<List<Integer>>>() {});
            // 输出结果
            for (List<Integer> list : twoDimensionalList) {
                System.out.println(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // 解析 JSON 字符串，将其转换为 List<List<Integer>> 类型
        // 示例代码，实际需要解析 JSON 字符串
        return twoDimensionalList;
    }
}
