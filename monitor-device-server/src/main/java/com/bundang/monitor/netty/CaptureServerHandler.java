package com.bundang.monitor.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class CaptureServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf captureBuf = (ByteBuf) msg;
        byte startByte = captureBuf.readByte();
        if (startByte == (byte) 0x99) {
            /* pass data to portstate handler */
            captureBuf.resetReaderIndex();
            ctx.fireChannelRead(msg);
            return;
        }

        if (startByte != (byte) 0x77) {
            log.error("Unknown Received data .... unmatched start byte");
        }

        String readMessage = captureBuf.toString(Charset.defaultCharset());
        log.info("Received data .... { from Capture }", readMessage);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    };

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
