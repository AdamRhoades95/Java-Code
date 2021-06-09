package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class Joystick extends View {
    private int outerCircleCenterPositionX;
    private int outerCircleCenterPositionY;
    private int innerCircleCenterPositionX;
    private int innerCircleCenterPositionY;

    private int outerCircleRadius;
    private int innerCircleRadius;
    private Bitmap innerCircle;
    private Paint innerCirclePaint;
    private Paint outerCirclePaint;
    private boolean isPressed = false;
    private double joystickCenterToTouchDistance;
    private double actuatorX;
    private double actuatorY;
    private double dRadian,dDegree;
    private double deltaX;
    private double deltaY;
    private double deltaDistance;
    double dSlope;

    public Joystick(Context context, int centerPositionX, int centerPositionY, int outerCircleRadius) {
        super(context);
        innerCircle = BitmapFactory.decodeResource(getResources(),R.drawable.joystick1);
        outerCircleRadius = (innerCircle.getHeight()/2)+10;
        // Outer and inner circle make up the joystick
        outerCircleCenterPositionX = centerPositionX+180;//-outerCircleRadius;
        outerCircleCenterPositionY = centerPositionY-(170+outerCircleRadius);//-outerCircleRadius;
        innerCircleCenterPositionX = centerPositionX+170;
        innerCircleCenterPositionY = centerPositionY-(170+outerCircleRadius);//-(innerCircle.getHeight()+100);


        // Radius of outer of circle
        this.outerCircleRadius = outerCircleRadius;

        // paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);


    }

    public void Draw(Canvas canvas) {
        super.draw(canvas);
        // Draw outer circle
//        canvas.drawCircle(
//                outerCircleCenterPositionX,
//                outerCircleCenterPositionY,
//                outerCircleRadius,
//                outerCirclePaint
//        );

        // Draw inner circle
        canvas.drawBitmap(innerCircle,innerCircleCenterPositionX,innerCircleCenterPositionY,null);

    }

    public double getdDegree(){
        if(this.getActuatorX()!=0&&this.getActuatorY()!=0) {
            dSlope =  (0-this.getActuatorX())/(0 -this.getActuatorY()); // Let the slope be 1: (0,0), (1,1)
            dRadian = -1*Math.atan(dSlope)-90;
            dDegree = Math.toDegrees(dRadian);

        }
        else {
            //dDegree = 0;
        }
        return dDegree;
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX = (int) (outerCircleCenterPositionX + actuatorX*outerCircleRadius)-(innerCircle.getWidth()/2);
        innerCircleCenterPositionY = (int) (outerCircleCenterPositionY + actuatorY*outerCircleRadius)-(innerCircle.getHeight()/2);
    }

    public void setActuator(double touchPositionX, double touchPositionY) {
        deltaX = touchPositionX - outerCircleCenterPositionX;
        deltaY = touchPositionY - outerCircleCenterPositionY;
        deltaDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if(deltaDistance < outerCircleRadius) {
            actuatorX = deltaX/outerCircleRadius;
            actuatorY = deltaY/outerCircleRadius;
        } else {
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }
    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        joystickCenterToTouchDistance = Math.sqrt(
                Math.pow(outerCircleCenterPositionX - touchPositionX, 2) +
                        Math.pow(outerCircleCenterPositionY - touchPositionY, 2)
        );
        return joystickCenterToTouchDistance < outerCircleRadius;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }

    public void resetActuator() {
        actuatorX = 0;
        actuatorY = 0;
    }

    public void saveState(Bundle outState) {
        outState.putInt("outerCircleCenterPositionX", outerCircleCenterPositionX);
        outState.putInt("outerCircleCenterPositionY", outerCircleCenterPositionY);
        outState.putInt("innerCircleCenterPositionX", innerCircleCenterPositionX);
        outState.putInt("innerCircleCenterPositionY", innerCircleCenterPositionY);
        outState.putInt("outerCircleRadius", outerCircleRadius);
        outState.putInt("innerCircleRadius", innerCircleRadius);
        outState.putBoolean("isPressed", isPressed);
        outState.putDouble("joystickCenterToTouchDistance", joystickCenterToTouchDistance);
        outState.putDouble("actuatorX", actuatorX);
        outState.putDouble("actuatorY", actuatorY);
        outState.putDouble("dRadian", dRadian);
        outState.putDouble("dDegree", dDegree);
        outState.putDouble("deltaX", deltaX);
        outState.putDouble("deltaY", deltaY);
        outState.putDouble("deltaDistance", deltaDistance);
        outState.putDouble("dSlope", dSlope);
//        private Bitmap innerCircle;
//        private Paint innerCirclePaint;
//        private Paint outerCirclePaint;
    }

    public void restoreState(Bundle savedInstanceState) {
        outerCircleCenterPositionX = savedInstanceState.getInt("outerCircleCenterPositionX");
        outerCircleCenterPositionY = savedInstanceState.getInt("outerCircleCenterPositionY");
        innerCircleCenterPositionX = savedInstanceState.getInt("innerCircleCenterPositionX");
        innerCircleCenterPositionY = savedInstanceState.getInt("innerCircleCenterPositionY");
        outerCircleRadius = savedInstanceState.getInt("outerCircleRadius");
        innerCircleRadius = savedInstanceState.getInt("innerCircleRadius");
        isPressed = savedInstanceState.getBoolean("isPressed");
        joystickCenterToTouchDistance = savedInstanceState.getDouble("joystickCenterToTouchDistance");
        actuatorX = savedInstanceState.getDouble("actuatorX");
        actuatorY = savedInstanceState.getDouble("actuatorY");
        dRadian = savedInstanceState.getDouble("dRadian");
        dDegree = savedInstanceState.getDouble("dDegree");
        deltaX = savedInstanceState.getDouble("deltaX");
        deltaY = savedInstanceState.getDouble("deltaY");
        deltaDistance = savedInstanceState.getDouble("deltaDistance");
        dSlope = savedInstanceState.getDouble("dSlope");



    }
}

