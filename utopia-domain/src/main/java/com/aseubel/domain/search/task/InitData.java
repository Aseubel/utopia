package com.aseubel.domain.search.task;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.domain.search.adapter.repo.ISearchDiscussPostRepository;
import com.aseubel.domain.search.adapter.repo.ISearchFileRepository;
import com.aseubel.domain.search.adapter.repo.ISearchTradePostRepository;
import com.aseubel.domain.search.model.DiscussPostVO;
import com.aseubel.domain.search.model.FileVO;
import com.aseubel.domain.search.model.TradePostVO;
import com.aseubel.types.common.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.aseubel.types.common.Constant.*;

@Component
public class InitData {

    @Resource
    private Client client;

    @Resource
    private ISearchDiscussPostRepository dpRepository;

    @Resource
    private ISearchTradePostRepository tpRepository;

    @Resource
    private ISearchFileRepository fileRepository;

    @Resource
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws JsonProcessingException {
        // 清空索引
        Index[] results = client.getIndexes().getResults();
        for (Index index : results) {
            client.deleteIndex(index.getUid());
        }

        // 讨论贴
        int pageSize = 500;
        Long lastPostId = null;
        Set<String> dpIndexSet = new HashSet<>();
        while (true) {
            String jsonArray = dpRepository.listDiscussPostStatistics(lastPostId, pageSize);
            List<DiscussPostVO> batch = objectMapper.readValue(
                    jsonArray,
                    objectMapper.getTypeFactory().constructCollectionType(List.class,DiscussPostVO.class)
            );
            if (CollectionUtil.isEmpty(batch)) break;
            List<String> discussPostJsons = batch.stream().map(DiscussPostVO::toJsonString).toList();
            // 处理本批数据
            for (int i = 0; i < discussPostJsons.size(); i++) {
                String index = Constant.getDiscussPostSearchIndex(batch.get(i).getSchoolCode());
                dpIndexSet.add(index);
                client.index(index).addDocuments(discussPostJsons.get(i), "postId");
            }
            lastPostId = batch.get(batch.size()-1).getId();
        }
        for (String index : dpIndexSet) {
            client.index(index).updateSortableAttributesSettings(SEARCH_SORT_SETTINGS);
        }

        // 交易贴
        Set<String> tpIndexSet = new HashSet<>();
        lastPostId = null;
        while (true) {
            String jsonArray = tpRepository.listTradePostStatistics(lastPostId, pageSize);
            List<TradePostVO> batch = objectMapper.readValue(
                    jsonArray,
                    objectMapper.getTypeFactory().constructCollectionType(List.class,TradePostVO.class)
            );
            if (CollectionUtil.isEmpty(batch)) break;
            List<String> tradePostJsons = batch.stream().map(TradePostVO::toJsonString).toList();
            for (int i = 0; i < tradePostJsons.size(); i++) {
                String index = Constant.getTradePostSearchIndex(batch.get(i).getSchoolCode());
                tpIndexSet.add(index);
                client.index(index).addDocuments(tradePostJsons.get(i), "postId");
            }
            lastPostId = batch.get(batch.size()-1).getId();
        }
        for (String index : tpIndexSet) {
            client.index(index).updateSortableAttributesSettings(SEARCH_SORT_SETTINGS);
        }

        // 文件
        Set<String> fileIndexSet = new HashSet<>();
        long lastFileId = 0;
        while (true) {
            String jsonArray = fileRepository.listFileStatistics(lastFileId, pageSize);
            List<FileVO> batch = objectMapper.readValue(
                    jsonArray,
                    objectMapper.getTypeFactory().constructCollectionType(List.class,FileVO.class)
            );
            if (CollectionUtil.isEmpty(batch)) break;
            List<String> fileJsons = batch.stream().map(FileVO::toJsonString).toList();
            for (String fileJson : fileJsons) {
                String index = FILE_SEARCH_INDEX;
                fileIndexSet.add(index);
                client.index(index).addDocuments(fileJson, "fileId");
            }
            lastFileId = batch.get(batch.size()-1).getId();
        }
        for (String index : fileIndexSet) {
            client.index(index).updateSortableAttributesSettings(FILE_SEARCH_SORT_SETTINGS);
        }
    }

}
