package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

public class Background extends View {

    Bitmap background;

    public Background(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(),R.drawable.slatea1);
    }


    public void Draw(Canvas canvas) {

        canvas.drawBitmap(background,0,0,null);
    }

    public void saveState(Bundle outState) {
    }

    public void restoreState(Bundle savedInstanceState) {
        background = BitmapFactory.decodeResource(getResources(),R.drawable.slatea1);
    }
}
