package com.bundang.monitor.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.charset.Charset;

@Slf4j
public class PortStateServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;

        if (byteBuf.readByte() != (byte) 0x99) {
            log.error("Receive unexpected data ... unmatched start byte ");
            return;
        }

        int length = byteBuf.readInt();

        if (!byteBuf.isReadable(length)) {
            log.error("Receive unexpected data ... unmatched data length ");
            return;
        }

        String readMessage = byteBuf.toString(Charset.defaultCharset());

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(readMessage);
        JSONArray portArray = (JSONArray) jsonObj.get("ports");

        for (int i = 0; i < portArray.size(); i++) {
            System.out.println("======== port : " + i + " ========");
            JSONObject portObject = (JSONObject) portArray.get(i);
            System.out.println(portObject.get("number"));
            System.out.println(portObject.get("state"));
        }

        log.info("Received data .... {}", readMessage);
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
