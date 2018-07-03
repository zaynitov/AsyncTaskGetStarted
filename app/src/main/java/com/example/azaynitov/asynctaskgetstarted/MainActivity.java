package com.example.azaynitov.asynctaskgetstarted;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final static int SHOW_TEXT = 1001;
    final static int SHOW_PROGRESS = 1002;
    final static int HIDE_PROGRESS = 1003;
    final static int SHOW_TEXT_PROGRESS = 1004;
    TextView mTextView;
    ProgressBar mProgressBar;

    UIHandler mHandler;

    private Handler getUIHandler() {
        return mHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    private void init() {
        mTextView = (TextView) findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mHandler = new UIHandler();
        MyAsyncTask task = new MyAsyncTask();
        task.execute("111", "222", "333", "444");
    }


    class UIHandler extends Handler {
        public static final int DEFAULT_DELAY = 300;

        public UIHandler() {
            super(Looper.myLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TEXT:
                    mTextView.setText((String) msg.obj);
                    break;
                case SHOW_PROGRESS:
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case HIDE_PROGRESS:
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case SHOW_TEXT_PROGRESS:
                    mTextView.setText((String) msg.obj);

            }


        }


    }


    class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            int i = 0;
            for (String string : strings) {
                result += string + String.valueOf(i++);
                publishProgress(i);
                showText(result);
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            showProgress();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            showTextProgress(String.valueOf(values[0]));

        }

        @Override
        protected void onPostExecute(String s) {
            hideProgress();
        }


        public void showText(String text) {
            Message message = new Message();
            message.what = SHOW_TEXT;
            message.obj = text;
            getUIHandler().sendMessageDelayed(message, UIHandler.DEFAULT_DELAY);
        }

        public void showTextProgress(String text) {
            Message message = new Message();
            message.what = SHOW_TEXT_PROGRESS;
            message.obj = text;
            getUIHandler().sendMessageDelayed(message, UIHandler.DEFAULT_DELAY);
        }

        public void showProgress() {
            Message message = new Message();
            message.what = SHOW_PROGRESS;
            if (getUIHandler().hasMessages(HIDE_PROGRESS)) {
                getUIHandler().removeMessages(HIDE_PROGRESS);
            }
            getUIHandler().sendMessageDelayed(message, UIHandler.DEFAULT_DELAY);
        }

        public void hideProgress() {
            Message message = new Message();
            message.what = HIDE_PROGRESS;
            if (getUIHandler().hasMessages(SHOW_PROGRESS)) {
                getUIHandler().removeMessages(SHOW_PROGRESS);
            }
            getUIHandler().sendMessageDelayed(message, UIHandler.DEFAULT_DELAY);
        }


    }


}
