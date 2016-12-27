package com.example.administrator.myplay;

import android.Manifest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static MediaPlayer mMediaPlayer;
    private static boolean isPause = false;

    private static final String LOG_TAG = "" ;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private File fpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        fpath = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/data/files/");

       // fpath.mkdirs();// 创建文件夹
        try {
// 创建临时文件,注意这里的格式为.pcm
            audioFile = File.createTempFile("recording", ".pcm", fpath);
        } catch (IOException e) {
//         TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(mediaRecorder == null) {

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setOutputFile(String.valueOf(audioFile));
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
        }


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(audioFile.getAbsolutePath());
            }
        });
 }
//    停止录音
//    public void stopAndRelease() {
//        if (mediaRecorder == null) return;
//
//        mediaRecorder.release();
//        mediaRecorder = null;
//    }

    //播放
    public  void playSound(String filePath) {

//      mediaRecorder.stop();
//        if (mMediaPlayer == null) {
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                @Override
//                public boolean onError(MediaPlayer mp, int what, int extra) {
//                    mMediaPlayer.reset();
//                    return false;
//                }
//            });
//        } else {
//            mMediaPlayer.reset();
//        }
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
