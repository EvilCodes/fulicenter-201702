package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import cn.ucai.fulicenter.R;

/**
 * Created by clawpo on 2017/5/3.
 */

public class SplashActivity extends AppCompatActivity {

    private final static int time = 5000;
    TextView tvSkip;
    MyCountDownTimer cdt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.e("main","onCreate.....");
        tvSkip = (TextView) findViewById(R.id.tv_skip);
        cdt = new MyCountDownTimer(time,1000);
        cdt.start();
    }

    class MyCountDownTimer extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("MyCountDownTimer","onTick....."+millisUntilFinished);
            tvSkip.setText(getString(R.string.skip)+" " + millisUntilFinished/1000 + "s");
        }

        @Override
        public void onFinish() {
            Log.e("MyCountDownTimer","onFinish.....");
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
    }
}
