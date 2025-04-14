package com.aseubel.domain.community.adapter.repo;

/**
 * @author Aseubel
 * @date 2025/4/15 上午2:18
 */
public interface IAICommunityRepository {

    /**
     * 图片审核
     * @param imgUrl 图片url
     * @param userId 用户id
     * @return 审核结果 true合规 false不合规
     */
    boolean ImgCensor(String imgUrl, String userId);

    /**
     * 图片审核
     * @param imgUrl 图片url
     * @return 审核结果 true合规 false不合规
     */
    boolean ImgCensor(String imgUrl);

    /**
     * 文本审核
     * @param text 文本
     * @param userId 用户id
     * @return 审核结果 true合规 false不合规
     */
    boolean TextCensor(String text, String userId);

    /**
     * 文本审核
     * @param text 文本
     * @return 审核结果 true合规 false不合规
     */
    boolean TextCensor(String text);
}
