package com.github.reneweb.androidasyncsocketexamples.tcp;

import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.Util;

import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.stream.IntStream;

public class Client {

    private static final String TAG = "TcpClient";
    private static final String FIXED_WIDTH_LEADING_ZEROES = "%08d";
//    private static final Integer TRANSMIT_ELEMENT_COUNT = 250000;
//    private static final Integer TRANSMIT_ELEMENT_COUNT = 445;
    private static final Integer TRANSMIT_ELEMENT_COUNT = 1500;

    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;

        setup();
    }

    private void setup() {
        final AsyncServer asyncServer = new AsyncServer();
        asyncServer.connectSocket(
                new InetSocketAddress(host, port),
                this::handleConnectCompleted);
    }

    private void handleConnectCompleted(Exception connectionCompletedEx, final AsyncSocket socket) {
        if (connectionCompletedEx != null) {
            throw new RuntimeException(connectionCompletedEx);
        }

        Log.i(TAG, "[TCP Client] Writing message");

        final StringBuilder messageBuilder = new StringBuilder();
//        messageBuilder.append("Hello, Server...\r\n");
        IntStream.range(0, TRANSMIT_ELEMENT_COUNT)
                .mapToObj(oneInt -> String.format(
                        Locale.US,
                        FIXED_WIDTH_LEADING_ZEROES,
                        oneInt))
                .forEach(oneIntStr -> {
                    messageBuilder
                            .append(oneIntStr)
                            .append(',');
                });
        messageBuilder.append("\r\n\r\n\r\n");

        Util.writeAll(
                socket,
                messageBuilder.toString().getBytes(),
                writeEx -> {
                    if (writeEx != null) {
                        throw new RuntimeException(writeEx);
                    }

                    Log.i(TAG, "[TCP Client] Successfully wrote message");
                });

        socket.setDataCallback((emitter, bb) -> Log.i(TAG, "[TCP Client] Received Message: " + new String(bb.getAllByteArray())));

        socket.setClosedCallback(closedEx -> {
            if (closedEx != null) {
                throw new RuntimeException(closedEx);
            }

            Log.i(TAG, "[TCP Client] Successfully closed connection");
        });

        socket.setEndCallback(endEx -> {
            if (endEx != null) {
                throw new RuntimeException(endEx);
            }

            Log.i(TAG, "[TCP Client] Successfully end connection");
        });

        socket.setWriteableCallback(() -> Log.i(TAG, "[TCP Client] In Client.WritableCallback::onWriteable()"));
    }

}
