package com.wangsan.study.aio;

import com.google.common.primitives.Ints;
import com.wangsan.study.nio.TimeClientHandler;

/**
 * @author wangsan
 * @date 2015/6/27
 */
public class TimeClient {
    public static void main(String[] args) {
        Integer port = null;
        if (args != null && args.length > 0) {
            port = Ints.tryParse(args[0]);
        }
        if (port == null) {
            port = 8080;
        }

        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "NIO-AsyncTimeClient-001").start();
    }

}
