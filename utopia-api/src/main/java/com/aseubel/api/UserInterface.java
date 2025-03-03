package com.aseubel.api;

import com.aseubel.api.dto.user.UploadAvatarRequestDTO;
import com.aseubel.api.dto.user.UploadAvatarResponseDTO;
import com.aseubel.api.dto.user.*;
import com.aseubel.api.dto.user.post.*;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import java.util.List;

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
     * 用户登出
     * @param logoutRequestDTO
     * @return
     */
    Response logout(@Valid @RequestBody LogoutRequestDTO logoutRequestDTO);

    /**
     * 刷新token
     * @param refreshTokenRequestDTO 刷新token请求DTO，包含refreshToken和userId
     * @return 新的refresh_token和access_token
     */
    Response<RefreshTokenResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO);

    /**
     * 查询用户信息
     * @param queryUserInfoRequestDTO 查询用户信息请求DTO，仅包含用户openid
     * @return 查询用户信息响应DTO
     */
    Response<QueryUserInfoResponseDTO> queryUserInfo(@Valid QueryUserInfoRequestDTO queryUserInfoRequestDTO);

    /**
     * 查询其他用户信息
     * @param queryOtherInfoRequest 查询其他用户信息请求DTO，包含用户id和目标用户id
     * @return 目标用户信息
     */
    Response<QueryOtherInfoResponse> queryOtherInfo(QueryOtherInfoRequest queryOtherInfoRequest);

    /**
     * 修改用户个人信息接口
     * @param modifyUserInfoRequestDTO 修改用户个人信息请求DTO
     * @return 修改结果
     */
    Response modifyUserInfo(@Valid @RequestBody ModifyUserInfoRequestDTO modifyUserInfoRequestDTO);

    /**
     * 上传头像接口
     * @param uploadAvatarRequestDTO 用户id和头像文件
     * @return 返回用户头像url，头像id，用户id
     */
    Response<UploadAvatarResponseDTO> uploadAvatar(@Valid @ModelAttribute UploadAvatarRequestDTO uploadAvatarRequestDTO);

    /**
     * 查询收藏帖子列表
     * @param queryFavoriteDiscussPostRequestDTO
     * @return
     */
    Response<List<QueryFavoriteDiscussPostResponseDTO>> queryFavoriteDiscussPost(QueryFavoriteDiscussPostRequestDTO queryFavoriteDiscussPostRequestDTO);

    /**
     * 查询我的讨论帖子列表
     * @param queryMyDiscussPostRequest
     * @return
     */
    Response<List<QueryMyDiscussPostResponse>> queryMyDiscussPost(QueryMyDiscussPostRequest queryMyDiscussPostRequest);

    /**
     * 查询我的交易帖子列表
     * @param queryMyTradePostRequest
     * @return
     */
    Response<List<QueryMyTradePostResponse>> queryMyTradePost(QueryMyTradePostRequest queryMyTradePostRequest);

    /**
     * 注销账号接口
     * 用户未完成的交易帖将被删除，讨论帖和评论会保留，但不显示该用户信息
     */
    Response cancelAccount(String userId);
}
