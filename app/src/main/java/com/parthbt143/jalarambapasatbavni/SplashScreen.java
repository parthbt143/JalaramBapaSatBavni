package com.parthbt143.jalarambapasatbavni;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(){

            public void run()
            {
                try{
                    sleep(3000);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }finally {
                    Intent intent= new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }

        };
        thread.start();
    }

    protected void onPause()
    {
        super.onPause();
        finish();
    }
}
