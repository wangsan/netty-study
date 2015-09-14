package com.wangsan.study.nio;

import com.google.common.primitives.Ints;

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

        new Thread(new TimeClientHandler("127.0.0.1", port), "NIO-TimeClient-001").start();
    }

}
