package jayfeng.com.meituan.manager.useraccesskey.handler;

import jayfeng.com.meituan.manager.useraccesskey.constant.ResponseFailCodeConstant;
import jayfeng.com.meituan.manager.useraccesskey.controller.BaseController;
import jayfeng.com.meituan.manager.useraccesskey.exception.RequestForbiddenException;
import jayfeng.com.meituan.manager.useraccesskey.exception.ServerBusyException;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 * @author JayFeng
 * @date 2021/4/2
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler extends BaseController {

    private static final Integer KNOWN_EXCEPTION = 500; // 服务端异常

    private static final Integer SERVER_BUSY = 503; // 服务端繁忙

    private static final Integer UNKNOWN_EXCEPTION = 999; // 未知异常

    private static final Integer REQUEST_FORBIDDEN = 403; // 请求拒绝处理

    private static final Integer METHOD_NOT_SUPPORTED = 405; // 请求方式不对

    @ExceptionHandler(Exception.class)
    public ResponseMessage handler(Exception e) {
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        log.info("出现异常, 异常类型: {}", e.toString());
        log.info("异常位置: {} 类的第 {} 行, 出现异常的方法: {}", stackTraceElement.getClassName(), stackTraceElement.getLineNumber(), stackTraceElement.getMethodName());
        // 请求方式不被支持
        if (e.getClass() == HttpRequestMethodNotSupportedException.class) {
            HttpRequestMethodNotSupportedException exception = (HttpRequestMethodNotSupportedException) e;
            return requestFail(ResponseFailCodeConstant.METHOD_NOT_SUPPORTED.getResponseCode(), "请求方式不被支持", "request_method_not_allowed");
        }
        // 无权限访问
        if (e.getClass() == RequestForbiddenException.class) {
            RequestForbiddenException exception = (RequestForbiddenException) e;
            return requestFail(ResponseFailCodeConstant.REQUEST_FORBIDDEN.getResponseCode(), exception.getMessage(), "request_forbidden");
        }
        // 服务端繁忙
        if (e.getClass() == ServerBusyException.class) {
            ServerBusyException exception = (ServerBusyException) e;
            return requestFail(ResponseFailCodeConstant.SERVER_BUSY.getResponseCode(), exception.getMessage(), "server_busy");
        }
        // 未知异常
        return requestFail(ResponseFailCodeConstant.UNKNOWN_EXCEPTION.getResponseCode(), "服务器崩溃", "server_crack_up");
    }

}
