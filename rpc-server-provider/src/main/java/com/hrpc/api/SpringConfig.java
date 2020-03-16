package com.hrpc.api;

import com.hrpc.api.server.HrpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huoji
 */
@Configuration
@ComponentScan(basePackages = "com.hrpc.api")
public class SpringConfig {

    @Bean(name = "hrpcServer")
    public HrpcServer hrpcServer(){

        return new HrpcServer(8080);
    }
}
