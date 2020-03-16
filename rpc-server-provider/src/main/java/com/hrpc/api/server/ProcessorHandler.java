package com.hrpc.api.server;

import com.hrpc.api.RpcRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author huoji
 */
public class ProcessorHandler extends SimpleChannelInboundHandler<RpcRequest> {


    private Map<String,Object> handleMap = new HashMap<String, Object>();
    public ProcessorHandler(Map<String, Object> handleMap) {
        this.handleMap = handleMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        Object result=invoke(rpcRequest);
        channelHandlerContext.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);
    }

    private Object invoke(RpcRequest request) throws Exception {
        String serviceName = request.getClassName();
        String version = request.getVersion();

        if(!StringUtils.isEmpty(version)){
            serviceName+="-"+version;
        }
        Object service = handleMap.get(serviceName);

        if(Objects.isNull(service)){
            throw new RuntimeException("service not found:"+serviceName);
        }

        Object[] args = request.getParameters();

        Method method = null;

        Class clazz = Class.forName(request.getClassName());

        method = clazz.getMethod(request.getMethodName(),request.getParamTypes());

        Object result = method.invoke(service, args);

        return result;
    }
}
