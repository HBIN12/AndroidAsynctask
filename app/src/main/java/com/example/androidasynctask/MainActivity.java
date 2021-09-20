package com.example.androidasynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {
    private Button load, cancel;
    private TextView loadtext;
    private ProgressBar Bar;
    private MyTask mytask;
    private int state;

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            loadtext.setText("加载中 >>>");
            cancel.setText("暂停加载");
            state = 1;
            // 执行前显示提示
        }

        @Override
        protected String doInBackground(String... params) {
            //模拟耗时任务
            try {
                int rate = 0;
                int speed = 1;
                while (rate < 99) {
                    //加载更新进度
                    publishProgress(rate, 1);
                    Thread.sleep(100);
                    rate = rate + speed;
                    while (state == 2) {
                        Thread.sleep(100);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Bar.setProgress(progress[0]);
            loadtext.setText("加载中<<<" + progress[0] + "%>>>");
        }


        @Override
        protected void onPostExecute(String s) {
            loadtext.setText("加载完成");
            cancel.setText("重新加载");
            state=3;
        }


        protected void onCancelled() {
            loadtext.setText("未加载");
            Bar.setProgress(0);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        state = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load = findViewById(R.id.load);
        cancel = findViewById(R.id.cancel);
        loadtext = findViewById(R.id.loadtext);
        Bar = findViewById(R.id.Bar);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 0) {
                    mytask = new MyTask();
                    mytask.execute();
                }
                if (state==2){
                    state=1;
                }
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (state) {
                            case 1:
                                state = 2;
                                cancel.setText("重新加载");
                                break;
                            case 2:
                                mytask.cancel(true);
                                state=0;
                                cancel.setText("未加载");
                                break;
                            case 3:
                                state=0;
                                Bar.setProgress(0);
                                cancel.setText("未加载");
                                loadtext.setText("未加载");
                                break;

                        }

                    }
                });
            }
        });

    }
}
