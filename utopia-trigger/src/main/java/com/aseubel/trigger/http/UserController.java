package com.aseubel.trigger.http;

import com.aseubel.api.UserInterface;
import com.aseubel.api.dto.user.*;
import com.aseubel.domain.user.model.UserEntity;
import com.aseubel.domain.user.service.IUserService;
import com.aseubel.types.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/user/") //${app.config.api-version}
@RequiredArgsConstructor
public class UserController implements UserInterface {

    private final IUserService userService;

    /**
     * 登录
     */
    @Override
    @PostMapping("/login")
    public Response<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        UserEntity user = userService.login(loginRequestDTO.getCode());

        return Response.SYSTEM_SUCCESS(
                LoginResponseDTO.builder()
                        .userId(user.getOpenid())
                        .userName(user.getUserName())
                        .realName(user.getRealName())
                        .phone(user.getPhone())
                        .gender(user.getGender())
                        .avatar(user.getAvatar())
                        .signature(user.getSignature())
                        .refreshToken(user.getRefreshToken())
                        .accessToken(user.getAccessToken())
                        .build());
    }

    /**
     * 登出
     */
    @Override
    @PostMapping("/logout")
    public Response logout(@Valid @RequestBody LogoutRequestDTO logoutRequestDTO) {
        userService.logout(logoutRequestDTO.getUserId());

        return Response.SYSTEM_SUCCESS();
    }

    /**
     * 刷新token
     */
    @Override
    @PutMapping("/refresh")
    public Response<RefreshTokenResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        UserEntity user = userService.refreshToken(
                UserEntity.builder()
                        .openid(refreshTokenRequestDTO.getUserId())
                        .refreshToken(refreshTokenRequestDTO.getRefreshToken())
                        .build());

        return Response.SYSTEM_SUCCESS(
                RefreshTokenResponseDTO.builder()
                        .accessToken(user.getAccessToken())
                        .refreshToken(user.getRefreshToken())
                        .build());
    }

    /**
     * 查询用户信息
     */
    @Override
    @GetMapping("/info")
    public Response<QueryUserInfoResponseDTO> queryUserInfo(@Valid @RequestBody QueryUserInfoRequestDTO queryUserInfoRequestDTO) {
        UserEntity user = userService.queryUserInfo(queryUserInfoRequestDTO.getUserId());

        return Response.SYSTEM_SUCCESS(
                QueryUserInfoResponseDTO.builder()
                        .userId(user.getOpenid())
                        .userName(user.getUserName())
                        .realName(user.getRealName())
                        .phone(user.getPhone())
                        .gender(user.getGender())
                        .avatar(user.getAvatar())
                        .signature(user.getSignature())
                        .build());
    }

    /**
     * 修改用户信息
     */
    @Override
    @PutMapping("/info")
    public Response modifyUserInfo(@Valid @RequestBody ModifyUserInfoRequestDTO modifyUserInfoRequestDTO) {
        userService.updateUserInfo(UserEntity.builder()
                .openid(modifyUserInfoRequestDTO.getUserId())
                .userName(modifyUserInfoRequestDTO.getUserName())
                .realName(modifyUserInfoRequestDTO.getRealName())
                .phone(modifyUserInfoRequestDTO.getPhone())
                .gender(modifyUserInfoRequestDTO.getGender())
                .avatar(modifyUserInfoRequestDTO.getAvatar())
                .signature(modifyUserInfoRequestDTO.getSignature())
                .build());

        return Response.SYSTEM_SUCCESS();
    }


}
