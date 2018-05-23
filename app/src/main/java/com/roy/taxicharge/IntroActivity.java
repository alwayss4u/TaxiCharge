package com.roy.taxicharge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    RelativeLayout intro_layout;

    //스케쥴 관리 객체(비서)
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        intro_layout = findViewById(R.id.intro_layout);

        //appear_logo.xml문서를 읽어서 Animation객체로 생성
        intro_layout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.logo_appear));

        //4초후에 MainActivity 실행
        //Schedule 관리 객체에게 Schedule
        timer.schedule(task, 4000);
    }

    //timer의 schedule 작업을 수행하는 객체 생성
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            startActivity( new Intent(IntroActivity.this, MainActivity.class));
        }
    };

}
