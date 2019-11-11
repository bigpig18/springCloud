package com.source.coupon.advice;

import com.source.coupon.exception.CouponException;
import com.source.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述: 全局异常处理
 *
 * @author li
 * @date 2019/10/31
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 对 CouponException 异常 进行统一处理
     * @param request request
     * @param e CouponException
     * @return CommonResponse
     */
    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handlerCouponException(HttpServletRequest request, CouponException e){
        CommonResponse<String> response = new CommonResponse<>(-1,"business error");
        response.setData(e.getMessage());
        return response;
    }
}
