package com.cfgglobal.test.exception;

import com.cfgglobal.test.base.ApiResp;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity constraintViolationExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        ConstraintViolationException rootCause = (ConstraintViolationException) e;

        ApiResp apiResp = new ApiResp();
        String message;
        message = List.ofAll(rootCause.getConstraintViolations())
                .map(e2 -> e2.getPropertyPath() + " " + e2.getMessage() + ", but the actual value is " + e2.getInvalidValue())
                .mkString(";");
        apiResp.setError(message);
        return ResponseEntity.badRequest().body(apiResp);
    }

    //
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResp> noPermission(HttpServletRequest req, Exception e) throws Exception {
        ApiResp apiResp = new ApiResp();
        apiResp.setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResp);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResp> methodNotSupported(HttpServletRequest req, Exception e) throws Exception {
        ApiResp apiResp = new ApiResp();
        apiResp.setError("method " + req.getMethod() + " ,url " + req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResp);
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResp> httpMessageNotReadableException(HttpServletRequest req, Exception e) throws Exception {
        ApiResp apiResp = new ApiResp();
        apiResp.setError(e.getMessage());
        return ResponseEntity.badRequest().body(apiResp);
    }


    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResp> illegalArgumentException(Exception e) throws Exception {
        log.error(e.getMessage(), e);
        ApiResp apiResp = new ApiResp();
        apiResp.setError(e.getMessage());
        return ResponseEntity.badRequest().body(apiResp);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ApiResp> sqlErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        Throwable rootCause = ((DataIntegrityViolationException) e).getRootCause();
        String message = rootCause.getMessage();
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        if (message.contains("Duplicate entry")) {
            //Duplicate entry 'zhouleib1412@gmail.com' for key
            message = StringUtils.substringBetween(message, "Duplicate entry", " for key");
            message = "Duplicate data: " + message;
        } else {
            // 外键约束
            String db = "collinson";
            message = StringUtils.substringBetween(message, "a foreign key constraint fails (`" + db + "`.`", "`, CONSTRAINT");
            message = "It's used by a " + message;

        }
        return ResponseEntity.badRequest().body(new ApiResp().setError(message).setMessage(e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        e.printStackTrace();
        mav.setViewName("error");

        if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
            mav.addObject("msg", e.getMessage());
            mav.setViewName("ajaxError");
        }
        return mav;
    }
}