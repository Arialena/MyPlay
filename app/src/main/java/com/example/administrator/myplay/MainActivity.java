package com.example.administrator.myplay;

import android.Manifest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
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

    private int BASE = 600;
    private int SPACE = 200;// 间隔取样时间


    private final Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what=msg.what;
            //根据mHandler发送what的大小决定话筒的图片是哪一张
            //说话声音越大,发送过来what值越大
            if(what>13){
                what=13;
            }
        };
    };
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

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

                playSound(audioFile.getAbsolutePath());
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

      mediaRecorder.stop();
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }
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

    private void updateMicStatus() {
        if(mediaRecorder!=null){
            int ratio = mediaRecorder.getMaxAmplitude(); //BASE;
            int db = 0;// 分贝
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            System.out.println("分贝值："+db+"     "+Math.log10(ratio));
            //我对着手机说话声音最大的时候，db达到了35左右，
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
//         所以除了2，为的就是对应14张图片
//            mHandler.sendEmptyMessage(db/2);
        }
    }

}
