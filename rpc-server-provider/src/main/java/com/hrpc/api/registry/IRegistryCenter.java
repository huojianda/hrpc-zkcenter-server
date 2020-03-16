package com.hrpc.api.registry;

public interface IRegistryCenter {
    /**
     * 服务注册 名称 和地址
     * @param serviceName
     * @param serviceAddress
     */
    void registry(String serviceName,String serviceAddress);
}
