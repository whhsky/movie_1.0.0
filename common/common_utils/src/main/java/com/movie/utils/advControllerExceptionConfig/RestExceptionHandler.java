package com.movie.utils.advControllerExceptionConfig;


import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

import static com.movie.utils.argumentResolverConfig.UnderlineToCamelArgumentResolver.translate;


/**
 * @author:
 * @date 2023/8/12
 * @Description:
 * 服务层全局响应异常处理器
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public DataGridView exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return Utils.resFailure(500,"");
    }


    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<DataGridView> handleValidatedException(Exception e) {
        DataGridView dataGridView =null;
        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation exception
            try {
                MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
                dataGridView = Utils.resFailure(400, ex.getMessage());
            }catch (Exception exception){
                log.error("在进行校验的时候出现错误：{}",e.getMessage());
                return new ResponseEntity<>(dataGridView,HttpStatus.BAD_REQUEST);
            }
//            log.error("方法参数校验异常:{}",e.getMessage());
            return new ResponseEntity<>(dataGridView,HttpStatus.OK);
        } else if(e instanceof ConstraintViolationException){
            String[] msgList = e.getMessage().split("[.]")[1].split(":");
            String msg = translate(msgList[0]) +":"+ msgList[1];
            dataGridView = Utils.resFailure(400, msg);
            log.error("参数校验异常:{}",e.getMessage());
            return new ResponseEntity<>(dataGridView,HttpStatus.OK);
        } else if (e instanceof BindException) {
            // BeanValidation GET object param
            BindException ex = (BindException) e;
            dataGridView = Utils.resFailure(500,
                    ex.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; "))
            );
            log.error("BindException异常:{}", e.getMessage());
            return new ResponseEntity<>(dataGridView,HttpStatus.BAD_REQUEST);
        }
        log.error(e.getClass().getName() + "出现异常:{}",e.getMessage());
        return new ResponseEntity<>(dataGridView,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<DataGridView> httpRequestMethod(Exception e) {
        log.error("请求方法异常:{}",e.getMessage());
        DataGridView dataGridView = Utils.resFailure(405, "请求方法有误");
        return new ResponseEntity<>(dataGridView,HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<DataGridView> httpRequestMediaTypeNot(Exception e) {
        log.error("Content-Type异常:{}",e.getMessage());
        DataGridView dataGridView = Utils.resFailure(403, "Content type有误");
        return new ResponseEntity<>(dataGridView,HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, NotWritablePropertyException.class})
    public ResponseEntity<DataGridView> HttpMessageNotReadable(Exception e) {
        log.error("请求参数异常:{}",e.getMessage());
        DataGridView dataGridView = Utils.resFailure(400, "请求参数有误");
        return new ResponseEntity<>(dataGridView,HttpStatus.BAD_REQUEST);
    }

}
