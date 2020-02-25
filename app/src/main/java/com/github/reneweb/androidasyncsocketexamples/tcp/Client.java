package com.github.reneweb.androidasyncsocketexamples.tcp;

import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.callback.WritableCallback;

import java.net.InetSocketAddress;

public class Client {

    private static final String TAG = "TcpClient";

    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;

        setup();
    }

    private void setup() {
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                handleConnectCompleted(ex, socket);
            }
        });
    }

    private void handleConnectCompleted(Exception ex, final AsyncSocket socket) {
        if (ex != null) {
            throw new RuntimeException(ex);
        }

        Log.i(TAG, "[TCP Client] Writing message");

        Util.writeAll(socket, "Hello Server".getBytes(), new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[TCP Client] Successfully wrote message");
            }
        });

        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                Log.i(TAG, "[TCP Client] Received Message " + new String(bb.getAllByteArray()));
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[TCP Client] Successfully closed connection");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[TCP Client] Successfully end connection");
            }
        });

        socket.setWriteableCallback(new WritableCallback() {
            @Override
            public void onWriteable() {
                Log.i(TAG, "[TCP Client] In Client.WritableCallback::onWriteable()");
            }
        });
    }

}
