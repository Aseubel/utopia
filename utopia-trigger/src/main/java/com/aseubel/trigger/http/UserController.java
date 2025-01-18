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

    @Override
    @PutMapping("/refresh")
    public Response<RefreshTokenResponseDTO> refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
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

    @Override
    @GetMapping("/info")
    public Response<QueryUserInfoResponseDTO> queryUserInfo(@Valid QueryUserInfoRequestDTO queryUserInfoRequestDTO) {
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


}
