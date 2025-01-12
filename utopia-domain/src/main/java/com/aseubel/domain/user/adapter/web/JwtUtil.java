package com.aseubel.domain.user.adapter.web;

import java.util.Map;

/**
 * @author Aseubel
 * @description JwtUtil
 * @date 2025-01-12 19:23
 */
public interface JwtUtil {

    String createJWT(String secretKey, Long ttl, Map<String, Object> claims);

}
