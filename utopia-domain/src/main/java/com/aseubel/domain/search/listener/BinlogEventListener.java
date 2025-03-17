package com.aseubel.domain.search.listener;

import cn.hutool.core.util.ObjectUtil;
import com.aseubel.domain.search.adapter.repo.ISearchRepository;
import com.aseubel.domain.search.model.DiscussPostVO;
import com.aseubel.domain.search.model.FileVO;
import com.aseubel.domain.search.model.TradePostVO;
import com.aseubel.types.common.Constant;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

import static com.aseubel.types.common.Constant.FILE_SEARCH_INDEX;

/**
 * @author Aseubel
 * @date 2025-03-17 11:51
 */
@Component
public class BinlogEventListener implements CommandLineRunner {

    @Resource
    private Client meilisearchClient;

    @Resource
    private BinaryLogClient binaryLogClient;

    @Resource
    private ISearchRepository searchRepository;

    private static String tableName;

    @Override
    public void run(String... args) throws Exception {
        binaryLogClient.registerEventListener(this::handleEvent);
        binaryLogClient.connect();
    }

    private void handleEvent(Event event) {
        EventData data = event.getData();
        if (data instanceof TableMapEventData) {
            tableName = ((TableMapEventData) data).getTable();
            return;
        }
        if (!(data instanceof WriteRowsEventData || data instanceof UpdateRowsEventData || data instanceof DeleteRowsEventData)) {
            return;
        }
        switch (tableName) {
            case "discuss_post":
                handleDiscussPostEvent(data);
                break;
            case "trade_post":
                handleTradePostEvent(data);
                break;
            case "sfile":
                handleFileEvent(data);
                break;
        }
    }

    private void handleDiscussPostEvent(EventData data) {
//        if (data instanceof WriteRowsEventData) {
//            for (Serializable[] row : ((WriteRowsEventData) data).getRows()) {
//                DiscussPostVO post = DiscussPostVO.parseFrom(row);
//                if (ObjectUtil.isNotEmpty(post)) {
//                    post.setImage(searchRepository.getImageUrlByImageId(post.getImage()));
//                    meilisearchClient.index(Constant.getDiscussPostSearchIndex(post.getSchoolCode()))
//                            .addDocuments(post.toJsonString());
//                }
//            }
//        } else
        if (data instanceof UpdateRowsEventData) {
            for (Map.Entry<Serializable[], Serializable[]> row : ((UpdateRowsEventData) data).getRows()) {
                DiscussPostVO post = DiscussPostVO.parseFrom(row.getValue());
                if (ObjectUtil.isEmpty(post) || post.getIsDeleted() == 1) {
                    meilisearchClient.index(Constant.getDiscussPostSearchIndex(post.getSchoolCode()))
                            .deleteDocument(post.getPostId());
                } else {
                    meilisearchClient.index(Constant.getDiscussPostSearchIndex(post.getSchoolCode()))
                            .updateDocuments(post.toJsonStringWithoutImage());
                }
            }
        }
    }

    private void handleTradePostEvent(EventData data) {
//        if (data instanceof WriteRowsEventData) {
//            for (Serializable[] row : ((WriteRowsEventData) data).getRows()) {
//                TradePostVO post = TradePostVO.parseFrom(row);
//                if (ObjectUtil.isNotEmpty(post)) {
//                    post.setImage(searchRepository.getImageUrlByImageId(post.getImage()));
//                    meilisearchClient.index(Constant.getTradePostSearchIndex(post.getSchoolCode()))
//                            .addDocuments(post.toJsonString());
//                }
//            }
//        } else
        if (data instanceof UpdateRowsEventData) {
            for (Map.Entry<Serializable[], Serializable[]> row : ((UpdateRowsEventData) data).getRows()) {
                TradePostVO post = TradePostVO.parseFrom(row.getValue());
                if (ObjectUtil.isEmpty(post) || post.getIsDeleted() == 1 || post.getStatus() == 1) {
                    meilisearchClient.index(Constant.getTradePostSearchIndex(post.getSchoolCode()))
                            .deleteDocument(post.getPostId());
                } else {
                    meilisearchClient.index(Constant.getTradePostSearchIndex(post.getSchoolCode()))
                            .updateDocuments(post.toJsonStringWithoutImage());
                }
            }
        }
    }

    private void handleFileEvent(EventData data) {
        if (data instanceof WriteRowsEventData) {
            for (Serializable[] row : ((WriteRowsEventData) data).getRows()) {
                FileVO file = FileVO.parseFrom(row);
                if (ObjectUtil.isNotEmpty(file)) {
                    meilisearchClient.index(FILE_SEARCH_INDEX)
                            .addDocuments(file.toJsonString(), "fileId");
                }
            }
        } else if (data instanceof UpdateRowsEventData) {
            for (Map.Entry<Serializable[], Serializable[]> row : ((UpdateRowsEventData) data).getRows()) {
                FileVO file = FileVO.parseFrom(row.getValue());
                if (ObjectUtil.isEmpty(file) || file.getIsDeleted() == 1) {
                    meilisearchClient.index(FILE_SEARCH_INDEX)
                            .deleteDocument(file.getFileId());
                } else {
                    meilisearchClient.index(FILE_SEARCH_INDEX)
                            .updateDocuments(file.toJsonString());
                }
            }
        }
    }

}

