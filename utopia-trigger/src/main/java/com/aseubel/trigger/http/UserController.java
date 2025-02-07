package com.aseubel.trigger.http;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.UserInterface;
import com.aseubel.api.dto.community.QueryIndexDiscussPostResponseDTO;
import com.aseubel.api.dto.user.UploadAvatarRequestDTO;
import com.aseubel.api.dto.user.UploadAvatarResponseDTO;
import com.aseubel.api.dto.user.*;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.community.service.ICommunityService;
import com.aseubel.domain.user.model.entity.AvatarEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.domain.user.model.vo.School;
import com.aseubel.domain.user.service.IUserService;
import com.aseubel.types.Response;
import com.aseubel.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_UPLOAD_ERROR;
import static com.aseubel.types.enums.GlobalServiceStatusCode.PARAM_NOT_COMPLETE;

@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/user/") //${app.config.api-version}
@RequiredArgsConstructor
public class UserController implements UserInterface {

    private final IUserService userService;

    private final ICommunityService communityService;

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
                        .school(user.getSchool())
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
                        .school(user.getSchool())
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
                .school(School.builder().schoolCode(modifyUserInfoRequestDTO.getSchoolCode()).build())
                .build());

        return Response.SYSTEM_SUCCESS();
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    public Response<UploadAvatarResponseDTO> uploadAvatar(@Valid @ModelAttribute UploadAvatarRequestDTO uploadAvatarRequestDTO) {
        try {
            if (StringUtils.isEmpty(uploadAvatarRequestDTO.getAvatar().getOriginalFilename()) || StringUtils.isEmpty(uploadAvatarRequestDTO.getUserId())) {
                throw new AppException(PARAM_NOT_COMPLETE);
            }
            MultipartFile avatar = uploadAvatarRequestDTO.getAvatar();

            String avatarUrl = userService.uploadAvatar(
                    AvatarEntity.builder()
                    .avatar(avatar)
                    .userId(uploadAvatarRequestDTO.getUserId())
                    .build());

            return Response.SYSTEM_SUCCESS(UploadAvatarResponseDTO.builder().avatarUrl(avatarUrl).build());
        } catch (ClientException e) {
            log.error("上传头像时oss服务异常，{}, code:{}, message:{}",OSS_UPLOAD_ERROR.getMessage(), e.getErrCode(), e.getErrMsg(), e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        } catch (Exception e) {
            log.error("上传头像时出现未知异常", e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        }
    }

    /**
     * 查询用户收藏的帖子
     */
    @Override
    @GetMapping("/favorite/post")
    public Response<List<QueryFavoriteDiscussPostResponseDTO>> queryFavoriteDiscussPost(@Valid @RequestBody QueryFavoriteDiscussPostRequestDTO requestDTO) {
        List<DiscussPostEntity> discussPosts = communityService.queryUserFavoritePosts(
                requestDTO.getUserId(), requestDTO.getPostId(), requestDTO.getLimit());
        List<QueryFavoriteDiscussPostResponseDTO> responseDTOs = new ArrayList<>();
        for (DiscussPostEntity discussPost : discussPosts) {
            responseDTOs.add(QueryFavoriteDiscussPostResponseDTO.builder()
                    .discussPostId(discussPost.getDiscussPostId())
                    .userName(discussPost.getUserName())
                    .userAvatar(discussPost.getUserAvatar())
                    .title(discussPost.getTitle())
                    .content(discussPost.getContent())
                    .likeCount(discussPost.getLikeCount())
                    .commentCount(discussPost.getCommentCount())
                    .forwardCount(discussPost.getForwardCount())
                    .createTime(discussPost.getCreateTime())
                    .updateTime(discussPost.getUpdateTime())
                    .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

}
