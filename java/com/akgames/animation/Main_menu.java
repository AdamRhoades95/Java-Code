package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

public class Main_menu extends View {
    private Bitmap background, survival, arcade, instructions, credits, logout;
    private int x,y;
    private int black;
    private Paint paint;
    private int survivalH, arcadeH, instructionsH, creditsH;
    private int survivalW, arcadeW, instructionsW, creditsW;
    public Main_menu(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        //background = BitmapFactory.decodeResource(getResources(),R.drawable.brownground);
        survival = BitmapFactory.decodeResource(getResources(),R.drawable.survival);
        arcade = BitmapFactory.decodeResource(getResources(),R.drawable.arcade);
        instructions = BitmapFactory.decodeResource(getResources(),R.drawable.instructions);
        credits = BitmapFactory.decodeResource(getResources(),R.drawable.credits);
        logout = BitmapFactory.decodeResource(getResources(),R.drawable.logout);

        paint = new Paint();
        black = ContextCompat.getColor(context, R.color.black);

        survivalH = -220+(((y-credits.getHeight())-(instructions.getHeight()))-arcade.getHeight())-survival.getHeight();
        arcadeH = -190+((y-credits.getHeight())-(instructions.getHeight()))-arcade.getHeight();
        instructionsH = -170+(y-credits.getHeight())-instructions.getHeight();
        creditsH = y-credits.getHeight()-140;
         survivalW = x/2-(survival.getWidth()/2);
         arcadeW = x/2-(arcade.getWidth()/2);
         instructionsW = x/2-(instructions.getWidth()/2);
         creditsW = x/2-(credits.getWidth()/2);
    }

    public int getSurvivalH() {
        return survivalH;
    }

    public int getArcadeH() {
        return arcadeH;
    }

    public int getInstructionsH() {
        return instructionsH;
    }

    public int getCreditsH() {
        return creditsH;
    }

    public int getSurvivalW() {
        return survivalW;
    }

    public int getArcadeW() {
        return arcadeW;
    }

    public int getInstructionsW() {
        return instructionsW;
    }

    public int getCreditsW() {
        return creditsW;
    }

    public int getLogoutW() {
        return logout.getWidth()+5;
    }

    public int getLogoutH() {
        return logout.getHeight()+5;
    }

    public void Draw(Canvas canvas){
        paint.setColor(black);

        canvas.drawRect(0,0,x,y,paint);
        canvas.drawBitmap(credits , creditsW,creditsH,null);
        canvas.drawBitmap(logout , 5,5,null);
        canvas.drawBitmap(instructions , instructionsW,instructionsH,null);
        canvas.drawBitmap(arcade , arcadeW,arcadeH,null);
        canvas.drawBitmap(survival , survivalW,survivalH,null);
    }

    public void saveState(Bundle outState) {
        outState.putInt("x", x);
        outState.putInt("y", y);
        outState.putInt("black", black);
    }

    public void restoreState(Bundle savedInstanceState) {
        x = savedInstanceState.getInt("x");
        y = savedInstanceState.getInt("y");
        black = savedInstanceState.getInt("black");

        survival = BitmapFactory.decodeResource(getResources(),R.drawable.survival);
        arcade = BitmapFactory.decodeResource(getResources(),R.drawable.arcade);
        instructions = BitmapFactory.decodeResource(getResources(),R.drawable.instructions);
        credits = BitmapFactory.decodeResource(getResources(),R.drawable.credits);

    }
}
