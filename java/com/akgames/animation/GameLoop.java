package com.akgames.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.interstitial.InterstitialAd;





@SuppressWarnings("ALL")
public class GameLoop extends Thread {
    private InterstitialAd interstitialAd;
    static double MAX_UPS = 59.0;
    private static double UPS_PERIOD = 1E+3/MAX_UPS;
    private GameView game;
    private boolean isRunning;
    private SurfaceHolder surfaceHolder;
    private double averageUPS;
    private double averageFPS;

    private int updateCount = 0;
    private int frameCount = 0;

    private long startTime;
    private long elapsedTime;
    private long sleepTime;
    private boolean notPaused = true;
    private boolean check;
    //InterstitialAd interstitialAd;
    //private InterstitialAd mInterstitialAd;

    GameLoop(GameView game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
        notPaused = true;
        check = false;

try{
//        InterstitialAd interstitialAd = new InterstitialAd(game.getContext());
//        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//ca-app-pub-8676849276961960/4419861001");
//    AdRequest request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//        interstitialAd.loadAd(request);
}catch (Exception e){
System.out.println(e);
}
    }

    double getAverageUPS() {
        return averageUPS;
    }

    double getAverageFPS() {
        return averageFPS;
    }

    void startLoop() {
        Log.d("GameLoop.java", "startLoop()");
        isRunning = true;
        notPaused = true;
            start();
    }
    @Override
    public void run() {
        try {
            super.run();
        } catch (Exception e) {

        }


        Canvas canvas = null;
        startTime = System.currentTimeMillis();
            while (isRunning) {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        if (notPaused) {
                            game.update();
                            game.draw(canvas);
                        }
                        updateCount++;


                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                            frameCount++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);

                if (sleepTime > 0) {
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                    if (notPaused) {
                        try {
                            game.update();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    updateCount++;
                    elapsedTime = System.currentTimeMillis() - startTime;
                    sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
                }

                if (elapsedTime >= 1000) {
                    averageUPS = updateCount / (1E-3 * elapsedTime);
                    averageFPS = frameCount / (1E-3 * elapsedTime);
                    updateCount = 0;
                    frameCount = 0;
                    startTime = System.currentTimeMillis();
                }
            }

    }



    void resumeLoop() {
        Log.d("GameLoop.java", "startLoop()");
        notPaused = true;
        isRunning = true;
        //start();
    }

    public void setCheck(boolean check){
        this.check = check;
    }
    public void runAd(Context context){
        notPaused = false;
        isRunning = false;


        try{
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId("ca-app-pub-8676849276961960/4419861001");
        }catch (Exception e){

        }
        //interstitialAd.
        try{
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }catch (Exception e){

        }
        if(interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
        else{
            interstitialAd.setAdListener( new AdListener() {
                @Override
                public void onAdLoaded() {
                    interstitialAd.show();
                    // Showing a simple Toast message to user when an ad is loaded
                    //Toast.makeText ( MainActivity.this, "Interstitial Ad is Loaded", Toast.LENGTH_LONG).show() ;
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    super.onAdClosed();
//                    Intent intent = new Intent(MainActivity2.this,MainActivity.class);
//                    intent.putExtra("totalPoints", totalPoints);
//                    intent.putExtra("round", round);
//                    intent.putExtra("kills", kills);
//                    intent.putExtra("dead", dead);
//                    intent.putExtra("modeCheck", modeCheck);
//                    startActivity(intent);
//                    finish();
                    // Showing a simple Toast message to user when and ad is failed to load
                    //Toast.makeText ( MainActivity2.this, "Interstitial Ad Failed to Load ", Toast.LENGTH_LONG).show() ;
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
//                    Intent intent = new Intent(MainActivity2.this,MainActivity.class);
//                    intent.putExtra("totalPoints", totalPoints);
//                    intent.putExtra("round", round);
//                    intent.putExtra("kills", kills);
//                    intent.putExtra("dead", dead);
//                    intent.putExtra("modeCheck", modeCheck);
//                    startActivity(intent);
//                    finish();
                }
            });
        }


    }
    void pauseLoop() {
        Log.d("GameLoop.java", "pauseLoop()");
        notPaused = false;
        isRunning = false;

        //runAd();


        //start();
    }

    public void saveState(Bundle outState) {
        outState.putInt("updateCount", updateCount);
        outState.putInt("frameCount", frameCount);
        outState.putLong("startTime", startTime);
        outState.putLong("elapsedTime", elapsedTime);
        outState.putLong("sleepTime", sleepTime);
        outState.putBoolean("isRunning", isRunning);
        outState.putDouble("MAX_UPS", MAX_UPS);
        outState.putDouble("UPS_PERIOD", UPS_PERIOD);
        outState.putDouble("averageUPS", averageUPS);
        outState.putDouble("averageFPS", averageFPS);
    }

    public void restoreState(Bundle savedInstanceState, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        updateCount =  savedInstanceState.getInt("updateCount");
        frameCount =  savedInstanceState.getInt("frameCount");
        startTime =  savedInstanceState.getLong("startTime");
        elapsedTime =  savedInstanceState.getLong("elapsedTime");
        sleepTime =  savedInstanceState.getLong("sleepTime");
        isRunning =  savedInstanceState.getBoolean("isRunning");
        MAX_UPS =  savedInstanceState.getDouble("MAX_UPS");
        UPS_PERIOD =  savedInstanceState.getDouble("UPS_PERIOD");
        averageUPS =  savedInstanceState.getDouble("averageUPS");
        averageFPS =  savedInstanceState.getDouble("averageFPS");
        isRunning = true;
        startLoop();
    }

    public boolean getvalue() {
        return  check;
    }
}
