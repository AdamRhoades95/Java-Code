package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.core.content.ContextCompat;

import static java.lang.Math.atan;

public class Bullets extends View {
    public static final double SPEED_PIXELS_PER_SECOND = 800.0;
    private static float MAX_SPEED = (float)(SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS);
    private float velocityX, velocityY;
    private float slope;
    private float directionX;
    private float directionY;
    private float positionX, positionY;
    private Bitmap bullet;
    private float X,Y;
    private float dDegree;
    private float centerX, centerY;

    public Bullets(Context context, Player player, float X, float Y, Guns guns) {
        super(context);
        //positionX = guns.getGunX();
        //positionY = guns.getGunY();
        positionX = guns.getCenterX();
        positionY = guns.getCenterY();
        velocityX = X*MAX_SPEED;
        velocityY = Y*MAX_SPEED;
        bullet = BitmapFactory.decodeResource(getResources(),R.drawable.smbullet);
        this.dDegree = (float)player.getdDegree();
        slope = (float) atan(dDegree);

        //System.out.println("");
    }
    public Bullets(Context context, Player player, float X, float Y, Joystick joystick, Guns guns) {
        super(context);
        positionX = guns.getCenterX();//guns.getGunX()+(guns.getWidth()/2);//player.getPositionX()+25;
        positionY = guns.getCenterY();//guns.getGunY()-guns.getHeight();//player.getPositionY()+25;
        this.X = X;
        this.Y = Y;
        velocityX = X*MAX_SPEED;
        velocityY = Y*MAX_SPEED;

        dDegree = (float)player.getdDegree1();

        bullet = BitmapFactory.decodeResource(getResources(),R.drawable.smbullet);
        //this.dDegree = (float)joystick.getdDegree();
        if (velocityX != 0 || velocityY != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = (float)(velocityX/distance);
            directionY = (float)(velocityY/distance);
        }
//        if(directionX >0&&directionY<0) {
//            dDegree = (float)(-1 * Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360);//Math.toDegrees(Math.atan((directionX)/(directionY))));
//        }
//        else if(directionX <0&&directionY<0) {
//            dDegree = (float)(180-(Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
//        }
//        else if(directionX <0&&directionY>0) {
//            dDegree = (float)(180+(-1*Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
//        }
//        else if(directionX >0&&directionY>0) {
//            dDegree = (float)(360-(Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
//        }
        //player.setdDegree1(dDegree);
        dDegree = guns.getdDegree();
    }

    public float getPositionX(){
        return positionX;
    }
    public float getPositionY(){
        return positionY;
    }
    public void Draw (Canvas canvas, Guns guns){
        canvas.save();
        canvas.rotate(-1*dDegree,positionX,positionY);
        canvas.drawBitmap(bullet, positionX+20, positionY-(guns.getHeight()),null);
        canvas.restore();
    }

    public void Draw (Canvas canvas, int x){
//        String text = ""+ dDegree;
//
//        Paint paint = new Paint();
//        //int color = ContextCompat.getColor(context, R.color.gameOver);
//
//        float textSize = 30;
//        paint.setTextSize(textSize);
        canvas.save();
        canvas.rotate(-1*dDegree,positionX,positionY);
        canvas.drawBitmap(bullet, positionX, positionY,null);
        canvas.restore();
        //canvas.drawText(text,300,30, paint);
    }

    public void update (){
        positionX += velocityX;
        positionY += velocityY;
    }

}
