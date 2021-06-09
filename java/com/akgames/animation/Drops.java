package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import java.util.Random;

public class Drops extends View {
    private Bitmap perk;
    private float x,y;
    private int perkT, time, blink;

    public Drops(Context context, Enemy enemy){
        super(context);
        x = enemy.getPositionX();
        y = enemy.getPositionY();
        Random rand = new Random();
        int deside = rand.nextInt(5);
        time = 360;
        blink = 0;
        if(deside <= 1) {
            perkT = 1;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.insta20w);
        }
        else if(deside == 2) {
            perkT = 2;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.nuke);
        }
        else if(deside >= 3) {
            perkT = 3;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.unlim30w);
        }
    }

    public Drops(Context context, Enemy enemy, int t){
        super(context);
        x = enemy.getPositionX();
        y = enemy.getPositionY();
        Random rand = new Random();
        int deside = rand.nextInt(39);
        time = 360;
        blink = 0;
        if(deside > 33 && deside<=38) {
            perkT = 1;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.insta20w);
        }
        else if(deside > 28 && deside<=33) {
            perkT = 2;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.nuke);
        }
        else if(deside > 26 && deside<=28) {
            perkT = 3;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.akperk);
        }
        else if(deside > 24 && deside<=26) {
            perkT = 4;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.uziperk);
        }
        else if(deside > 22 && deside<=24) {
            perkT = 5;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.revperk);
        }
        else if(deside > 20 && deside<=22) {
            perkT = 6;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.shotgunperk);
        }
        else if(deside > 15 && deside<=18) {
            perkT = 7;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.healperk);
        }
        else if(deside > 5 && deside<=15) {
            perkT = 8;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.healthupperk);
        }
        else if(deside <= 5) {
            perkT = 9;
            perk = BitmapFactory.decodeResource(getResources(), R.drawable.damageperk);
        }

    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public int getPerkT() {
        return perkT;
    }

    public int getTime() {
        return time;
    }

    public void Draw (Canvas canvas){
        time--;
        if(time%60 == 0){
            blink += 8;
        }
        if(time%60 > blink) {
            canvas.drawBitmap(perk, x + 10, y + 10, null);
        }
    }
}
