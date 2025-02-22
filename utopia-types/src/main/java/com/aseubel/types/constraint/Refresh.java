package com.aseubel.types.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aseubel
 * @description 刷新 token aop注解
 * @date 2025-02-22 11:18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Refresh {
    String value() default "";
}
