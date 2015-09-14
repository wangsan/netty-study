package com.wangsan.study.netty;

import com.google.common.primitives.Ints;
import com.wangsan.study.netty.tcperror.TcpSuccessTimeClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author wangsan
 * @date 2015/6/28
 */
public class TimeClient {

    public void connect(int port, String host) throws Exception {
        // 配置客户单nio线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            // 默认处理
                            // channel.pipeline().addLast(new TimeClientHandler());

                            // 2.错误的处理，无法解析tcp粘包和拆包
                            // channel.pipeline().addLast(new TcpErrorTimeClientHandler());

                            // 3.正确的处理，可以解决粘包
                            channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            channel.pipeline().addLast(new StringDecoder());
                            channel.pipeline().addLast(new TcpSuccessTimeClientHandler());

                        }
                    });

            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
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

        new TimeClient().connect(port, "127.0.0.1");
    }
}
