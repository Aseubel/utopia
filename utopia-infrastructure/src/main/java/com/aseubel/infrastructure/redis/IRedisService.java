package com.aseubel.infrastructure.redis;

import org.redisson.api.*;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 服务
 */
public interface IRedisService {

    /**
     * 执行 Lua 脚本
     * @param script 脚本
     * @param returnType 返回类型
     * @param keys 键
     * @param values 值
     */
    <T> T executeScript(String script, RScript.ReturnType returnType, List<Object> keys, Object... values);

    /**
     * 设置指定 key 的值
     *
     * @param key   键
     * @param value 值
     */
    <T> void setValue(String key, T value);

    /**
     * 设置指定 key 的值
     *
     * @param key     键
     * @param value   值
     * @param expired 过期时间
     */
    <T> void setValue(String key, T value, long expired);

    /**
     * 获取指定 key 的值
     *
     * @param key 键
     * @return 值
     */
    <T> T getValue(String key);

    /**
     * 获取队列
     *
     * @param key 键
     * @param <T> 泛型
     * @return 队列
     */
    <T> RQueue<T> getQueue(String key);

    /**
     * 加锁队列
     *
     * @param key 键
     * @param <T> 泛型
     * @return 队列
     */
    <T> RBlockingQueue<T> getBlockingQueue(String key);

    /**
     * 延迟队列
     *
     * @param rBlockingQueue 加锁队列
     * @param <T>            泛型
     * @return 队列
     */
    <T> RDelayedQueue<T> getDelayedQueue(RBlockingQueue<T> rBlockingQueue);

    /**
     * 设置值
     *
     * @param key   key 键
     * @param value 值
     */
    void setAtomicLong(String key, long value);

    /**
     * 获取值
     *
     * @param key key 键
     */
    Long getAtomicLong(String key);

    /**
     * 自增 Key 的值；1、2、3、4
     *
     * @param key 键
     * @return 自增后的值
     */
    long incr(String key);

    /**
     * 指定值，自增 Key 的值；1、2、3、4
     *
     * @param key 键
     * @return 自增后的值
     */
    long incrBy(String key, long delta);

    /**
     * 自减 Key 的值；1、2、3、4
     *
     * @param key 键
     * @return 自增后的值
     */
    long decr(String key);

    /**
     * 指定值，自增 Key 的值；1、2、3、4
     *
     * @param key 键
     * @return 自增后的值
     */
    long decrBy(String key, long delta);


    /**
     * 移除指定 key 的值
     *
     * @param key 键
     */
    void remove(String key);

    /**
     * 判断指定 key 的值是否存在
     *
     * @param key 键
     * @return true/false
     */
    boolean isExists(String key);

    /**
     * 将指定的值添加到集合中
     *
     * @param key   键
     * @param value 值
     */
    void addToSet(String key, String value);

    /**
     * 删除集合中的指定值
     * @param key
     * @param value
     */
    void removeFromSet(String key, String value);

    /**
     * 获取集合中的所有值
     *
     * @param key 键
     * @return 值
     */
    Set<String> getSetMembers(String key);

    /**
     * 判断指定的值是否是集合的成员
     *
     * @param key   键
     * @param value 值
     * @return 如果是集合的成员返回 true，否则返回 false
     */
    boolean isSetMember(String key, String value);

    /**
     * 设置set的过期时间
     *
     */
    void setSetExpired(String key, long expired);

    /**
     * 将指定的值添加到列表中
     *
     * @param key   键
     * @param value 值
     */
    <T> void addToList(String key, T value);

    /**
     * 获取列表中的所有值
     */
    <T> List<T> getListValuesAndRemove(String key);

    /**
     * 设置列表的过期时间
     */
    void setListExpired(String key, Duration expired);

    /**
     * 获取列表中指定索引的值
     *
     * @param key   键
     * @param index 索引
     * @return 值
     */
    String getFromList(String key, int index);

    /**
     * 获取Map
     *
     * @param key 键
     * @return 值
     */
    <K, V> RMap<K, V> getMap(String key);

    /**
     * 将指定的键值对添加到哈希表中
     *
     * @param key   键
     * @param field 字段
     * @param value 值
     */
    <T> void addToMap(String key, String field, T value);

