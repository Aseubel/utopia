package com.aseubel.config;

import com.aseubel.filter.RateLimitingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aseubel
 * @date 2025/4/28 下午8:19
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilterRegistrationBean() {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter());
        // 配置过滤器拦截的 URL 模式，例如拦截所有请求
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
