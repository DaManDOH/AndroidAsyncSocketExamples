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

public class Client {

    private static final String TAG = "UdpClient";

    private final InetSocketAddress host;
    private AsyncDatagramSocket asyncDatagramSocket;

    public Client(String host, int port) {
        this.host = new InetSocketAddress(host, port);
        setup();
    }

    private void setup() {
        try {
            asyncDatagramSocket = AsyncServer.getDefault().connectDatagram(host);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        asyncDatagramSocket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[UDP Client] Successfully closed connection");
            }
        });

        asyncDatagramSocket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                Log.i(TAG, "[UDP Client] Received Message " + new String(bb.getAllByteArray()));
            }
        });

        asyncDatagramSocket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[UDP Client] Successfully end connection");
            }
        });
    }

    public void send(String msg) {
        Log.i(TAG, "[UDP Client] Blind-sending message");
        asyncDatagramSocket.send(host, ByteBuffer.wrap(msg.getBytes()));
        Log.i(TAG, "[UDP Client] Message sent blindly");
    }

}
