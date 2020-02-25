package com.github.reneweb.androidasyncsocketexamples.udp;

import android.util.Log;

import com.koushikdutta.async.AsyncDatagramSocket;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class Server {

    private static final String TAG = "UdpServer";

    private InetSocketAddress host;
    private AsyncDatagramSocket asyncDatagramSocket;

    public Server(String host, int port) {
        this.host = new InetSocketAddress(host, port);
        setup();
    }

    private void setup() {

        asyncDatagramSocket = AsyncServer.getDefault().openDatagram(
                host.getAddress(),
                host.getPort(),
                true);

        asyncDatagramSocket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                Log.i(TAG, "[UDP Server] Received Message " + new String(bb.getAllByteArray()));
            }
        });

        asyncDatagramSocket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[UDP Server] Successfully closed connection");
            }
        });

        asyncDatagramSocket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[UDP Server] Successfully ended connection");
            }
        });
    }

}
