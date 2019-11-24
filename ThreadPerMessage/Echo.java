package ThreadPerMessage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Echo {

    public static void main(String[] args) throws IOException {

        final ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(8080));

        try {
            while (true) {
                SocketChannel sc = ssc.accept();
                new Thread(() -> {
                    try {
                        ByteBuffer rb = ByteBuffer.allocateDirect(1024);
                        sc.read(rb);
                        Thread.sleep(2000);
                        ByteBuffer wb = (ByteBuffer) rb.flip();
                        sc.write(wb);
                        sc.close();
                    } catch (Exception e) {
                        throw new UncheckedIOException((IOException) e);
                    }
                }).start();
            }
        } finally {
            ssc.close();
        }
    }
}
