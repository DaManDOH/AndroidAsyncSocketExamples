package com.github.reneweb.androidasyncsocketexamples.tcp;

import android.util.Log;

import com.koushikdutta.async.*;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.callback.ListenCallback;
import com.koushikdutta.async.callback.WritableCallback;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

    private static final String TAG = "TcpServer";

    private InetAddress host;
    private int port;

    public Server(String host, int port) {
        try {
            this.host = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        this.port = port;

        setup();
    }

    private void setup() {
        AsyncServer.getDefault().listen(host, port, new ListenCallback() {

            @Override
            public void onAccepted(final AsyncSocket socket) {
                handleAccept(socket);
            }

            @Override
            public void onListening(AsyncServerSocket socket) {
                Log.i(TAG, "[TCP Server] Server started listening for connections");
            }

            @Override
            public void onCompleted(Exception ex) {
                if(ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[TCP Server] Successfully shutdown server");
            }
        });
    }

    private void handleAccept(final AsyncSocket socket) {
        Log.i(TAG, "[TCP Server] New Connection " + socket.toString());

        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                Log.i(TAG, "[TCP Server] Received Message: " + new String(bb.getAllByteArray()));

                Util.writeAll(socket, "Hello, Client!".getBytes(), new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        if (ex != null) {
                            throw new RuntimeException(ex);
                        }

                        Log.i(TAG, "[TCP Server] Successfully wrote message");
                    }
                });
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[TCP Server] Successfully closed connection");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }

                Log.i(TAG, "[TCP Server] Successfully end connection");
            }
        });

        socket.setWriteableCallback(new WritableCallback() {
            @Override
            public void onWriteable() {
                Log.i(TAG, "[TCP Server] In Server.WritableCallback::onWriteable()");
            }
        });
    }

}
