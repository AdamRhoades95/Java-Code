package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.core.content.ContextCompat;

public class GameOver extends View {
    private Context context;
    private int x, y, againH, quitH;
    private Bitmap again, quit, gameOver;


    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GameOver(Context context, int x, int y) {
        super(context);
        this.context = context;
        this.x = x;
        this.y = y;
        //int color = ContextCompat.getColor(context, R.color.gameOver);
        again = BitmapFactory.decodeResource(getResources(),R.drawable.again);
        quit = BitmapFactory.decodeResource(getResources(),R.drawable.quit);
        gameOver = BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
        againH = ((y/2)-(again.getHeight()/2))-10;
        quitH = ((y/2)+(quit.getHeight()/2))+10;
        //gameOver.setWidth(x);
    }

    public void Draw(Canvas canvas) {
        String text = "Game Over";

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.gameOver);

        float textSize = 150;
        paint.setTextSize(textSize);


        int black = ContextCompat.getColor(context, R.color.black);

        paint.setColor(black);
        //canvas.drawR;
        canvas.drawRect(0,0,x,y,paint);

        paint.setColor(color);

        canvas.drawBitmap(gameOver, x/2- (gameOver.getWidth()/2), 25, paint);
        canvas.drawBitmap(again, x/2- (again.getWidth()/2), againH, null);
        canvas.drawBitmap(quit, x/2- (quit.getWidth()/2), quitH, null);
    }

    public void saveState(Bundle outState) {
        //x,y;
        outState.putInt("gameoverx", x);
        outState.putInt("gameovery", y);

    }

    public void restoreState(Bundle savedInstanceState) {

        x = savedInstanceState.getInt("gameoverx");
        y = savedInstanceState.getInt("gameovery");

        again = BitmapFactory.decodeResource(getResources(),R.drawable.again);
        quit = BitmapFactory.decodeResource(getResources(),R.drawable.quit);
        gameOver = BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
    }

    public int getAgainH() {
        return againH;
    }

    public int getQuitH() {
        return quitH;
    }

    public int getAgain() {
        return again.getHeight();
    }

    public int getQuit() {
        return quit.getHeight();
    }
}
