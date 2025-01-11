package com.aseubel.aop;

import com.aseubel.infrastructure.util.JwtUtil;
import com.aseubel.properties.JwtProperties;
import com.aseubel.types.Response;
import com.aseubel.types.enums.GlobalServiceStatusCode;
import com.aseubel.types.exception.AppException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Aseubel
 * @description 登录校验切面
 * @date 2025-01-11 21:01
 */
@Aspect
@Component
@Slf4j
public class LoginVerifyAspect {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 拦截入口
     */
    @Pointcut("@annotation(com.aseubel.types.constraint.Login)")
    public void pointCut(){
    }

    @Around("pointCut()")
    public Object checkJwtToken(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        Object arg = point.getArgs()[0];
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 获取用户ID
        String userId = (String) arg.getClass().getMethod("getUserId").invoke(arg);
        String token = request.getHeader(jwtProperties.getTokenName());

        // 校验令牌
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            log.info("用户进行jwt校验通过，id:{}，token:{}", userId, token);
            return point.proceed();
        } catch (AppException e) {
            // 不通过，响应401状态码
            log.error("用户进行jwt校验失败！id:{}，token:{}", userId, token);;
            if (response != null) {
                response.setStatus(401);
            }
            return Response.CUSTOMIZE_ERROR(GlobalServiceStatusCode.USER_TOKEN_ERROR);
        }
    }

}
