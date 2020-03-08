package com.bundang.monitor.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class IntegerHeaderFrameDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf buf, List<Object> out) {

        if (buf.readableBytes() < 5) {
            return;
        }

        buf.markReaderIndex();
        byte startByte = buf.readByte();
        int length = buf.readInt();

        if (buf.readableBytes() < length) {
            buf.resetReaderIndex();
            return;
        }

        buf.resetReaderIndex();
        int headerLength = 5;
        out.add(buf.readBytes(length + headerLength));
    }
}
