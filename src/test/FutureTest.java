package test;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;

/**
 * Created by laiwenqiang on 2019/3/6.
 */
public class FutureTest {
    public static void main(String[] args) throws InterruptedException {
        EventLoop loop = new DefaultEventLoop();
        Future future = startJob(loop, new Runnable() {
            @Override
            public void run() {
                System.out.println("begin execute...");
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("after execute...");
            }
        });

        future.addListener(new FutureListener<String>() {
            @Override
            public void operationComplete(Future<String> future) throws Exception {
                System.out.println("listener completed! Success: " + future.isSuccess());
            }
        });

        System.out.println("before sync");
        future.sync();
        System.out.println("after sync");
        loop.shutdownGracefully();
    }

    private static Future startJob(EventLoop loop, Runnable runnable) {
        Promise promise = new DefaultPromise(loop);
        loop.execute(runnable);
        loop.execute(setSuccess(promise));
        return promise;
    }

    private static Runnable setSuccess(Promise promise) {
        return new Runnable() {
            @Override
            public void run() {
                promise.setSuccess(null);
            }
        };
    }
}
