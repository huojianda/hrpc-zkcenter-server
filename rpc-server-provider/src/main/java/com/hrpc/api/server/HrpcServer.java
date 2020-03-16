package com.hrpc.api.server;

import com.hrpc.api.annnotion.HrpcService;
import com.hrpc.api.registry.IRegistryCenter;
import com.hrpc.api.registry.RegistryCenterWithZk;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class HrpcServer implements ApplicationContextAware,InitializingBean {

    private Map<String,Object> handleMap = new HashMap<String,Object>();

    private int port;

    private IRegistryCenter registryCenter = new RegistryCenterWithZk();
    public HrpcServer(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 接收客户端连接
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        /**
         * 处理已经连接的
         */
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new ProcessorHandler(handleMap));
                    }
                });

            bootstrap.bind(port).sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(HrpcService.class);
        if(!serviceBeanMap.isEmpty()){
            for (Object serviceBean : serviceBeanMap.values()) {
                HrpcService hrpcService = serviceBean.getClass().getAnnotation(HrpcService.class);
                String serviceName = hrpcService.value().getName();
                String version = hrpcService.version();
                if(!StringUtils.isEmpty(version)){
                    serviceName += "-"+version;
                }
                handleMap.put(serviceName,serviceBean);
                registryCenter.registry(serviceName,getAddress()+":"+port);
            }
        }
    }

    private String getAddress() {

        InetAddress inetAddress=null;
        try {
            inetAddress=InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return inetAddress.getHostAddress();// 获得本机的ip地址

    }
}
