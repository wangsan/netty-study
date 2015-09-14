package com.wangsan.study.netty.tcperror;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * tcp粘包和拆包
 *
 * @author wangsan
 * @date 2015/6/28
 */
public class TcpErrorTimeServerHandler extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String separator = System.getProperty("line.separator");
        String body = new String(req, "UTF-8").substring(0, req.length - separator.length());
        System.out.println("The time server receive order : " + body + " ;the counter is : " + ++counter);

        String currentTimeString = new Date().toString();
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? currentTimeString : "BAD ORDER";
        currentTime += separator;

        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
