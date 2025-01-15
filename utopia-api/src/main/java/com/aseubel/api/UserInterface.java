package com.aseubel.api;

import com.aseubel.api.dto.user.*;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Aseubel
 * @description 用户服务接口
 * @date 2025-01-11 21:03
 */
public interface UserInterface {

    /**
     * 用户登录
     * @param loginRequestDTO 登录请求DTO，仅包含登录凭证code
     * @return 登录响应DTO
     */
    Response<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO);

    /**
     * 查询用户信息
     * @param queryUserInfoRequestDTO 查询用户信息请求DTO，仅包含用户openid
     * @return 查询用户信息响应DTO
     */
    Response<QueryUserInfoResponseDTO> queryUserInfo(@Valid QueryUserInfoRequestDTO queryUserInfoRequestDTO);

    /**
     * 刷新token
     * @param refreshTokenRequestDTO 刷新token请求DTO，包含refreshToken和userId
     * @return 新的refresh_token和access_token
     */
    Response<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO);

}
