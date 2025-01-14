package com.aseubel.trigger.http;

import com.aseubel.api.UserInterface;
import com.aseubel.api.dto.user.LoginRequestDTO;
import com.aseubel.api.dto.user.LoginResponseDTO;
import com.aseubel.api.dto.user.QueryUserInfoRequestDTO;
import com.aseubel.api.dto.user.QueryUserInfoResponseDTO;
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
