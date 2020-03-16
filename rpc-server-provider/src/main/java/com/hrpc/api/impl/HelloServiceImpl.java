package com.hrpc.api.impl;

import com.hrpc.api.IHelloService;
import com.hrpc.api.annnotion.HrpcService;

/**
 * @author huoji
 */
@HrpcService(value = IHelloService.class,version = "v1.0")
public class HelloServiceImpl implements IHelloService {

    @Override
    public String sayHello(String name) {
        return "你好"+name+" 我是基于zookeeper注册中心的RPC调用 版本1";
    }
}
