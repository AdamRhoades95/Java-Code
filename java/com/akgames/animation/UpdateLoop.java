package com.akgames.animation;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import static java.lang.Thread.sleep;

public class UpdateLoop extends Thread {
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

    UpdateLoop(GameView game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
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

        start();
    }
    @Override
    public void run(){
        try {
            super.run();
        }catch(Exception e){

        }



        Canvas canvas = null;
        startTime = System.currentTimeMillis();

        while(isRunning){
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    if(notPaused) {
                        game.update();
                        //game.draw(canvas);
                    }
                    updateCount++;


                }

            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }finally{
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            elapsedTime =System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount*UPS_PERIOD -elapsedTime);

            if (sleepTime > 0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(sleepTime < 0 && updateCount < MAX_UPS-1){
                if(notPaused) {

                    try {
                        game.update();
                    }catch(Exception e){

                    }
                }
                updateCount++;
                elapsedTime =System.currentTimeMillis()-startTime;
                sleepTime = (long) (updateCount*UPS_PERIOD -elapsedTime);
            }

            if (elapsedTime>=1000){
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
        //start();
    }
    void pauseLoop() {
        Log.d("GameLoop.java", "startLoop()");
        notPaused = false;
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
}
