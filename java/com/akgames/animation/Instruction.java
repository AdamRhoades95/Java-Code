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
import androidx.core.content.ContextCompat;

public class Instruction extends View {
    private int x, y;
    private int backW, backH, homeW, homeH, nextW, nextH;
    private Bitmap next,home,back, display, display2;
    private Context context;
    private int displaycode;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Instruction(Context context, int x, int y) {
        super(context);
        this.context = context;
        this.x = x;
        this.y = y;
        next = BitmapFactory.decodeResource(getResources(),R.drawable.next);
        home = BitmapFactory.decodeResource(getResources(),R.drawable.backtohome);
        back = BitmapFactory.decodeResource(getResources(),R.drawable.back);
        display = BitmapFactory.decodeResource(getResources(),R.drawable.describe_survival);
        display2 = BitmapFactory.decodeResource(getResources(),R.drawable.describe_arcade);
        nextH = 0;
        displaycode = 0;
        homeH = nextH;
        homeW = (x/2)-((home.getWidth()/2));
        backH = nextH;
        backW = homeW-(back.getWidth());
        nextW = homeW+home.getWidth();

        try {
            display.setHeight(y - (next.getHeight()+nextH));
            display2.setHeight(y - (next.getHeight()+nextH));
        }catch(Exception e){

        }
    }
    public void Draw (Canvas canvas) {
//        String text = "Game Over";
//
        Paint paint = new Paint();
//        int color = ContextCompat.getColor(context, R.color.gameOver);
//
//        float textSize = 150;
//        paint.setTextSize(textSize);
//
//

        int black = ContextCompat.getColor(context, R.color.black);
//
        paint.setColor(black);
        //canvas.drawR;
        canvas.drawRect(0, 0, x, y, paint);


        if (displaycode == 0){
            canvas.drawBitmap(display, (x/2)-(display.getWidth()/2), (next.getHeight()+nextH), null);
        }
        else if (displaycode == 1){
            canvas.drawBitmap(display2, (x/2)-(display2.getWidth()/2), (next.getHeight()+nextH), null);
        }
        canvas.drawBitmap(next, nextW, nextH, paint);
        canvas.drawBitmap(home, homeW, homeH, null);
        canvas.drawBitmap(back, backW, backH, null);
    }

    public void restoreState(Bundle savedInstanceState) {
        x = savedInstanceState.getInt("instructionx");
        y = savedInstanceState.getInt("instructiony");
        //menu = BitmapFactory.decodeResource(getResources(),R.drawable.menu);
    }

    public void saveState(Bundle outState) {
        outState.putInt("instructionx", x);
        outState.putInt("instructiony", y);
    }

    public void setDisplaycode(int displaycode) {
        this.displaycode = displaycode;
    }

    public int getBackW() {
        return backW;
    }

    public int getBackH() {
        return backH;
    }

    public int getHomeW() {
        return homeW;
    }

    public int getHomeH() {
        return homeH;
    }

    public int getNextW() {
        return nextW;
    }

    public int getNextH() {
        return nextH;
    }

    public int notBackW() {
        return back.getWidth();
    }

    public int notBackH() {
        return back.getHeight();
    }

    public int notHomeW() {
        return home.getWidth();
    }

    public int notHomeH() {
        return home.getHeight();
    }

    public int notNextW() {
        return next.getWidth();
    }

    public int notNextH() {
        return next.getHeight();
    }
}
