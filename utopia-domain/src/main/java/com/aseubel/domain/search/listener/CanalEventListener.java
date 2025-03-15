package com.aseubel.domain.search.listener;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.aseubel.domain.search.model.DiscussPostVO;
import com.aseubel.domain.search.model.FileVO;
import com.aseubel.domain.search.model.TradePostVO;
import com.aseubel.types.common.Constant;
import com.google.protobuf.InvalidProtocolBufferException;
import com.meilisearch.sdk.Client;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aseubel.types.common.Constant.FILE_SEARCH_INDEX;

@Component
public class CanalEventListener implements CommandLineRunner {

    @Autowired
    private CanalConnector canalConnector;

    @Resource
    private Client meilisearchClient;

    @Value("${canal.client.batch-size:100}")
    private int batchSize = 100;

    @Value("${canal.client.interval:1000}")
    private long interval = 1000;

    @Override
    public void run(String... args) throws Exception {
        canalConnector.connect();
        canalConnector.subscribe(".*\\..*");
        canalConnector.rollback();

        while (true) {
            Message message = canalConnector.getWithoutAck(
                    batchSize,
                    interval,
                    java.util.concurrent.TimeUnit.MILLISECONDS
            );

            long batchId = message.getId();
            if (batchId != -1) {
                processEntry(message.getEntries());
            }
            canalConnector.ack(batchId);
        }
    }

    private void processEntry(List<CanalEntry.Entry> entries) throws InvalidProtocolBufferException {
        // 这里添加具体的数据处理逻辑（同之前示例中的processEntry方法）
        System.out.println("processEntry");
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                switch (entry.getHeader().getTableName()) {
                    case "discuss_post":
                        handleDiscussPost(rowChange);
                        break;
                    case "trade_post":
                        handleTradePost(rowChange);
                        break;
                    case "sfile":
                        handleFile(rowChange);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void handleDiscussPost(CanalEntry.RowChange rowChange) {
        CanalEntry.EventType eventType = rowChange.getEventType();
        if (eventType == CanalEntry.EventType.INSERT) {
            handlePublishDiscussPost(rowChange.getRowDatasList());
        } else if (eventType == CanalEntry.EventType.UPDATE) {
            handleUpdateDiscussPost(rowChange.getRowDatasList());
        } else if (eventType == CanalEntry.EventType.DELETE) {
            System.out.println("delete");
        }
    }

    private void handleTradePost(CanalEntry.RowChange rowChange) {
        CanalEntry.EventType eventType = rowChange.getEventType();
        if (eventType == CanalEntry.EventType.INSERT) {
            handlePublishTradePost(rowChange.getRowDatasList());
        } else if (eventType == CanalEntry.EventType.UPDATE) {
            handleUpdateTradePost(rowChange.getRowDatasList());
        } else if (eventType == CanalEntry.EventType.DELETE) {
            System.out.println("delete");
        }
    }

    private void handleFile(CanalEntry.RowChange rowChange) {
        CanalEntry.EventType eventType = rowChange.getEventType();
        if (eventType == CanalEntry.EventType.INSERT) {
            handleUploadFile(rowChange.getRowDatasList());
        } else if (eventType == CanalEntry.EventType.UPDATE) {
            handleUpdateFile(rowChange.getRowDatasList());
        } else if (eventType == CanalEntry.EventType.DELETE) {
            System.out.println("delete");
        }
    }

    private void handlePublishDiscussPost(List<CanalEntry.RowData> rowDatas) {
        for (CanalEntry.RowData rowData : rowDatas) {
            DiscussPostVO post = DiscussPostVO.parseFrom(rowData.getAfterColumnsList());
            meilisearchClient.index(Constant.getDiscussPostSearchIndex(post.getSchoolCode())).addDocuments(post.toJsonString());
        }
    }

    private void handleUpdateDiscussPost(List<CanalEntry.RowData> rowDatas) {
        for (CanalEntry.RowData rowData : rowDatas) {
            DiscussPostVO post = DiscussPostVO.parseFrom(rowData.getAfterColumnsList());
            if (ObjectUtil.isEmpty(post) || post.getIsDeleted() == 1) {
                meilisearchClient.index(Constant.getDiscussPostSearchIndex(post.getSchoolCode())).deleteDocument(post.getPostId());
            } else {
                meilisearchClient.index(Constant.getDiscussPostSearchIndex(post.getSchoolCode())).updateDocuments(post.toJsonString(), post.getPostId());
            }
        }
    }

    private void handlePublishTradePost(List<CanalEntry.RowData> rowDatas) {
        for (CanalEntry.RowData rowData : rowDatas) {
            TradePostVO post = TradePostVO.parseFrom(rowData.getAfterColumnsList());
            meilisearchClient.index(Constant.getTradePostSearchIndex(post.getSchoolCode())).addDocuments(post.toJsonString());
        }
    }

    private void handleUpdateTradePost(List<CanalEntry.RowData> rowDatas) {
        for (CanalEntry.RowData rowData : rowDatas) {
            TradePostVO post = TradePostVO.parseFrom(rowData.getAfterColumnsList());
            if (ObjectUtil.isEmpty(post) || post.getIsDeleted() == 1 || post.getStatus() == 1) {
                meilisearchClient.index(Constant.getTradePostSearchIndex(post.getSchoolCode())).deleteDocument(post.getPostId());
            } else {
                meilisearchClient.index(Constant.getTradePostSearchIndex(post.getSchoolCode())).updateDocuments(post.toJsonString(), post.getPostId());
            }
        }
    }

    private void handleUploadFile(List<CanalEntry.RowData> rowDatas) {
        for (CanalEntry.RowData rowData : rowDatas) {
            FileVO file = FileVO.parseFrom(rowData.getAfterColumnsList());
            meilisearchClient.index(FILE_SEARCH_INDEX).addDocuments(file.toJsonString(), "fileId");
        }
    }

    private void handleUpdateFile(List<CanalEntry.RowData> rowDatas) {
        for (CanalEntry.RowData rowData : rowDatas) {
            FileVO file = FileVO.parseFrom(rowData.getAfterColumnsList());
            if (ObjectUtil.isEmpty(file) || file.getIsDeleted() == 1) {
                meilisearchClient.index(FILE_SEARCH_INDEX).deleteDocument(file.getFileId());
            } else {
                meilisearchClient.index(FILE_SEARCH_INDEX).updateDocuments(file.toJsonString(), file.getFileId());
            }
        }
    }
}
