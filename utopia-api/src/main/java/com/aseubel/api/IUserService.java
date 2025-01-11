package com.aseubel.api;

import com.aseubel.api.dto.user.LoginRequestDTO;
import com.aseubel.api.dto.user.LoginResponseDTO;
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
public interface IUserService {

    /**
     * 用户登录
     * @param loginRequestDTO 登录请求DTO，仅包含登录凭证code
     * @param request 请求对象
     * @param response 响应对象
     * @return 登录响应DTO
     */
    Response<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request, HttpServletResponse response);

}
