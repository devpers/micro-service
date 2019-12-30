package com.demo.util.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * discard server handler
 *
 * @author cc.zhao
 * @date 2019/09/05
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            // process msg
            ByteBuf byteBuf = ((ByteBuf) msg);

            StringBuilder stringBuilder = new StringBuilder();
            while (byteBuf.isReadable()) {
                stringBuilder.append((char) byteBuf.readByte());
            }
            System.out.println("request data : " + stringBuilder.toString());

            // response msg
            ctx.writeAndFlush(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // release msg
            // echo msg do not need release
//            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
