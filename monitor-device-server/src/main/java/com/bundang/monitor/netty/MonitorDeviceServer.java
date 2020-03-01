package com.bundang.monitor.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonitorDeviceServer {
    private static final int PORT = 8090;

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
                        protected void initChannel(final SocketChannel ch) throws Exception {
                            final ChannelPipeline p = ch.pipeline();
                            p.addLast(new IntegerHeaderFrameDecoder());
                            p.addLast(new CaptureServerHandler());
                            p.addLast(new PortStateServerHandler());
                        }
                    });


            final ChannelFuture future = b.bind(PORT).sync();


            future.channel().closeFuture().sync();
        }catch(final Exception e){
            log.info("Shutting down ~~~");
            e.printStackTrace();
        }
        finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
