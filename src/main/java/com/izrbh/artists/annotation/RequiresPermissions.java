package com.izrbh.artists.annotation;

import java.lang.annotation.*;

/**
 * 方法权限拦截注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {

    String[] value() default {};

}