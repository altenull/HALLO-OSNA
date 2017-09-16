package com.altenull.hallo_osna;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.viewanimator.ViewAnimator;


public class IntroActivity extends Activity {
    private boolean isLoadedXML = false;
    private boolean isFinishedIntro = false;


    private void changeMain() {
        if ( this.isLoadedXML && this.isFinishedIntro ) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }


    private void loadXML() {
        ConnectThread localConnectThread = new ConnectThread("https://s3.ap-northeast-2.amazonaws.com/altenull/AndroidApp/HALLO-OSNA/HALLO-OSNA.xml", new ProgressHandler(), IntroActivity.this);
        localConnectThread.start();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ViewAnimator.animate(((ImageView)findViewById(R.id.intro_symbol))).rubber().duration(1000L).startDelay(1000L).start();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                IntroActivity.this.isFinishedIntro = true;
                IntroActivity.this.changeMain();
            }
        }, 2000L);
        loadXML();
    }


    public class ProgressHandler extends Handler {
        public ProgressHandler() {}

        public void handleMessage(Message paramMessage) {
            if ( !paramMessage.obj.toString().equals("") ) {
                Toast.makeText(IntroActivity.this, paramMessage.obj.toString(), Toast.LENGTH_LONG).show();
            }

            IntroActivity.this.isLoadedXML = true;
            IntroActivity.this.changeMain();
        }
    }


}