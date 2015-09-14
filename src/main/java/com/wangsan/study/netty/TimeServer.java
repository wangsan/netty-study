package com.wangsan.study.netty;

import com.google.common.primitives.Ints;
import com.wangsan.study.netty.tcperror.TcpSuccessTimeServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author wangsan
 * @date 2015/6/28
 */
public class TimeServer {

    public void bind(int port) throws Exception {
        // 配置nio服务端的线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            // 等待服务器监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            // 默认处理
            // channel.pipeline().addLast(new TimeServerHandler());

            // 2.错误的处理，无法解析tcp粘包和拆包
            // channel.pipeline().addLast(new TcpErrorTimeServerHandler());

            // 3.正确的处理，可以解决粘包
            channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            channel.pipeline().addLast(new StringDecoder());
            channel.pipeline().addLast(new TcpSuccessTimeServerHandler());

        }
    }

    public static void main(String[] args) throws Exception {
        Integer port = null;
        if (args != null && args.length > 0) {
            port = Ints.tryParse(args[0]);
        }
        if (port == null) {
            port = 8080;
        }

        new TimeServer().bind(port);
    }
}
