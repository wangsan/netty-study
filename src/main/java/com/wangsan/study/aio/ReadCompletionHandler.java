package com.wangsan.study.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

import com.google.common.base.Strings;

/**
 * @author wangsan
 * @date 2015/6/28
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel asc;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        if (this.asc == null) {
            this.asc = channel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] readBytes = new byte[attachment.remaining()];
        attachment.get(readBytes);
        try {
            String body = new String(readBytes, "UTF-8");
            System.out.println("The time server receive order : " + body);

            String currentTimeString = new Date().toString();
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? currentTimeString : "BAD ORDER";
            doWrite(currentTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String currentTime) {
        if (!Strings.isNullOrEmpty(currentTime)) {
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            asc.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // 如果没有发送完，继续发送
                    if (attachment.hasRemaining()) {
                        asc.write(attachment, attachment, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        asc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            asc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
