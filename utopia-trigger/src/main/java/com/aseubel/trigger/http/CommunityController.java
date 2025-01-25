package com.aseubel.trigger.http;

import com.aseubel.api.CommunityInterface;
import com.aseubel.api.dto.community.QueryIndexDiscussPostRequestDTO;
import com.aseubel.api.dto.community.QueryIndexDiscussPostResponseDTO;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.community.service.ICommunityService;
import com.aseubel.types.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/community/") //${app.config.api-version}
@RequiredArgsConstructor
public class CommunityController implements CommunityInterface {

    private final ICommunityService communityService;

    /**
     * 查询首页帖子列表
     */
    @Override
    @GetMapping("/post")
    public Response<List<QueryIndexDiscussPostResponseDTO>> queryIndexDiscussPost(@Valid QueryIndexDiscussPostRequestDTO requestDTO) {
        List<DiscussPostEntity> discussPosts = communityService.listDiscussPost(requestDTO.getPostId(), requestDTO.getLimit());
        List<QueryIndexDiscussPostResponseDTO> responseDTOs = new ArrayList<>();
        for (DiscussPostEntity discussPost : discussPosts) {
           responseDTOs.add(QueryIndexDiscussPostResponseDTO.builder()
                   .discussPostId(discussPost.getDiscussPostId())
                   .userId(discussPost.getUserId())
                   .userName(discussPost.getUserName())
                   .userAvatar(discussPost.getUserAvatar())
                   .title(discussPost.getTitle())
                   .content(discussPost.getContent())
                   .likeCount(discussPost.getLikeCount())
                   .commentCount(discussPost.getCommentCount())
                   .forwardCount(discussPost.getForwardCount())
                   .type(discussPost.getType())
                   .status(discussPost.getStatus())
                   .createTime(discussPost.getCreateTime())
                   .updateTime(discussPost.getUpdateTime())
                   .images(discussPost.getImages())
                   .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

}
