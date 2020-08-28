package com.parthbt143.jalarambapasatbavni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MediaPlayer mPlayer;
    ImageButton btnPlayPause;
    TextView time,totalDurationlbl;
    Handler handler = new Handler();
    SeekBar seekBar;
    int IS_PLAYING = 0;
    int totalDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayer =  MediaPlayer.create(this, R.raw.sat_bavni);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        time = findViewById(R.id.time);
        totalDurationlbl = findViewById(R.id.totalDuration);
        seekBar = findViewById(R.id.seekBar);

        totalDuration = mPlayer.getDuration();

        String min = padLeftSpaces(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(mPlayer.getDuration())),2);
        String sec = padLeftSpaces(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(mPlayer.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mPlayer.getDuration()))),2);
        totalDurationlbl.setText(min+":"+sec);

        seekBar.setMax(totalDuration);
        btnPlayPause.setOnClickListener(this);
        handler.postDelayed(changeTime, 100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                IS_PLAYING = 0;
//                mPlayer.pause();
//                btnPlayPause.setImageResource(R.drawable.play_img);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if(IS_PLAYING == 0)
                {
                    btnPlayPause.setImageResource(R.drawable.play_img);
                }else{
                    btnPlayPause.setImageResource(R.drawable.pause_img);
                }
                mPlayer.seekTo(seekBar.getProgress());

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnPlayPause:
                if(IS_PLAYING == 0){
                    mPlayer.start();
                    IS_PLAYING = 1;
                    btnPlayPause.setImageResource(R.drawable.pause_img);
                }else{
                    IS_PLAYING = 0;
                    mPlayer.pause();
                    btnPlayPause.setImageResource(R.drawable.play_img);
                }

                break;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        if(IS_PLAYING == 1)
        {
            sendNotification("1", "Jay Jalaram", "Sat Bavni Is Being Played In Background");
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.cancelAll();
    }

    @Override
    protected void onStop() {
        super.onStop();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.cancelAll();
    }

    private Runnable changeTime  = new Runnable() {
        @Override
        public void run() {

            String min = padLeftSpaces(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(mPlayer.getCurrentPosition())),2);
            String sec = padLeftSpaces(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(mPlayer.getCurrentPosition()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mPlayer.getCurrentPosition()))),2);
            time.setText(min+":"+sec);



            if(mPlayer.getCurrentPosition() >= mPlayer.getDuration())
            {
                if(IS_PLAYING == 1)
                {
                    IS_PLAYING = 0;
                    btnPlayPause.setImageResource(R.drawable.restart_img);
//                     Toast.makeText(MainActivity.this, "Over", Toast.LENGTH_LONG).show();
                }

            }

            handler.postDelayed(this, 100);
            seekBar.setProgress(mPlayer.getCurrentPosition());
        }
    };

    private void sendNotification(String id, String title, String details) {
        String NOTIFICATION_CHANNEL_ID = "bg_music";


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.play_img)
                .setContentTitle(title)
                .setContentText(details)
                .setSound(null)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_MIN);
//        Uri alarmSound = RingtoneManager
//                .getDefaultUri(R.raw.sound);
//        builder.setSound(alarmSound);
//        builder.setAutoCancel(false);

        Notification notification = builder.build();
        // this is the main thing to do to make a non removable notification
        notification.flags |= Notification.FLAG_NO_CLEAR;

        Intent resultIntent = this.getIntent();
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setAction(Intent.ACTION_MAIN);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) MainActivity.this
                .getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Background Play";
            String description ="";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            channel.enableVibration(true);
            channel.setSound(null, null);


            notificationManager.createNotificationChannel(channel);

        }

        Notification not = builder.build();
        not.flags =Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(Integer.parseInt(id), not);




    }
    public static String padLeftSpaces(String str, int n) {
        return String.format("%1$" + n + "s", str).replace(' ', '0');
    }
}
