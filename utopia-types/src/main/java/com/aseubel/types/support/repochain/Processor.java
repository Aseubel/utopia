package com.aseubel.types.support.repochain;

/**
 * @author Aseubel
 * @description 处理器接口
 * @date 2025/4/28 上午12:41
 */
public interface Processor<T> {
    Result<T> process(T data, int index, ProcessorChain<T> chain);
}
