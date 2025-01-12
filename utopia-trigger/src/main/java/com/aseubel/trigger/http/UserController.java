package com.aseubel.trigger.http;

import com.aseubel.api.UserInterface;
import com.aseubel.api.dto.user.LoginRequestDTO;
import com.aseubel.api.dto.user.LoginResponseDTO;
import com.aseubel.domain.user.model.UserEntity;
import com.aseubel.domain.user.service.IUserService;
import com.aseubel.types.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/user")
@RequiredArgsConstructor
public class UserController implements UserInterface {

    private final IUserService userService;

    @Override
    @PostMapping("/login")
    public Response<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request, HttpServletResponse response) {
        UserEntity user = userService.login(loginRequestDTO.getCode());

        return Response.SYSTEM_SUCCESS(
                LoginResponseDTO.builder()
                        .userId(user.getOpenid())
                        .token(user.getToken())
                        .build());
    }
}
