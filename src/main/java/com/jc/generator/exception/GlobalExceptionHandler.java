package com.jc.generator.exception;

import com.jc.generator.common.req.ResponseConstant;
import com.jc.generator.common.req.RestBean;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Optional;

/**
 * 全局异常处理
 * @author hjc
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //捕获器1
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public RestBean<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        if (log.isErrorEnabled()) {
            log.error(ex.getMessage(), ex);
        }
        return RestBean.failure(ResponseConstant.ERROR_CODE, String.format("缺少必要参数[%s]", ex.getParameterName()), "");
    }
    //捕获器2
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public RestBean<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        if (log.isErrorEnabled()) {
            log.error(ex.getMessage(), ex);
        }
        BindingResult result = ex.getBindingResult();
        HashMap<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return RestBean.failure(ResponseConstant.ERROR_CODE, ResponseConstant.ERROR_MESSAGE,String.valueOf(errorMap));
    }
    //捕获器3
    @ExceptionHandler(value = {BindException.class})
    public RestBean<String> handleBindException(BindException ex) {
        if (log.isErrorEnabled()) {
            log.error(ex.getMessage(), ex);
        }
        BindingResult result = ex.getBindingResult();
        HashMap<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return RestBean.failure(ResponseConstant.ERROR_CODE, ResponseConstant.ERROR_MESSAGE,String.valueOf(errorMap));
    }
    //捕获器4
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public RestBean<String> handleConstraintViolationException(ConstraintViolationException ex) {
        if (log.isErrorEnabled()) {
            log.error(ex.getMessage(), ex);
        }
        Optional<ConstraintViolation<?>> first = ex.getConstraintViolations().stream().findFirst();
        return RestBean.failure(ResponseConstant.ERROR_CODE, first.isPresent() ? first.get().getMessage() : ResponseConstant.ERROR_MESSAGE);
    }
    //其他所有异常捕获器
//    @ExceptionHandler(Exception.class)
//    public RestBean<String> otherErrorDispose(Exception e) {
//        // 打印错误日志
//        log.error("错误代码({}),错误信息({})", ResponseConstant.ERROR_CODE, e.getMessage());
//        e.printStackTrace();
//        return RestBean.failure(ResponseConstant.ERROR_CODE, ResponseConstant.ERROR_MESSAGE, e.getMessage());
//    }
}
