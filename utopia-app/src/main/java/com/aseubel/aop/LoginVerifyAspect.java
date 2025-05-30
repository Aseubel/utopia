package com.aseubel.aop;

import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.properties.JwtProperties;
import com.aseubel.types.Response;
import com.aseubel.types.enums.GlobalServiceStatusCode;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.JwtUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.aseubel.types.common.Constant.ACCESS_EXPIRE_TIME;
import static com.aseubel.types.common.Constant.USER_ID_KEY;

/**
 * @author Aseubel
 * @description 登录校验切面
 * @date 2025-01-11 21:01
 */
@Aspect
@Component
@Slf4j
public class LoginVerifyAspect {

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IRedisService redisService;

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

        try {
            // 校验redis中是否有token，没有就是过期
//            log.info("redis校验accessToken，id:{}，token:{}", userId, token);
            String accessToken = redisService.getValue(RedisKeyBuilder.userAccessTokenKey(userId));
            // token为空过期
            if (accessToken == null) {
                Optional.ofNullable(response).ifPresent(r -> r.setStatus(401));
                return Response.CUSTOMIZE_ERROR(GlobalServiceStatusCode.USER_TOKEN_EXPIRED);
            }
            // 校验token是否正确
            if (!accessToken.equals(token)) {
                Optional.ofNullable(response).ifPresent(r -> r.setStatus(401));
                return Response.CUSTOMIZE_ERROR(GlobalServiceStatusCode.USER_TOKEN_ERROR);
            }
        } catch (Exception e) {
            log.error("redis校验accessToken失败！id:{}，token:{}", userId, token);
            // redis宕机，解码校验令牌
            try {
                Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
                // 校验用户id
                if (!claims.get(USER_ID_KEY).equals(userId)) {
                    throw new AppException("用户id与accessToken不匹配！");
                }
//                log.info("用户进行jwt校验通过，id:{}，token:{}", userId, token);
            } catch (AppException ae) {
                throw ae;
            } catch (Exception ex) {
                // 不通过，响应401状态码
                log.error("用户进行jwt校验失败！id:{}，token:{}", userId, token);;
                if (response != null) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                }
                return Response.CUSTOMIZE_ERROR(GlobalServiceStatusCode.USER_TOKEN_ERROR);
            }
        }
        String newToken = userRepository.generateUserToken(
                userId, jwtProperties.getSecretKey(), jwtProperties.getAccess_ttl());
        // 更新redis中token
        redisService.setValue(RedisKeyBuilder.userAccessTokenKey(userId), newToken, ACCESS_EXPIRE_TIME);
        // 设置token到响应头
        Optional.ofNullable(response)
                .ifPresent(r -> r.setHeader(jwtProperties.getTokenName(), newToken));
//        log.info("用户进行登录校验通过，id:{}，token:{}", userId, newToken);
        return point.proceed();
    }

}
