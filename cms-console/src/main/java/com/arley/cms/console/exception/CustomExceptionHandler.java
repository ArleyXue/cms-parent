package com.arley.cms.console.exception;

import com.arley.cms.console.constant.CodeEnum;
import com.arley.cms.console.util.AnswerBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public AnswerBody exception(Exception exception){
        logger.error("异常={}", exception.getMessage());
        exception.printStackTrace();
        return AnswerBody.getInstance(CodeEnum.ERROR.getCode(), CodeEnum.ERROR.getMsg());
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public AnswerBody customException(CustomException exception){
        logger.error("自定义异常={}", exception.getMessage());
        return AnswerBody.getInstance(exception.getCode(), exception.getMsg());
    }
}
