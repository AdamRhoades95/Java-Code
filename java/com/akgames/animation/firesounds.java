package com.akgames.animation;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.provider.MediaStore;

public class firesounds extends Thread{
    private boolean isRunning;
    private MediaPlayer gunFire;
    private Context context;
    private SoundPool soundPool;
    public firesounds(Context context){

        this.context =context;

        gunFire = new MediaPlayer();
        gunFire = MediaPlayer.create(context, R.raw.shot_the_gun);
        isRunning = true;
        //start();
    }
    public void isFired(){
        //isRunning = true;
       play();
    }
    public void play() {

            //super.run();
            gunFire.start();
    }
    public void Destroy(){
        gunFire.release();
        //soundPool = null;
    }
}
