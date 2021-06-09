package com.akgames.animation;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;


@SuppressWarnings("ALL")
public class MainActivity extends Activity {
    private GameView game;
    private MediaPlayer bgroundSound;
    private Bundle savedInstanceState;
    private int check;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        check = 0;
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {}
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();

        if(check == 0) {
//            bgroundSound = new MediaPlayer();
//            bgroundSound = MediaPlayer.create(this, R.raw.bground_song1);
//            bgroundSound.setLooping(true);
//            bgroundSound.start();
            try {
                long totalPoints = getIntent().getIntExtra("totalPoints", 0);
                int round = getIntent().getIntExtra("round", 1);
                int kills = getIntent().getIntExtra("kills", 0);
                boolean dead = getIntent().getBooleanExtra("dead", false);
                int modeCheck = getIntent().getIntExtra("modeCheck", 0);
                game = new GameView(this, totalPoints, round, kills, dead, modeCheck);
                game.setUser(intent.getStringExtra("username"));
                setContentView(game);


            } catch (Exception e) {
//            game = new GameView(this);
//            game.setUser(intent.getStringExtra("username"));
//            setContentView(game);
            }

        }
    }




    @Override
    protected void onRestart() {
        Log.d("MainActivity.java", "onRestart()");
        super.onRestart();
        if(check == 1) {
            bgroundSound = MediaPlayer.create(this, R.raw.bground_song1);
            bgroundSound.setLooping(true);
            bgroundSound.start();
//            //game.restoreState(savedInstanceState);
//            game.resume(1);
        }
//                game.resume(1);
//            }
        else{
            check = 1;
        }
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity.java", "onStart()");
        try {
            super.onStart();
            if(check == 1) {
                bgroundSound = MediaPlayer.create(this, R.raw.bground_song1);
                bgroundSound.setLooping(true);
                bgroundSound.start();




                //game.restoreState(savedInstanceState);

                game.resume(1);
            }
//                game.resume(1);
//            }
            else{
                check = 1;
            }
        }catch (Exception e){
//            Intent intent = getIntent();
//            game = new GameView(this);
//            game.setUser(intent.getStringExtra("username"));
//            setContentView(game);
//            super.onStart();
        }
    }

    @Override
    protected void onResume() {
        try {
            Log.d("MainActivity.java", "onResume()");
            super.onResume();
            //Bundle saved = getB
            if (check == 1) {
                bgroundSound = MediaPlayer.create(this, R.raw.bground_song1);
                bgroundSound.setLooping(true);
                bgroundSound.start();
            }
//                //game.restoreState(savedInstanceState);
//                game.resume(1);
//            }
////                game.resume(1);
////            }
//            else{
//                check = 1;
//            }
                //game = new GameView(this);
                //setContentView(game);
//            bgroundSound = MediaPlayer.create(this, R.raw.bground_song1);
//            bgroundSound.setLooping(true);
//            bgroundSound.start();
//            AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>(){
//                protected Void doInBackground(Void... voids) {
//                    try {
//                        game.setUser(getIntent().getStringExtra("username"));
//                        game.resume();
//                    }catch(Exception e){
//
//                    }
//
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void aVoid) {
//                    //super.onPostExecute(aVoid);
//                }
//            };
//            task.execute();
                //task.THREAD_POOL_EXECUTOR.execute();
            }catch(Exception e){

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try{
            super.onSaveInstanceState(outState);
            //outState.putInt(YourSurfaceViewClass.SOME_ID, surfaceViewClass.getVar());
            //game.pause(1);
//            game.pause();
            Log.d("MainActivity.java", "onSave()");
//            bgroundSound.release();
            game.saveState(outState);
        }catch (Exception e){
        }
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        try{
            super.onRestoreInstanceState(savedInstanceState);
            //Bundle saved = getB
            Log.d("MainActivity.java", "onRestore()");
            //value = savedInstanceState.getInt(YourSurfaceViewClass.SOME_ID);
            //game.resume(1);
            //game = new GameView(this);
            game.restoreState(savedInstanceState);
            game.resume(1);
          //setContentView(game);
        }catch (Exception e){
        }
    }

    @Override
    protected void onPause() {
        Log.d("MainActivity.java", "onPause()");
        try {
            super.onPause();
            game.pause(1);
////            AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>(){
////                @Override
////                protected Void doInBackground(Void... voids) {
////                    game.pause();
//                    bgroundSound.release();
//                    return null;
//                }
//            };
//            task.execute();
            //bgroundSound.pause();
        }catch (Exception e){ }
    }

    @Override
    protected void onStop() {
        try{
        //Log.d("MainActivity.java", "onStop()");
        super.onStop();
        }catch(Exception e){
        }
    }

    @Override
    protected void onDestroy() {
        try{
        //Log.d("MainActivity.java", "onDestroy()");
        super.onDestroy();
        }catch(Exception e){

        }
    }

    @Override
    public void onBackPressed() {

    }

    public void callMe(boolean dead, long totalPoints, int round, int kills, int modeCheck) {

        bgroundSound.release();
        Intent intent = new Intent(this,MainActivity2.class);
        intent.putExtra("totalPoints", totalPoints);
        intent.putExtra("round", round);
        intent.putExtra("kills", kills);
        intent.putExtra("dead", dead);
        intent.putExtra("modeCheck", modeCheck);
        startActivity(intent);
        finish();
    }
}