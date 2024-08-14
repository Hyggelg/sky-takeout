package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @description:    自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 * @author: liangguang
 * @date: 2024/8/11 0011 16:13
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //数据库操作类型：update insert
    OperationType value();
}
