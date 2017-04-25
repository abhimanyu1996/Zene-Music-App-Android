package com.example.kapils.zene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SlashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);

        Thread timer = new Thread(){
            @Override
            public void run() {

                try{
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }
}
