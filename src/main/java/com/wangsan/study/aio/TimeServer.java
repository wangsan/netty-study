package com.wangsan.study.aio;

import com.google.common.primitives.Ints;

/**
 * @author wangsan
 * @date 2015/6/28
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

        new Thread(new AsyncTimeServerHandler(port), "AIO-AsyncTimeServerHandler-001").start();
    }
}
