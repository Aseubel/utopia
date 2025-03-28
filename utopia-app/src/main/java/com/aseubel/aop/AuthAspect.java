package com.aseubel.aop;

import com.aseubel.domain.user.adapter.repo.IAdminRepository;
import com.aseubel.types.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author yangzhiyao
 * @description 删帖删评鉴权切面
 * @date 2025-03-28 18:08
 */
@Aspect
@Component
@Slf4j
public class AuthAspect {

    @Resource
    private IAdminRepository adminRepository;

    /**
     * 拦截入口
     */
    @Pointcut("@annotation(com.aseubel.types.constraint.Auth)")
    public void pointCut(){
    }

    @Around("pointCut()")
    public Object checkAuth(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("ServletRequestAttributes is null");
        }
        HttpServletResponse response = attributes.getResponse();
        Object arg = point.getArgs()[0];

        String userId = (String) arg.getClass().getMethod("getUserId").invoke(arg);
        String schoolCode = (String) arg.getClass().getMethod("getSchoolCode").invoke(arg);
        boolean isAdmin = adminRepository.isAdmin(userId, schoolCode);
        if (!isAdmin) {
            if (response != null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
            return Response.builder().code(HttpStatus.UNAUTHORIZED.value()).info("用户无权限").build();
        }
        return point.proceed();
    }

}