    /**
     * 将指定的键值对添加到哈希表中
     *
     * @param key   键
     * @param field 字段
     * @param value 值
     */
    void addToMap(String key, String field, String value);

    /**
     * 设置指定 key 的过期时间
     *
     * @param key     键
     * @param expired 过期时间
     */
    void setMapExpired(String key, long expired);


    /**
     * 获取Map的过期时间
     *
     * @param key 键
     * @return 值
     */
    Long getMapExpired(String key);

//    /**
//     * 获取Map并转换为Java Map
//     * @param key 键
//     * @return 值
//     */
//    Map<String,String> getMapToJavaMap(String key);

    /**
     * 获取Map并转换为Java Map
     * @param key 键
     * @return 值
     */
    <T> Map<String,T> getMapToJavaMap(String key);

    /**
     * 移除哈希表中指定字段的值
     *
     * @param key   键
     * @param field 字段
     */
    void removeFromMap(String key, String field);

    /**
     * 增加哈希表中指定字段的值
     * @param key   键
     * @param field 字段
     * @param delta 增量
     * @return      增量后的值
     */
    Integer incrMapValue(String key, String field, Integer delta);

    /**
     * 获取哈希表中指定字段的值
     *
     * @param key   键
     * @param field 字段
     * @return 值
     */
    <K, V> V getFromMap(String key, K field);

    /**
     * 将指定的值添加到有序集合中
     *
     * @param key   键
     * @param value 值
     */
    void addToSortedSet(String key, String value);

    /**
     * 将指定的值添加到有序集合中
     * @param key   键
     * @param value 值
     * @param score 分数
     */
    <V> void addToSortedSet(String key, V value, double score);

    /**
     * 获取有序集合中指定索引的值
     * @param key   键
     * @param value 上一页最后的值
     * @param limit 限制数量
     * @return      entry
     */
    <V> Collection<V> getFromSortedSet(String key, V value, int limit);

    /**
     * 获取有序集合中指定索引的值
     * @param key   键
     * @param value 上一页最后的值
     * @param limit 限制数量
     * @return 值
     */
    <V> Collection<V> getReverseFromSortedSet(String key, V value, int limit);

    /**
     * 增加有序集合中指定值的分数
     * @param key   键
     * @param value 值
     * @param delta 分数增量
     */
    <T> void incrSortedSetScore(String key, T value, double delta);

    /**
     * 减少有序集合中指定值的分数
     * @param key   键
     * @param value 值
     * @param delta 分数减量
     */
    <T> void decrSortedSetScore(String key, T value, double delta);

    /**
     * 获取有序集合中指定值的分数
     * @param key   键
     * @param value 值
     * @return      分数
     */
    <T> Double getScoreFromSortedSet(String key, T value);

    /**
     * 移除有序集合中指定的值
     * @param key   键
     * @param value 值
     */
    <T> void RemoveFromSortedSet(String key, T value);

    /**
     * 获取 Redis 锁（可重入锁）
     *
     * @param key 键
     * @return Lock
     */
    RLock getLock(String key);

    /**
     * 释放 Redis 锁
     *
     * @param key 键
     */
    void unLock(String key);

    /**
     * 获取 Redis 锁（公平锁）
     *
     * @param key 键
     * @return Lock
     */
    RLock getFairLock(String key);

    /**
     * 获取 Redis 锁（读写锁）
     *
     * @param key 键
     * @return RReadWriteLock
     */
    RReadWriteLock getReadWriteLock(String key);

    /**
     * 获取 Redis 信号量
     *
     * @param key 键
     * @return RSemaphore
     */
    RSemaphore getSemaphore(String key);

    /**
     * 获取 Redis 过期信号量
     * <p>
     * 基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。
     * 同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
     *
     * @param key 键
     * @return RPermitExpirableSemaphore
     */
    RPermitExpirableSemaphore getPermitExpirableSemaphore(String key);

    /**
     * 闭锁
     *
     * @param key 键
     * @return RCountDownLatch
     */
    RCountDownLatch getCountDownLatch(String key);

    /**
     * 布隆过滤器
     *
     * @param key 键
     * @param <T> 存放对象
     * @return 返回结果
     */
    <T> RBloomFilter<T> getBloomFilter(String key);

    Boolean setNx(String key);

    Boolean setNx(String key, long expired, TimeUnit timeUnit);

}
