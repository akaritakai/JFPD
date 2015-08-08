import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.time.LocalDateTime;

public final class FpdServer {

    private static int PORT;
    static {
        try {
            PORT = Integer.parseInt(System.getenv("port"));
        } catch (Exception e) {
            PORT = 843;
        }
    }
    private static int TIMEOUT;
    static {
        try {
            TIMEOUT = Integer.parseInt(System.getenv("timeout"));
        } catch (Exception e) {
            TIMEOUT = 30;
        }
    }

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(TIMEOUT, TIMEOUT, TIMEOUT));
                            p.addLast(new FpdServerHandler());
                        }
                    });
            log("FPD server started.");
            ChannelFuture f = b.bind(PORT).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log("FPD server stopped.");
        }
    }

    private static void log(String msg) {
        String timestamp = LocalDateTime.now().toString();
        System.out.println(timestamp + "\t" + msg);
    }

}
