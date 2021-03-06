package edu.northeastern.ccs.im.server;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.PrintNetNB;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests for the class client runnable
 */
class ClientRunnableTest {
    private static final int port = 4518;
    private static final int port2 = 4516;
    private static final int port3 = 4510;
    private static final int port4 = 4523;
    private static final int port5 = 4511;
    private static final int port6 = 4509;
    private static final int port7 = 4519;
    private static final int port8 = 4521;



    @Test
    void testNewClient() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port);
        socketChannel.connect(socketAddr);
        List<Message> msgs = new ArrayList<>();
        PrintNetNB printer = new PrintNetNB(socketChannel);
        msgs.add(Message.makeDeleteMessage("username42", null));
        for (Message msg : msgs) {
            printer.print(msg);
        }
        socketChannel.close();

        SocketChannel channel = serverSocket.accept();
        ClientRunnable client = new ClientRunnable(channel);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        ScheduledFuture clientFuture = threadPool.scheduleAtFixedRate(client, 200,
                200, TimeUnit.MILLISECONDS);
        client.setFuture(clientFuture);

        assertFalse(client.isValidated());
        assertFalse(client.isInitialized());
        assertEquals(0, client.getUserId());
        client.signin("username42", "password42");
        client.signup("username42", "password42");
//        assertTrue(client.isValidated());
//        assertTrue(client.isInitialized());
        try {
            for (int i = 0; i < msgs.size() + 10; i++) {
                client.run();
            }
        } catch (Exception e) {
        }

        assertNotEquals(0, client.getUserId());
        client.enqueueMessage(Message.makeAcknowledgeMessage(client.getName()));
    }

    @Test
    void testOldClient2() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port5));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port5);
        socketChannel.connect(socketAddr);
        List<Message> msgs = new ArrayList<>();
        PrintNetNB printer = new PrintNetNB(socketChannel);
        msgs.add(Message.makeQuitMessage("username333"));
        for (Message msg : msgs) {
            printer.print(msg);
        }
        socketChannel.close();

        SocketChannel channel = serverSocket.accept();
        ClientRunnable client = new ClientRunnable(channel);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        ScheduledFuture clientFuture = threadPool.scheduleAtFixedRate(client, 200,
                200, TimeUnit.MILLISECONDS);
        client.setFuture(clientFuture);

        assertFalse(client.isValidated());
        assertFalse(client.isInitialized());
        assertEquals(0, client.getUserId());
        client.signin("username333", "username333");
        try {
            for (int i = 0; i < msgs.size() + 10; i++) {
                client.run();
            }
        } catch (Exception e) {
        }
    }

    @Test
    void testOldClient3() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port8));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port8);
        socketChannel.connect(socketAddr);
        List<Message> msgs = new ArrayList<>();
        PrintNetNB printer = new PrintNetNB(socketChannel);
        msgs.add(Message.makeLoginMessage("oma"));
        msgs.add(Message.makeSignupMessage("oma", "omma"));
        msgs.add(Message.makeSigninMessage("oma", "omma"));
        for (Message msg : msgs) {
            printer.print(msg);
        }
        socketChannel.close();

        SocketChannel channel = serverSocket.accept();
        ClientRunnable client = new ClientRunnable(channel);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        ScheduledFuture clientFuture = threadPool.scheduleAtFixedRate(client, 200,
                200, TimeUnit.MILLISECONDS);
        client.setFuture(clientFuture);

        assertFalse(client.isValidated());
        assertFalse(client.isInitialized());
        try {
            for (int i = 0; i < msgs.size() + 10; i++) {
                client.run();
            }
        } catch (Exception e) {
        }


    }


    @Test
    void testOldClient() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port2));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port2);
        socketChannel.connect(socketAddr);

        SocketChannel socketChannel2 = SocketChannel.open();
        socketChannel2.connect(socketAddr);

        List<Message> msgs = new ArrayList<>();
        PrintNetNB printer2 = new PrintNetNB(socketChannel);
        msgs.add(Message.makeBroadcastMessage("username22", "Hello"));
        msgs.add(Message.makeDirectMessage("username22", "receiverUser", "Hello"));
        msgs.add(Message.makeRecallMessage("username22", "0"));
        msgs.add(Message.makeGroupMessage("username22", "friends", "Hello"));
        msgs.add(Message.makeSetRoleMessage("username22", "opa", "admin"));
        msgs.add(Message.makeRetrieveMessage("username22", "PASSWORD"));
        msgs.add(Message.makeRetrieveMessage("username22", "EPASSWORD"));
        msgs.add(Message.makeRetrieveMessage("username22", "GROUPS"));
        msgs.add(Message.makeRetrieveMessage("username22", "GROUP test_group"));
        msgs.add(Message.makeRetrieveMessage("username22", "GROUP_MESSAGES test_group"));
        msgs.add(Message.makeRetrieveMessage("username22", "WRONG_REQUEST"));

        msgs.add(Message.makeBroadcastMessage("username22", "test"));
        msgs.add(Message.makeJoinMessage("username22", "test_group"));
        msgs.add(Message.makeRetrieveMessage("username22", "GROUP test_group"));
        msgs.add(Message.makeGroupMessage("username22", "test_group", "hello"));
        msgs.add(Message.makeRetrieveMessage("username22", "SEND_MESSAGES"));
        msgs.add(Message.makeRetrieveMessage("username22", "RECEIVE_MESSAGES"));
        msgs.add(Message.makeRetrieveMessage("username22", "GROUP_MESSAGES test_group"));
        msgs.add(Message.makeLeaveMessage("username22", "test_group"));
        msgs.add(Message.makeJoinMessage("username22", "test_group"));
        msgs.add(Message.makeRetrieveMessage("username22", "USERS"));
        msgs.add(Message.makeRetrieveMessage("username22", "ONLINE"));
        msgs.add(Message.makeUpdateMessage("username22", "password22"));
        for (Message msg : msgs) {
            printer2.print(msg);
        }
        socketChannel2.close();

        SocketChannel channel2 = serverSocket.accept();
        ClientRunnable client2 = new ClientRunnable(channel2);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        ScheduledFuture clientFuture = threadPool.scheduleAtFixedRate(client2, 200,
                200, TimeUnit.MILLISECONDS);
        client2.setFuture(clientFuture);
        client2.signin("username22", "password22");
        try {
            for (int i = 0; i < msgs.size() + 10; i++) {
                client2.run();
            }
        } catch (Exception e) {
        }


        serverSocket.close();
    }


    void testAgency() throws IOException, InterruptedException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port4));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port4);
        socketChannel.connect(socketAddr);

        SocketChannel socketChannel2 = SocketChannel.open();
        socketChannel2.connect(socketAddr);

        List<Message> msgs = new ArrayList<>();
        PrintNetNB printer2 = new PrintNetNB(socketChannel);
        msgs.add(Message.makeRetrieveMessage("agencyOne", "WIRETAPS"));
        msgs.add(Message.makeWiretapUserMessage("agencyOne", "oma", "5"));
        msgs.add(Message.makeWiretapGroupMessage("agencyOne", "friends", "5"));
        msgs.add(Message.makeRetrieveMessage("agencyOne", "WIRETAPS"));
        msgs.add(Message.makeDirectMessage("agencyOne", "admin", "hi"));
        msgs.add(Message.makeDirectMessage("agencyOne", "admin", "hi"));
        msgs.add(Message.makeDirectMessage("agencyOne", "admin", "hi"));
        msgs.add(Message.makeRetrieveMessage("agencyOne", "WIRETAPS"));


        for (Message msg : msgs) {
            printer2.print(msg);
        }

        socketChannel2.close();

        SocketChannel channel2 = serverSocket.accept();
        ClientRunnable client2 = new ClientRunnable(channel2);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        ScheduledFuture clientFuture = threadPool.scheduleAtFixedRate(client2, 200,
                200, TimeUnit.MILLISECONDS);
        client2.setFuture(clientFuture);
        client2.signin("agencyOne", "pass");

        try {
            for (int i = 0; i < msgs.size() + 10; i++) {
                client2.run();
            }
        } catch (Exception e) {
        }


        serverSocket.close();
    }


    // tests new user CRUD operations
    @Test
    void testAdmin() throws IOException, InterruptedException {
        this.testAgency();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port3));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port3);
        socketChannel.connect(socketAddr);

        SocketChannel socketChannel2 = SocketChannel.open();
        socketChannel2.connect(socketAddr);

        List<Message> msgs = new ArrayList<>();
        PrintNetNB printer2 = new PrintNetNB(socketChannel);
        msgs.add(Message.makeBroadcastMessage("admin", "ass"));
        msgs.add(Message.makeRetrieveMessage("admin", "SENDER tim"));
        msgs.add(Message.makeRetrieveMessage("admin", "RECEIVER tim"));
        msgs.add(Message.makeRetrieveMessage("admin", "CONTENT hi"));
        msgs.add(Message.makeRetrieveMessage("admin", "DATE 2018-11-30"));
        msgs.add(Message.makeRetrieveMessage("admin", "REQUESTS"));
        msgs.add(Message.makeHelpMessage("admin"));
        msgs.add(Message.makeWiretapApproveMessage("admin", "agencyOne", "*"));
        msgs.add(Message.makeWiretapApproveMessage("admin", "agencyOne", "*"));
        msgs.add(Message.makeWiretapApproveMessage("admin", "agencyOne", "0"));
        msgs.add(Message.makeWiretapRejectMessage("admin", "agencyOne", "3"));
        msgs.add(Message.makeSetRoleMessage("admin", "opa", "agency"));
        msgs.add(Message.makeSetRoleMessage("admin", "opa", "admin"));
        msgs.add(Message.makeSetRoleMessage("admin", "opa", "user"));
        msgs.add(Message.makeSetRoleMessage("admin", "opa", "god"));
        msgs.add(Message.makeRetrieveMessage("admin", "WIRETAPS"));
        msgs.add(Message.makeDirectMessage("admin", "tim", "ass"));

        for (Message msg : msgs) {
            printer2.print(msg);
        }

        socketChannel2.close();

        SocketChannel channel2 = serverSocket.accept();
        ClientRunnable client = new ClientRunnable(channel2);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        ScheduledFuture clientFuture = threadPool.scheduleAtFixedRate(client, 200,
                200, TimeUnit.MILLISECONDS);
        client.setFuture(clientFuture);
        client.signin("admin", "admin");
        try {
            for (int i = 0; i < msgs.size() + 10; i++) {
                client.run();
            }

        } catch (Exception e) {
        }

    }


    @Test
    void testAgency2() throws IOException, InterruptedException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port7));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port7);
        socketChannel.connect(socketAddr);

        SocketChannel socketChannel2 = SocketChannel.open();
        socketChannel2.connect(socketAddr);

        List<Message> msgs = new ArrayList<>();
        PrintNetNB printer2 = new PrintNetNB(socketChannel);
        msgs.add(Message.makeRetrieveMessage("agencyOne", "WIRETAPS"));
        msgs.add(Message.makeWiretapUserMessage("agencyOne", "oma", "5"));
        msgs.add(Message.makeWiretapGroupMessage("agencyOne", "friends", "5"));
        msgs.add(Message.makeWiretapApproveMessage("agencyOne", "agencyOne", "0"));
        msgs.add(Message.makeWiretapRejectMessage("agencyOne", "agencyOne", "3"));
        msgs.add(Message.makeSetRoleMessage("agencyOne", "opa", "god"));
        msgs.add(Message.makeRetrieveMessage("agencyOne", "WIRETAPS"));
        msgs.add(Message.makeDirectMessage("agencyOne", "admin", "hi"));
        msgs.add(Message.makeDirectMessage("agencyOne", "admin", "hi"));
        msgs.add(Message.makeDirectMessage("agencyOne", "admin", "hi"));
        msgs.add(Message.makeLoggerMessage("agencyOne"));
        msgs.add(Message.makePControlMessage("agencyOne", "qqq"));


        for (Message msg : msgs) {
            printer2.print(msg);
        }

        socketChannel2.close();

        SocketChannel channel2 = serverSocket.accept();
        ClientRunnable client = new ClientRunnable(channel2);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        ScheduledFuture clientFuture = threadPool.scheduleAtFixedRate(client, 200,
                200, TimeUnit.MILLISECONDS);
        client.setFuture(clientFuture);
        client.signin("agencyOne", "pass");

        try {
            for (int i = 0; i < msgs.size() + 10; i++) {
                client.run();
            }
        } catch (Exception e) {
        }

        serverSocket.close();
    }

    // tests new user CRUD operations
    @Test
    void testAdmin2() throws IOException, InterruptedException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port6));
        Selector selector = SelectorProvider.provider().openSelector();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", port6);
        socketChannel.connect(socketAddr);

        SocketChannel socketChannel2 = SocketChannel.open();
        socketChannel2.connect(socketAddr);

        List<Message> msgs = new ArrayList<>();
        PrintNetNB printer2 = new PrintNetNB(socketChannel);
        msgs.add(Message.makeLoggerMessage("admin"));
        msgs.add(Message.makePControlMessage("admin", "qqq"));
        msgs.add(Message.makeLoggerMessage("admin"));
        msgs.add(Message.makePControlMessage("admin", "qqq"));
        msgs.add(Message.makeLoggerMessage("admin"));
        msgs.add(Message.makePControlMessage("admin", "qqq"));

        for (Message msg : msgs) {
            printer2.print(msg);
        }

        socketChannel2.close();

        SocketChannel channel2 = serverSocket.accept();
        ClientRunnable client = new ClientRunnable(channel2);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        ScheduledFuture clientFuture = threadPool.scheduleAtFixedRate(client, 200,
                200, TimeUnit.MILLISECONDS);
        client.setFuture(clientFuture);
        client.signin("admin", "admin");
        try {
            for (int i = 0; i < msgs.size() + 10; i++) {
                client.run();
            }
        } catch (Exception e) {
        }

    }

}