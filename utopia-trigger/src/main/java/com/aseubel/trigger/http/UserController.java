package com.aseubel.trigger.http;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.UserInterface;
import com.aseubel.api.dto.user.*;
import com.aseubel.api.dto.user.post.*;
import com.aseubel.domain.bazaar.model.bo.BazaarBO;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.domain.bazaar.service.IBazaarService;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.community.service.ICommunityService;
import com.aseubel.domain.user.model.entity.AvatarEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.domain.user.model.vo.School;
import com.aseubel.domain.user.service.IUserService;
import com.aseubel.types.Response;
import com.aseubel.types.event.CancelAccountEvent;
import com.aseubel.types.exception.AppException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_UPLOAD_ERROR;
import static com.aseubel.types.enums.GlobalServiceStatusCode.PARAM_NOT_COMPLETE;

/**
 * 用户
 */
@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/user/") //${app.config.api-version}
@RequiredArgsConstructor
public class UserController implements UserInterface {

    private final IUserService userService;

    private final ICommunityService communityService;

    private final ApplicationEventPublisher eventPublisher;

    private final IBazaarService bazaarService;

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
     * 查询其他用户信息
     */
    @Override
    @GetMapping("/other/info")
    public Response<QueryOtherInfoResponse> queryOtherInfo(QueryOtherInfoRequest requestDTO) {
        UserEntity user = userService.queryOtherInfo(requestDTO.getUserId(), requestDTO.getTargetId());

        return Response.SYSTEM_SUCCESS(
                QueryOtherInfoResponse.builder()
                        .userId(user.getOpenid())
                        .userName(user.getUserName())
                        .gender(user.getGender())
                        .avatarUrl(user.getAvatar())
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
    @Override
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
            log.error("上传头像时oss服务异常，{}, code:{}, message:{}", OSS_UPLOAD_ERROR.getMessage(), e.getErrCode(), e.getErrMsg(), e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("上传头像时出现未知异常", e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        }
    }

    /**
     * 查询用户收藏的帖子
     */
    @Override
    @GetMapping("/post/favorite")
    public Response<List<QueryFavoriteDiscussPostResponseDTO>> queryFavoriteDiscussPost(QueryFavoriteDiscussPostRequestDTO requestDTO) {
        List<DiscussPostEntity> discussPosts = communityService
                .queryUserFavoritePosts(CommunityBO.builder()
                        .userId(requestDTO.getUserId())
                        .postId(requestDTO.getPostId())
                        .limit(requestDTO.getLimit())
                        .build());
        List<QueryFavoriteDiscussPostResponseDTO> responseDTOs = new ArrayList<>();
        for (DiscussPostEntity discussPost : discussPosts) {
            responseDTOs.add(QueryFavoriteDiscussPostResponseDTO.builder()
                    .discussPostId(discussPost.getDiscussPostId())
                    .userName(discussPost.getUserName())
                    .userAvatar(discussPost.getUserAvatar())
                    .title(discussPost.getTitle())
                    .content(discussPost.getContent())
                    .tag(discussPost.getTag())
                    .likeCount(discussPost.getLikeCount())
                    .commentCount(discussPost.getCommentCount())
                    .favoriteCount(discussPost.getFavoriteCount())
                    .createTime(discussPost.getCreateTime())
                    .updateTime(discussPost.getUpdateTime())
                    .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

    /**
     * 查询用户发布的讨论帖
     */
    @Override
    @GetMapping("/post/community")
    public Response<List<QueryMyDiscussPostResponse>> queryMyDiscussPost(QueryMyDiscussPostRequest requestDTO) {
        List<DiscussPostEntity> discussPosts = communityService
                .queryMyDiscussPosts(CommunityBO.builder()
                        .userId(requestDTO.getUserId())
                        .postId(requestDTO.getPostId())
                        .limit(requestDTO.getLimit())
                        .build());
        List<QueryMyDiscussPostResponse> responseDTOs = new ArrayList<>();
        for (DiscussPostEntity discussPost : discussPosts) {
            responseDTOs.add(QueryMyDiscussPostResponse.builder()
                    .discussPostId(discussPost.getDiscussPostId())
                    .userName(discussPost.getUserName())
                    .userAvatar(discussPost.getUserAvatar())
                    .title(discussPost.getTitle())
                    .content(discussPost.getContent())
                    .tag(discussPost.getTag())
                    .likeCount(discussPost.getLikeCount())
                    .commentCount(discussPost.getCommentCount())
                    .favoriteCount(discussPost.getFavoriteCount())
                    .createTime(discussPost.getCreateTime())
                    .updateTime(discussPost.getUpdateTime())
                    .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

    /**
     * 查询用户发布的交易帖
     */
    @Override
    @GetMapping("/post/trade")
    public Response<List<QueryMyTradePostResponse>> queryMyTradePost(QueryMyTradePostRequest requestDTO) {
        validateUserId(requestDTO.getUserId());
        List<TradePostEntity> discussPosts = bazaarService
                .queryMyTradePosts(BazaarBO.builder()
                        .userId(requestDTO.getUserId())
                        .postId(requestDTO.getPostId())
                        .limit(requestDTO.getLimit())
                        .status(requestDTO.getStatus())
                        .type(requestDTO.getType())
                        .build());
        List<QueryMyTradePostResponse> responseDTOs = new ArrayList<>();
        for (TradePostEntity post : discussPosts) {
            responseDTOs.add(QueryMyTradePostResponse.builder()
                    .tradePostId(post.getTradePostId())
                    .userId(post.getUserId())
                    .userName(post.getUserName())
                    .userAvatar(post.getUserAvatar())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .type(post.getType())
                    .price(post.getPrice())
                    .image(post.getImage())
                    .status(post.getStatus())
                    .createTime(post.getCreateTime())
                    .updateTime(post.getUpdateTime())
                    .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

    /**
     * 注销账号
     */
    @Override
    @DeleteMapping("/cancel")
    public Response cancelAccount(String userId) {
        log.info("用户{}请求注销账号", userId);
        userService.cancelAccount(userId);
        eventPublisher.publishEvent(new CancelAccountEvent(userId));
        return Response.SYSTEM_SUCCESS();
    }

    private void validateUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new AppException(400, "用户id不能为空!");
        }
    }

}
