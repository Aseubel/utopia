package com.aseubel.types.util;

import java.util.HashMap;
import java.util.Map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Aseubel
 * @date 2025/4/17 下午2:06
 */
public class CESRecommender {

    // 存储用户行为数据的结构：user_id -> (post_id, [likes, saves, comments])
    private Map<String, Map<String, int[]>> userBehaviors;

    public CESRecommender() {
        userBehaviors = new ConcurrentHashMap<>();
    }

    /**
     * 添加用户行为数据
     *
     * @param userId   用户ID
     * @param postId   文章ID
     * @param likes    点赞次数
     * @param saves    收藏次数
     * @param comments 评论次数
     */
    public void addUserBehavior(String userId, String postId, int likes, int saves, int comments) {
        userBehaviors.putIfAbsent(userId, new HashMap<>());
        userBehaviors.get(userId).put(postId, new int[]{likes, saves, comments});
    }

    /**
     * 计算文章的CES评分
     *
     * @return 文章ID及其CES评分的映射
     */
    private Map<String, Integer> calculateCEScores() {
        Map<String, Integer> cesScores = new HashMap<>();

        for (Map.Entry<String, Map<String, int[]>> entry : userBehaviors.entrySet()) {
            for (Map.Entry<String, int[]> postEntry : entry.getValue().entrySet()) {
                String postId = postEntry.getKey();
                int[] behaviors = postEntry.getValue();

                int likes = behaviors[0];
                int saves = behaviors[1];
                int comments = behaviors[2];

                // CES评分公式：点赞 2 分 + 收藏 4 分 + 评论 2 分
                int cesScore = likes * 2 + saves * 4 + comments * 2;
                cesScores.put(postId, cesScores.getOrDefault(postId, 0) + cesScore);
            }
        }

        return cesScores;
    }

    /**
     * 生成推荐
     *
     * @param n 推荐数量
     * @return 推荐的post_id列表
     */
    public List<String> generateRecommendations(int n) {
        Map<String, Integer> cesScores = calculateCEScores();

        // 按CES评分降序排序
        List<Map.Entry<String, Integer>> sortedPosts = new ArrayList<>(cesScores.entrySet());
        sortedPosts.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // 返回前n个推荐
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(n, sortedPosts.size()); i++) {
            result.add(sortedPosts.get(i).getKey());
        }

        return result;
    }

    public static void main(String[] args) {
        // 示例数据
        CESRecommender ces = new CESRecommender();
        ces.addUserBehavior("1", "101", 5, 3, 2);
        ces.addUserBehavior("1", "102", 3, 2, 1);
        ces.addUserBehavior("2", "101", 4, 5, 3);
        ces.addUserBehavior("2", "103", 2, 1, 4);
        ces.addUserBehavior("3", "103", 5, 4, 2);
        ces.addUserBehavior("3", "104", 3, 3, 5);

        // 生成推荐
        List<String> recommendations = ces.generateRecommendations(3);
        System.out.println("CES Recommendations: " + recommendations);
    }
}
