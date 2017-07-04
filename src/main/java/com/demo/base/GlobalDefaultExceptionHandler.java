package com.demo.base;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常配置
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletRequest request, Exception e){
        /*
        * 返回视图：
        * 定义一个ModelAndView即可，
        * 然后return;
        * 定义视图文件(比如：error.html,error.ftl,error.jsp);
        *
        */
        // 打印异常信息
        e.printStackTrace();
    }
}
