package com.aseubel.api;

import com.aseubel.api.dto.community.QueryIndexDiscussPostRequestDTO;
import com.aseubel.api.dto.community.QueryIndexDiscussPostResponseDTO;
import com.aseubel.types.Response;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aseubel
 * @description 社区请求接口
 * @date 2025-01-23 19:23
 */
public interface CommunityInterface {

    Response<List<QueryIndexDiscussPostResponseDTO>> queryIndexDiscussPost(@Valid QueryIndexDiscussPostRequestDTO requestDTO);

}
