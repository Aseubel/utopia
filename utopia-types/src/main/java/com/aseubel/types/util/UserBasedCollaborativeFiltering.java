package com.aseubel.types.util;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Aseubel
 * @date 2025/4/17 下午12:21
 */
@Setter
@Component
public class UserBasedCollaborativeFiltering {

    // 存储用户评分数据的结构：user_id -> (post_id, score)
    private Map<String, Map<String, Double>> userRatings = new HashMap<>();

    // 存储用户行为数据的结构：user_id -> (post_id, [likes, saves, comments])
    private Map<String, Map<String, int[]>> userBehaviors = new HashMap<>();

    private void clearRating() {
        userRatings.clear();
    }

    private void clearBehavior() {
        userBehaviors.clear();
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
     * 计算用户对文章的CES评分
     *
     * @return 文章ID及其CES评分的映射
     */
    public Map<String, Map<String, Double>> calculateCEScores() {
        Map<String, Map<String, Double>> cesScores = new HashMap<>();

        for (Map.Entry<String, Map<String, int[]>> entry : userBehaviors.entrySet()) {
            String userId = entry.getKey();
            Map<String, Double> userCesScores = new HashMap<>();
            for (Map.Entry<String, int[]> postEntry : entry.getValue().entrySet()) {
                String postId = postEntry.getKey();
                int[] behaviors = postEntry.getValue();

                int likes = behaviors[0];
                int saves = behaviors[1];
                int comments = behaviors[2];

                // CES评分公式：点赞 2 分 + 收藏 4 分 + 评论 2 分
                Double cesScore = likes * 2 + saves * 4 + comments * 2 * 1.0;
                userCesScores.put(postId, userCesScores.getOrDefault(postId, 0.0) + cesScore);
            }
            cesScores.put(userId, userCesScores);
        }

        return cesScores;
    }

    /**
     * 添加用户评分数据
     *
     * @param userId 用户ID
     * @param postId 文章ID
     * @param score  评分
     */
    public void addUserRating(String userId, String postId, double score) {
        userRatings.putIfAbsent(userId, new HashMap<>());
        userRatings.get(userId).put(postId, score);
    }

    /**
     * 计算两个用户之间的相似度（余弦相似度）
     *
     * @param user1 用户1的ID
     * @param user2 用户2的ID
     * @return 相似度分数（0到1之间）
     */
    private double computeSimilarity(String user1, String user2) {
        Map<String, Double> ratings1 = userRatings.get(user1);
        Map<String, Double> ratings2 = userRatings.get(user2);

        if (ratings1 == null || ratings2 == null) {
            return 0.0;
        }

        Set<String> commonPosts = new HashSet<>(ratings1.keySet());
        commonPosts.retainAll(ratings2.keySet());

        if (commonPosts.isEmpty()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String postId : commonPosts) {
            double score1 = ratings1.get(postId);
            double score2 = ratings2.get(postId);
            dotProduct += score1 * score2;
            norm1 += score1 * score1;
            norm2 += score2 * score2;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * 找到与目标用户最相似的K个邻居
     *
     * @param userId 目标用户ID
     * @param k      需要找到的邻居数量
     * @return 按相似度排序的邻居列表
     */
    private List<Map.Entry<String, Double>> findNeighbors(String userId, int k) {
        List<Map.Entry<String, Double>> neighbors = new ArrayList<>();

        for (Map.Entry<String, Map<String, Double>> entry : userRatings.entrySet()) {
            String neighborId = entry.getKey();
            if (!neighborId.equals(userId)) {
                double similarity = computeSimilarity(userId, neighborId);
                neighbors.add(new AbstractMap.SimpleEntry<>(neighborId, similarity));
            }
        }

        // 按相似度降序排序
        neighbors.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // 只保留前K个邻居
        if (neighbors.size() > k) {
            neighbors = neighbors.subList(0, k);
        }

        return neighbors;
    }

    /**
     * 为用户生成推荐
     *
     * @param userId 目标用户ID
     * @param k      邻居数量
     * @param n      推荐数量
     * @return 推荐的post_id列表
     */
    public List<String> generateRecommendations(String userId, int k, int n) {
        List<Map.Entry<String, Double>> neighbors = findNeighbors(userId, k);
        if (neighbors.isEmpty()) {
            return Collections.emptyList();
        }

        // 收集邻居评分的post
        Map<String, Double> recommendations = new HashMap<>();
        for (Map.Entry<String, Double> entry : neighbors) {
            String neighborId = entry.getKey();
            double similarity = entry.getValue();
            Map<String, Double> neighborRatings = userRatings.get(neighborId);

            for (Map.Entry<String, Double> postEntry : neighborRatings.entrySet()) {
                String postId = postEntry.getKey();
                double score = postEntry.getValue();

                // 如果目标用户已经评分过这个post，则跳过，即不推荐用户已经有行为的post
                if (userRatings.get(userId).containsKey(postId)) {
                    continue;
                }

                // 累加加权评分
                recommendations.put(postId, recommendations.getOrDefault(postId, 0.0) + score * similarity);
            }
        }

        // 按加权评分排序
        List<Map.Entry<String, Double>> sortedRecommendations = new ArrayList<>(recommendations.entrySet());
        sortedRecommendations.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // 返回前n个推荐
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(n, sortedRecommendations.size()); i++) {
            result.add(sortedRecommendations.get(i).getKey());
        }

        return result;
    }
//
    public static void main(String[] args) {
        // 示例数据
        UserBasedCollaborativeFiltering cf = new UserBasedCollaborativeFiltering();
        cf.addUserRating("1", "101", 5.0);
        cf.addUserRating("1", "102", 3.0);
        cf.addUserRating("1", "103", 4.0);
        cf.addUserRating("2", "101", 4.0);
        cf.addUserRating("2", "102", 5.0);
        cf.addUserRating("2", "104", 3.0);
        cf.addUserRating("3", "103", 5.0);
        cf.addUserRating("3", "104", 4.0);
        cf.addUserRating("3", "105", 5.0);

        // 为用户1生成推荐
        List<String> recommendations = cf.generateRecommendations("1", 2, 3);
        System.out.println("Recommendations for user 1: " + recommendations);
    }
}
