package com.wangsan.study.nio;

import com.google.common.primitives.Ints;

/**
 * @author wangsan
 * @date 2015/6/27
 */
public class TimeServer {
    public static void main(String[] args) {
        Integer port = null;
        if (args != null && args.length > 0) {
            port = Ints.tryParse(args[0]);
        }
        if (port == null) {
            port = 8080;
        }

        new Thread(new MultiplexerTimeServer(port), "NIO-MultiplexerTimeServer-001").start();
    }
}
