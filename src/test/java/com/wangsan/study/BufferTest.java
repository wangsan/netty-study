package com.wangsan.study;

import java.nio.CharBuffer;

import org.junit.Test;

/**
 * 使用Buffer读写数据一般遵循以下四个步骤：
 * <p/>
 * 写入数据到Buffer
 * 调用flip()方法
 * 从Buffer中读取数据
 * 调用clear()方法或者compact()方法
 * <p/>
 * http://ifeve.com/buffers/
 *
 * @author wangsan
 * @date 2015/11/17
 */
public class BufferTest {

    CharBuffer buffer = CharBuffer.allocate(20);

    private void fill() {
        String test = "hello";
        for (int i = 0; i < test.length(); i++) {
            buffer.put(test.charAt(i));
        }
    }

    @Test
    public void testBasic() throws Exception {
        System.out.println(
                "初始化时候：position=" + buffer.position() + " limit=" + buffer.limit() + " capacity=" + buffer.capacity());
        fill();
        System.out.println("填入hello以后：position=" + buffer.position() + " limit=" + buffer.limit());
        buffer.put(0, 'M').put('w');
        System.out.println("修改成Mellow后：position=" + buffer.position() + " limit=" + buffer.limit());
        buffer.flip();
        System.out.println("翻转后：position=" + buffer.position() + " limit=" + buffer.limit());
        buffer.clear();
        System.out.println("clear后：position=" + buffer.position() + " limit=" + buffer.limit());
        buffer.position(0);
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
    }

    /**
     * flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。
     * 换句话说，position现在用于标记读的位置，limit表示之前写进了多少个byte、char等 —— 现在能读取多少个byte、char等。
     */
    @Test
    public void testFlip() {
        fill();
        System.out.println("flip before：position=" + buffer.position() + " limit=" + buffer.limit());
        buffer.flip();
        System.out.println("flip after：position=" + buffer.position() + " limit=" + buffer.limit());

        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.println("read after：position=" + buffer.position() + " limit=" + buffer.limit());
    }

    /**
     * Buffer.rewind()将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，
     * 仍然表示能从Buffer中读取多少个元素（byte、char等）。一般
     */
    @Test
    public void testRewind() {
        fill();
        buffer.flip();
        System.out.print("flip and read: ");
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.print("\n");

        System.out.println("rewind前：position=" + buffer.position() + " limit=" + buffer.limit());
        buffer.rewind();
        System.out.println("rewind后：position=" + buffer.position() + " limit=" + buffer.limit());
        System.out.print("rewind and read: ");
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.print("\n");
    }

    @Test
    public void testCompact() {
        fill();
        buffer.flip();
        System.out.println("compact before：position=" + buffer.position() + " limit=" + buffer.limit());
        buffer.compact();
        System.out.println("compact after：position=" + buffer.position() + " limit=" + buffer.limit());
        buffer.put("j");
        buffer.put("a");
        buffer.put("v");
        buffer.put("a");

        buffer.flip();
        System.out.print("compact: ");
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.print("\n");
    }
}
