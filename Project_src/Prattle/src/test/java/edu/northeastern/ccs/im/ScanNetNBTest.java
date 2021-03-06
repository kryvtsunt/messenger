package edu.northeastern.ccs.im;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;

import org.junit.jupiter.api.Test;

import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ScanNetNBTest {

    @Test
    public void testScanNetNB() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        /*
          The port number to listen on.
         */
        int port = 4512;
        serverSocket.socket().bind(new InetSocketAddress(port));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port);
        socketChannel.connect(socketAddr);

        String toSend = "HLO 4 temp 2 --";
        List<String> msgs = new ArrayList<>();
        msgs.add(toSend);
        msgs.add("BCT 4 temp 4 test");
        msgs.add("BYE 4 temp 2 --");
        for (String s : msgs) {
            ByteBuffer wrapper = ByteBuffer.wrap(s.getBytes());
            int bytesWritten = 0;
            while (bytesWritten != s.length()) {
                System.out.println(wrapper);
                bytesWritten += socketChannel.write(wrapper);
            }
        }

        socketChannel.close();
        SocketChannel client = null;
        try {
            client = serverSocket.accept();
            client.configureBlocking(false);
        } catch (Exception ignored) {
        }
//        List<String> tests = Arrays.asList("HLO 4 temp 2 --", "BCT 4 temp 4 test", "BYE 4 temp 2 --");
        List<String> tests = Arrays.asList(Message.makeHelloMessage("Hello").toString(), Message.makeBroadcastMessage("mockUser","testMsg").toString());
        try {
            if (client != null) {
                ScanNetNB  input = new ScanNetNB(client);

//                while (input.hasNextMessage()) {
//                    for (String each : tests) {
//                        Message msg = input.nextMessage();
//                        //System.out.println(msg.toString());
//                        assertEquals(msg.toString(), each);
//                    }
//                }
                //System.out.println("while loop closed in ScanNetNBTest");
                client.close();
                System.out.println("Scan Net NB closed");
                //serverSocket.close();
            } else {
                System.out.println("socket channel is null");
            }
        } catch (Exception e) {
            assertEquals("No next line has been typed in at the keyboard", e.getMessage());
        }
    }

}