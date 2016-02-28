package com.huxian.childhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;

    private Button btnSendChild;
    private ChildThread childThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSendChild = (Button) findViewById(R.id.btn_send_to_child_thread_msg);
        btnSendChild.setOnClickListener(sendChildListener);

        childThread = new ChildThread();
        childThread.start();
    }

    private View.OnClickListener sendChildListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            childThread.getHandler().sendEmptyMessage(102);
        }
    };

    @Override
    protected void onDestroy() {
        childThread.terminateMessageLooper();
        super.onDestroy();
    }

    class ChildThread extends Thread {

        Handler handler;
        Looper looper;

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            looper = Looper.myLooper();

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Log.e(TAG, "child thread received msg from ui thread");
                    return false;
                }
            });

            Looper.loop();
        }

        Handler getHandler() {
            return handler;
        }

        void terminateMessageLooper() {
            if (looper != null) {
                looper.quit();
            }
        }
    }

}
