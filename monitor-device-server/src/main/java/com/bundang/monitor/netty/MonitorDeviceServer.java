package com.bundang.monitor.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Component
public class MonitorDeviceServer {

    @Autowired
    private PortStatServerHandler portStatServerHandler;

    @Value("${server.default-port}")
    private int defaultPort;

    @Value("${server.base-port}")
    private int basePort;

    @Value("${server.numofport}")
    private int numberOfPort;

    @Value("${sequential_multi_port}")
    private boolean isSequentialMultiPort;

    public void start() {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            final ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel ch) {
                            final ChannelPipeline p = ch.pipeline();
                            p.addLast(new IntegerHeaderFrameDecoder());
                            p.addLast(new CaptureServerHandler());
                            p.addLast(portStatServerHandler);
                        }
                    });

            ArrayList<Integer> ports = new ArrayList<>();
            if(isSequentialMultiPort) {
                for (int p = basePort; p < basePort + numberOfPort; p++)
                    ports.add(p);
            } else {
                ports.add(defaultPort);
            }

            Collection<Channel> channels = new ArrayList<>(ports.size());

            for(int p : ports) {
                Channel serverChannel = b.bind(p).sync().channel();
                channels.add(serverChannel);
            }

            for(Channel ch : channels)
                ch.closeFuture().sync();

        }catch(final Exception e){
            log.info("Shutting down ...");
            e.printStackTrace();
        }
        finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
