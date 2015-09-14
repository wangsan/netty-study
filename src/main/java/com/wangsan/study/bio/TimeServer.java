package com.wangsan.study.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.primitives.Ints;

/**
 * 1.传统的阻塞io模式，是一客户端一线程模式，受限于线程数等<br></br>
 * 2.在传统的同步，使用线程池，改良下线程问题。会合理管理线程。但是底层依然是同步阻塞模型
 *
 * @author wangsan
 * @date 2015/6/1
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        Integer port = null;
        if (args != null && args.length > 0) {
            port = Ints.tryParse(args[0]);
        }
        if (port == null) {
            port = 8080;
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            // 2.伪异步模式
            ThreadPoolExecutor executor = getThreadPoolExecutor();

            while (true) {
                Socket socket = server.accept();
                // 1.传统一线程一客户
                // new Thread(new TimeServerHandler(socket)).start();

                // 2.伪异步模式
                executor.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
            }
        }
    }

    /**
     * 线程池模式，伪异步方式，仅执行一次即可
     *
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor getThreadPoolExecutor() {
        int coreSize = Runtime.getRuntime().availableProcessors();
        int maxSize = 100;
        int queueSize = 10000;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(queueSize);
        return new ThreadPoolExecutor(coreSize, maxSize, 120L, TimeUnit.SECONDS, workQueue);
    }
}
