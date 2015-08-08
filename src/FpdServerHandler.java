import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;

import java.time.LocalDateTime;

@Sharable
public class FpdServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log(ctx, "Connected.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(Unpooled.wrappedBuffer(FpdPolicyReader.getPolicy()))
                .addListener(ChannelFutureListener.CLOSE);
        log(ctx, "Sent policy.");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            ctx.close();
            log(ctx, "Disconnected for idling.");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static void log(ChannelHandlerContext ctx, String msg) {
        String timestamp = LocalDateTime.now().toString();
        String remoteAddress = ctx.channel().remoteAddress().toString().substring(1);
        System.out.println(timestamp + "\t" + remoteAddress + "\t" + msg);
    }

}
