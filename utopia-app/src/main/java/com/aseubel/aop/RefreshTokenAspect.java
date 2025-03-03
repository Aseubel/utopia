package com.aseubel.aop;

import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.properties.JwtProperties;
import com.aseubel.types.util.JwtUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.aseubel.types.common.Constant.ACCESS_TOKEN;
import static com.aseubel.types.common.Constant.USER_ID_KEY;

/**
 * @author Aseubel
 * @description 刷新token切面
 * @date 2025-02-22 11:05
 */
@Aspect
@Component
@Slf4j
public class RefreshTokenAspect {

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IRedisService redisService;

    /**
     * 拦截入口
     */
    @Pointcut("@annotation(com.aseubel.types.constraint.Refresh)")
    public void pointCut(){
    }

    @Around("pointCut()")
    public Object refreshToken(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        Object arg = point.getArgs()[0];
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 获取用户ID
        String userId = (String) arg.getClass().getMethod("getUserId").invoke(arg);
        String token = request.getHeader(jwtProperties.getTokenName());
        // token为空，用户未登录，直接返回不需要刷新token
        if(!isLogin(userId, token)) {
            return point.proceed();
        }

        try {
            log.info("RefreshTokenAspect：redis校验accessToken，id:{}，token:{}", userId, token);
            String accessToken = redisService.getFromMap(
                    RedisKeyBuilder.UserTokenKey(userId), ACCESS_TOKEN);
            // token为空或与redis中不匹配
            if (accessToken == null || !accessToken.equals(token)) {
                return point.proceed();
            }
        } catch (Exception e) {
            log.warn("RefreshTokenAspect：redis校验accessToken失败！id:{}，token:{}", userId, token);
            // redis宕机，解码校验令牌
            try {
                Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
                // 校验用户id
                if (!claims.get(USER_ID_KEY).equals(userId)) {
                    return point.proceed();
                }
                log.info("RefreshTokenAspect：用户进行jwt校验通过，id:{}，token:{}", userId, token);
            } catch (Exception ex) {
                // 不通过，响应 401 状态码
                log.warn("RefreshTokenAspect：用户进行jwt校验失败！id:{}，token:{}", userId, token);
                return point.proceed();
            }
        }
        String newToken = userRepository.generateUserToken(
                userId, jwtProperties.getSecretKey(), jwtProperties.getAccess_ttl());
        // 更新redis中token
        redisService.addToMap(RedisKeyBuilder.UserTokenKey(userId), ACCESS_TOKEN, newToken);
        // 设置token到响应头
        Optional.ofNullable(response)
                .ifPresent(r -> r.setHeader(jwtProperties.getTokenName(), newToken));
        log.info("RefreshTokenAspect：用户进行登录校验通过，id:{}，token:{}", userId, newToken);
        return point.proceed();
    }

    private boolean isLogin(String userId, String token) {
        return !(StringUtils.isEmpty(token) || StringUtils.isEmpty(userId));
    }

}
