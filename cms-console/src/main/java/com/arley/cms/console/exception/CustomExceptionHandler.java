package com.arley.cms.console.exception;

import com.alibaba.fastjson.JSON;
import com.arley.cms.console.constant.PublicCodeEnum;
import com.arley.cms.console.util.AnswerBody;
import com.arley.cms.console.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XueXianlei
 * @Description: 异常处理
 * @date Created in 2018/4/8 15:14
 */
@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AnswerBody exception(Exception exception, HttpServletRequest request){
        exception.printStackTrace();
        AnswerBody body = AnswerBody.buildAnswerBody(PublicCodeEnum.ERROR.getCode(), PublicCodeEnum.ERROR.getMsg());
        logger.error("系统异常={} | 请求路径={} | 请求IP={} | 请求参数={} | 响应内容={}",
                exception.getMessage(),
                request.getRequestURI(),
                RequestUtils.getClientIpAddr(request),
                RequestUtils.getAllRequestParam(request),
                JSON.toJSONString(body));
        return body;
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public AnswerBody customException(CustomException exception, HttpServletRequest request){
        AnswerBody body = AnswerBody.buildAnswerBody(exception.getCode(), exception.getMsg());
        if (CustomException.LOGGER_INFO_TYPE.equals(exception.getType())) {
            logger.info("自定义异常={} | 请求路径={} | 请求IP={} | 请求参数={} | 响应内容={}",
                    exception.getMessage(),
                    request.getRequestURI(),
                    RequestUtils.getClientIpAddr(request),
                    RequestUtils.getAllRequestParam(request),
                    JSON.toJSONString(body));
        } else if (CustomException.LOGGER_WARN_TYPE.equals(exception.getType())) {
            logger.warn("自定义异常={} | 请求路径={} | 请求IP={} | 请求参数={} | 响应内容={}",
                    exception.getMessage(),
                    request.getRequestURI(),
                    RequestUtils.getClientIpAddr(request),
                    RequestUtils.getAllRequestParam(request),
                    JSON.toJSONString(body));
        } else if (CustomException.LOGGER_ERROR_TYPE.equals(exception.getType())) {
            logger.error("自定义异常={} | 请求路径={} | 请求IP={} | 请求参数={} | 响应内容={}",
                    exception.getMessage(),
                    request.getRequestURI(),
                    RequestUtils.getClientIpAddr(request),
                    RequestUtils.getAllRequestParam(request),
                    JSON.toJSONString(body));
        }
        return body;
    }
}
