package com.aseubel.domain.community.adapter.repo;

import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.NoticeEntity;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-03-18 20:40
 */
public interface INoticeRepository {

    /**
     * 获取通知列表
     * @param communityBO
     * @return
     */
    List<NoticeEntity> listNotices(CommunityBO communityBO);

    /**
     * 添加通知
     * @param bo
     * @param type
     */
    void addNotice(CommunityBO bo, int type);

    /**
     * 标记通知已读
     * @param communityBO
     */
    void readNotice(CommunityBO communityBO);

    /**
     * 删除通知
     * @param communityBO
     */
    void deleteNotice(CommunityBO communityBO);
}
