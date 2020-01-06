package com.micro.util.netty.discard;

import com.micro.util.Assert;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;

/**
 * discard server
 *
 * @author cc.zhao
 * @date 2019/09/05
 */
@Data
public class DiscardServer {

    private Integer port;

    public DiscardServer(Integer port) {
        this.port = port;
    }

    public void run() {
        // config boss/work group by constructor method
        // accept incoming connection
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // process connection from bossGroup
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // TODO
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            // custom config handler
                            // ChannelInitializer is abstract class.nonsupport lambda
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    // can add many of handler
                                    ch.pipeline().addLast(new DiscardServerHandler());
                                }
                            }
                    )
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // bind and start to accept incoming connections
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // wait until the server socket is closed
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * nonsupport static method on inner class
     *
     * @param args
     */
    public static void main(String[] args) {
        int port = 8080;
        if (Assert.isNotEmpty(args)) {
//            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }

    class DemoChannelInitializer extends ChannelInitializer {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            // custom handler（one or more）
            ch.pipeline().addLast(new DiscardServerHandler());

            ch.pipeline().addLast();

            // ...
        }
    }

}
