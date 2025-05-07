package com.aseubel.filter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author Aseubel
 * @date 2025/4/28 下午8:15
 */
public class RateLimitingFilter implements Filter {

    private RateLimiter rateLimiter;
    private final double perClientLimit = 2.0;

//    ConcurrentHashMap<String, RateLimiter> clientLimiters = new ConcurrentHashMap<>();

    private final LoadingCache<String, RateLimiter> clientLimiters = Caffeine.newBuilder()
            .scheduler(Scheduler.systemScheduler()) // 使用系统调度器，避免多线程竞争
            .executor(ForkJoinPool.commonPool()) // 使用ForkJoinPool作为后台线程池，避免阻塞
            .weakValues() // 弱引用值，防止内存泄漏
            .refreshAfterWrite(Duration.ofMinutes(1)) // 每分钟刷新一次
            .expireAfterAccess(30, TimeUnit.MINUTES) // 30分钟无访问自动清理
            .maximumSize(10_000)                     // 防止内存溢出
            .recordStats()                           // 开启统计
            .build(ip -> RateLimiter.create(perClientLimit)); // 加载逻辑

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        rateLimiter = RateLimiter.create(50, Duration.ofSeconds(5));
        // Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String clientIP = request.getRemoteAddr();
//        RateLimiter clientLimiter = clientLimiters.computeIfAbsent(clientIP,
//                ip -> RateLimiter.create(perClientLimit));

        // 尝试获取令牌，获取不到则返回 429 Too Many Requests 状态码
        if (rateLimiter.tryAcquire() && clientLimiters.get(clientIP).tryAcquire()) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Rate limit exceeded");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
