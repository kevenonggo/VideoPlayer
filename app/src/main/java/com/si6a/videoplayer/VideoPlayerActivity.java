package com.si6a.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    private TextView videoNameTV, videoTimeTV;
    private ImageButton backIB, forwardIB, playPauseIB;
    private SeekBar videoSeekBar;
    private VideoView videoView;
    private RelativeLayout controlsRL, videoRL;
    boolean isOpen = true;
    private String videoName, videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoName = getIntent().getStringExtra("videoName");
        videoPath = getIntent().getStringExtra("videoPath");
        videoNameTV = findViewById(R.id.tv_videotitle);
        videoTimeTV = findViewById(R.id.tv_time);
        backIB = findViewById(R.id.ib_replay);
        forwardIB = findViewById(R.id.ib_forward);
        playPauseIB = findViewById(R.id.ib_play);
        videoSeekBar = findViewById(R.id.sb_progress);
        videoView = findViewById(R.id.VideoView);
        playPauseIB = findViewById(R.id.ib_play);
        controlsRL = findViewById(R.id.rl_control);
        videoRL = findViewById(R.id.rl_control);

        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoSeekBar.setMax(videoView.getDuration());
                videoView.start();
            }
        });

        videoNameTV.setText(videoName);
        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getDuration()+10000);
            }
        });

        forwardIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getDuration()+10000);
            }
        });

        playPauseIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()){
                    videoView.pause();
                    playPauseIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                }else {
                    videoView.start();
                    playPauseIB.setImageDrawable((getResources().getDrawable(R.drawable.ic_pause)));
                }
            }
        });

        videoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen){
                    hideControls();
                    isOpen = false;
                }else {
                    showControls();
                    isOpen = true;
                }
            }
        });
        setHandler();
        initializeSeekBar();
    }

    private void setHandler(){
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(videoView.getDuration()>0){
                    int curPos = videoView.getCurrentPosition();
                    videoSeekBar.setProgress(curPos);
                    videoTimeTV.setText(""+convertTime(videoView.getDuration()-curPos));
                }
                handler.postDelayed(this,0);
            }
        };
        handler.postDelayed(runnable,500);
    }
    private String convertTime(int ms){
        String time;
        int x,seconds,minutes,hours;
        x = ms/1000;
        seconds = x%60;
        x /=60;
        minutes = x % 60;
        x /=60;
        hours = x %24;
        if (hours!=0){
            time = String.format("%02d",hours)+":"+String.format("%02",minutes)+":"+String.format("%02",seconds);
        }else {
            time = String.format("%02",minutes)+":"+String.format("%02",seconds);
        }
        return time;
    }
    private void initializeSeekBar(){
    videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (videoSeekBar.getId()==R.id.sb_progress){
                if(fromUser){
                    videoView.seekTo(progress);
                    videoView.start();
                    int curPos = videoView.getCurrentPosition();
                    videoTimeTV.setText("" + convertTime(videoView.getDuration() - curPos));
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    }

    private void showControls() {
        controlsRL.setVisibility(View.VISIBLE);

        final Window window = this.getWindow();
        if (window==null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = window.getDecorView();
        if (decorView!=null){
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT>=14){
                uiOption&= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT>=16){
                uiOption&= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT>=19){
                uiOption&= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void hideControls() {
        controlsRL.setVisibility(View.GONE);

        final Window window = this.getWindow();
        if (window==null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = window.getDecorView();
        if (decorView!=null){
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT>=14){
                uiOption |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT>=16){
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT>=19){
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }
}