package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class FireBTN extends View {
    private int fireBTNPx, swapPx, reloadPx, fireBTNPy, swapPy, reloadPy;
    private DisplayMetrics displayMetrics;
    //private final int height,width;
    private Bitmap fireBTNSet, fireBTNGo,swap,reload,bag;
    private int positionX, positionY;
    private int fireBTNH, fireBTNW;
    private int swapW, swapH;
    private int reloadW, reloadH;

    public FireBTN(Context context, int positionX, int positionY) {
        super(context);
        displayMetrics = new DisplayMetrics();
//        ((Activity) getContext()).getWindowManager()
//                .getDefaultDisplay()
//                .getMetrics(displayMetrics);
//        this.height = displayMetrics.heightPixels;
//        this.width = displayMetrics.widthPixels;
        this.positionX = positionX;
        this.positionY = positionY;
        fireBTNSet = BitmapFactory.decodeResource(getResources(),R.drawable.fireready);
        //Bitmap fireBTNSet1 = Bitmap.createBitmap(fireBTNSet,positionX-190,positionY-180,150,100);
        fireBTNGo = BitmapFactory.decodeResource(getResources(),R.drawable.fired);
        swap = BitmapFactory.decodeResource(getResources(),R.drawable.gunswap60);
        //bag = BitmapFactory.decodeResource(getResources(),R.drawable.bag60);
        reload = BitmapFactory.decodeResource(getResources(),R.drawable.reload60);

        fireBTNH = fireBTNSet.getHeight();
        fireBTNW = fireBTNSet.getWidth();
        swapH = swap.getHeight();
        swapW = swap.getWidth();
        reloadH = reload.getHeight();
        reloadW = reload.getWidth();

        fireBTNPx = positionX-fireBTNW;
        fireBTNPy =  positionY-(fireBTNH+80);

        swapPx = positionX-(swapW);
        swapPy = fireBTNPy - 10 - swapH;

        reloadPx = positionX - (20+swapW+reloadW);
        reloadPy = fireBTNPy - 10 - reloadH;
    }

    public int getFireBTNH() {
        return fireBTNH;
    }

    public int getFireBTNW() {
        return fireBTNW;
    }

    public int getSwapW() {
        return swapW;
    }

    public int getSwapH() {
        return swapH;
    }

    public int getReloadW() {
        return reloadW;
    }

    public int getReloadH() {
        return reloadH;
    }

    public int getFireBTNPx() {
        return fireBTNPx;
    }

    public void setFireBTNPx(int fireBTNPx) {
        this.fireBTNPx = fireBTNPx;
    }

    public int getSwapPx() {
        return swapPx;
    }

    public void setSwapPx(int swapPx) {
        this.swapPx = swapPx;
    }

    public int getReloadPx() {
        return reloadPx;
    }

    public void setReloadPx(int reloadPx) {
        this.reloadPx = reloadPx;
    }

    public int getFireBTNPy() {
        return fireBTNPy;
    }

    public void setFireBTNPy(int fireBTNPy) {
        this.fireBTNPy = fireBTNPy;
    }

    public int getSwapPy() {
        return swapPy;
    }

    public void setSwapPy(int swapPy) {
        this.swapPy = swapPy;
    }

    public int getReloadPy() {
        return reloadPy;
    }

    public void setReloadPy(int reloadPy) {
        this.reloadPy = reloadPy;
    }

    public void Draw(Canvas canvas, int check) {
        super.draw(canvas);
        if(check == 0) {
            canvas.drawBitmap(fireBTNSet, fireBTNPx, fireBTNPy, null);
        }
        else{
            canvas.drawBitmap(fireBTNGo, fireBTNPx, fireBTNPy, null);
        }
        canvas.drawBitmap(swap, swapPx, swapPy, null);
        canvas.drawBitmap(reload, reloadPx, reloadPy, null);
        //canvas.drawBitmap(bag, positionX-320, positionY-180, null);
    }

    public void restoreState(Bundle savedInstanceState) {
        displayMetrics = new DisplayMetrics();
//        ((Activity) getContext()).getWindowManager()
//                .getDefaultDisplay()
//                .getMetrics(displayMetrics);
//        this.height = displayMetrics.heightPixels;
//        this.width = displayMetrics.widthPixels;
        this.positionX = positionX;
        this.positionY = positionY;
        fireBTNSet = BitmapFactory.decodeResource(getResources(),R.drawable.fireready);
        //Bitmap fireBTNSet1 = Bitmap.createBitmap(fireBTNSet,positionX-190,positionY-180,150,100);
        fireBTNGo = BitmapFactory.decodeResource(getResources(),R.drawable.fired);
        swap = BitmapFactory.decodeResource(getResources(),R.drawable.gunswap60);
        //bag = BitmapFactory.decodeResource(getResources(),R.drawable.bag60);
        reload = BitmapFactory.decodeResource(getResources(),R.drawable.reload60);

        fireBTNH = fireBTNSet.getHeight();
        fireBTNW = fireBTNSet.getWidth();
        swapH = swap.getHeight();
        swapW = swap.getWidth();
        reloadH = reload.getHeight();
        reloadW = reload.getWidth();
    }
}
