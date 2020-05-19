package com.github.reneweb.androidasyncsocketexamples;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private Button mTestTcpButton;
    private Button mTestUdpButton;

    private static class TcpCommsTestAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //TCP client and server (Client will automatically send welcome message after setup and server will respond)
            new com.github.reneweb.androidasyncsocketexamples.tcp.Server("localhost", 7000);
            new com.github.reneweb.androidasyncsocketexamples.tcp.Client("localhost", 7000);

            return null;
        }

    }

    private static class UdpCommsTestAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            //UDP client and server (Here the client explicitly sends a message)
            new com.github.reneweb.androidasyncsocketexamples.udp.Server("localhost", 7001);
            new com.github.reneweb.androidasyncsocketexamples.udp.Client("localhost", 7001).send("Hello World");

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTestTcpButton = findViewById(R.id.testTcpButton);
//        mTestTcpButton.setOnClickListener((_unused) -> new TcpCommsTestAsyncTask().execute());
        new TcpCommsTestAsyncTask().execute();

//        mTestUdpButton = findViewById(R.id.testUdpButton);
//        mTestUdpButton.setOnClickListener((_unused) -> new UdpCommsTestAsyncTask().execute());
    }

}
