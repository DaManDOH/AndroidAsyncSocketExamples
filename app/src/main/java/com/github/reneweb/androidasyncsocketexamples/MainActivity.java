package com.github.reneweb.androidasyncsocketexamples;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    private static class CommsTestAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //TCP client and server (Client will automatically send welcome message after setup and server will respond)
            new com.github.reneweb.androidasyncsocketexamples.tcp.Server("localhost", 7000);
            new com.github.reneweb.androidasyncsocketexamples.tcp.Client("localhost", 7000);

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

        new CommsTestAsyncTask().execute();
    }

}
