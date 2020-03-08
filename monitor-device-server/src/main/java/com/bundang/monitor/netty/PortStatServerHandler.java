package com.bundang.monitor.netty;

import com.bundang.monitor.application.PortService;
import com.bundang.monitor.domain.Port;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
@Component
public class PortStatServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private PortService portService;

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
        byteBuf.release();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(readMessage);
        JSONArray portArray = (JSONArray) jsonObj.get("ports");

        for (int i = 0; i < portArray.size(); i++) {
            JSONObject portObject = (JSONObject) portArray.get(i);
            Integer number = new Integer(String.valueOf(portObject.get("number")));
            String state = portObject.get("state").toString();

            List<Port> pl = portService.getPorts(number);
            // NOT empty and same state
            if(pl.size() > 0) {
                /* TODO : for the moment, Assume last index is lastest code */
                Port p = pl.get(pl.size() - 1);
                if (p.getState().equals(state))
                    continue;
            }

            portService.addPort(
                    Port.builder()
                            .number(number)
                            .state(state)
                            //.date(date)
                            .build()
            );
        }

        log.info("Received port stat data .... {}", readMessage);
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
