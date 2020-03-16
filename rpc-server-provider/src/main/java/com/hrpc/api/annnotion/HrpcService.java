package com.hrpc.api.annnotion;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface HrpcService {
    /**
     * 获取服务的接口
     * @return
     */
    Class<?> value();

    /**
     * 版本控制
     * @return
     */
    String version() default "";
}
