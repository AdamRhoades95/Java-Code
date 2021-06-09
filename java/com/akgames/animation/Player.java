package com.akgames.animation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import static android.content.Context.MODE_PRIVATE;

public class Player extends View {
    public static double SPEED_PIXELS_PER_SECOND = 400.0;
    private static double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    public static int MAX_HEALTH_POINTS = 5;
    private Joystick joystick, joystick2;
    private Bitmap playerIMGStationary,playerIMGRBackMoving,playerIMGLBackMoving;
    private HealthBar healthBar;
    private int healthPoints = MAX_HEALTH_POINTS;
    private double velocityX, velocityY, velocityX2, velocityY2;
    private float positionX, positionY, directionX, directionY;
    private double dDegree;
    private int drawCheck,width,height;
    private double dRadian;
    private double dDegree1;
    private float centerX, centerY;

    public Player(Context context, Joystick joystick, float positionX, float positionY) {
        super(context);
        this.width = (int)positionX;
        this.height = (int)positionY;
        height -= 100;
        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        playerIMGStationary = BitmapFactory.decodeResource(getResources(),R.drawable.playerstationary);
        playerIMGRBackMoving = BitmapFactory.decodeResource(getResources(),R.drawable.playerrbmoving);
        playerIMGLBackMoving = BitmapFactory.decodeResource(getResources(),R.drawable.playerlbmoving);
        this.positionX = positionX/2-(playerIMGStationary.getWidth()/2);
        this.positionY = positionY/2-(playerIMGStationary.getHeight()/2);

        centerX = positionX+(playerIMGStationary.getWidth()/2);
        centerY = positionY+(playerIMGStationary.getHeight()/2);
        drawCheck = 0;
        dDegree1 = 0;
    }

    public Player(Context context, Joystick joystick, Joystick joystick2, float positionX, float positionY) {
        super(context);
        this.width = (int)positionX;
        this.height = (int)positionY;
        height -= 100;

        this.joystick = joystick;
        this.joystick2 = joystick2;
        this.healthBar = new HealthBar(context, this);
        playerIMGStationary = BitmapFactory.decodeResource(getResources(),R.drawable.playerstationary);
        playerIMGRBackMoving = BitmapFactory.decodeResource(getResources(),R.drawable.playerrbmoving);
        playerIMGLBackMoving = BitmapFactory.decodeResource(getResources(),R.drawable.playerlbmoving);
        this.positionX = positionX/2-(playerIMGStationary.getWidth()/2);
        this.positionY = positionY/2-(playerIMGStationary.getHeight()/2);

        centerX = positionX+(playerIMGStationary.getWidth()/2);
        centerY = positionY+(playerIMGStationary.getHeight()/2);
        drawCheck = 0;
        dDegree1 = 0;
    }

    public void update(int x) {

        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

        velocityX2 = joystick2.getActuatorX()*MAX_SPEED;
        velocityY2 = joystick2.getActuatorY()*MAX_SPEED;
//        System.out.println("velocityX2  "+ velocityX2);
//        System.out.println("velocityY2  "+ velocityY2);
        if(joystick2.getActuatorX()!=0&&joystick2.getActuatorY()!=0) {
            double dSlope =  (0-(joystick2.getActuatorX()-(width-300)))/(0 -joystick2.getActuatorY()); // Let the slope be 1: (0,0), (1,1)
            dRadian = -1*Math.atan(dSlope)-90;
            dDegree1 = Math.toDegrees(dRadian);
            //System.out.println(dDegree1);

        }

        // Update position

        positionY += velocityY;
        positionX += velocityX;

        centerX = positionX+(playerIMGStationary.getWidth()/2);
        centerY = positionY+(playerIMGStationary.getHeight()/2);

        if(positionX > width-30){
            positionX = width-30;
        }
        else if(positionX<0){
            positionX = 0;
        }

        if(positionY > height){
            positionY = height;
        }
        else if(positionY < 0){
            positionY = 0;
        }

        // Update direction
        if (velocityX2 != 0 || velocityY2 != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX2, velocityY2);
            directionX = (float)(velocityX2/distance);
            directionY = (float)(velocityY2/distance);
        }
        if(directionX >0&&directionY<0) {
            dDegree1 = (-1 * Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360);//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }
        else if(directionX <0&&directionY<0) {
            dDegree1 = (180-(Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }
        else if(directionX <0&&directionY>0) {
            dDegree1 = (180+(-1*Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }
        else if(directionX >0&&directionY>0) {
            dDegree1 = (360-(Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }

        //System.out.println(dDegree1);

    }

    public void update() {

        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;
//        System.out.println("velocityX2  "+ velocityX);
//        System.out.println("velocityY2  "+ velocityY);
        if(joystick.getActuatorX()!=0&&joystick.getActuatorY()!=0) {
            double dSlope =  (0-joystick.getActuatorX())/(0 -joystick.getActuatorY()); // Let the slope be 1: (0,0), (1,1)
            dRadian = -1*Math.atan(dSlope)-90;
            dDegree = Math.toDegrees(dRadian);
            //System.out.println(dDegree);

        }

        // Update position

        positionY += velocityY;
        positionX += velocityX;

        centerX = positionX+(playerIMGStationary.getWidth()/2);
        centerY = positionY+(playerIMGStationary.getHeight()/2);

        if(positionX > width-30){
            positionX = width-30;
        }
        else if(positionX<0){
            positionX = 0;
        }

        if(positionY > height){
            positionY = height;
        }
        else if(positionY < 0){
            positionY = 0;
        }

        // Update direction
        if (velocityX != 0 || velocityY != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = (float)(velocityX/distance);
            directionY = (float)(velocityY/distance);
        }
        if(directionX >0&&directionY<0) {
            dDegree = (-1 * Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360);//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }
        else if(directionX <0&&directionY<0) {
            dDegree = (180-(Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }
        else if(directionX <0&&directionY>0) {
            dDegree = (180+(-1*Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }
        else if(directionX >0&&directionY>0) {
             dDegree = (360-(Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }

    }


    public void reset(){
        dDegree = 0;
        dDegree1 = 0;
        velocityY = 0;
        velocityX = 0;
    }

    public void Draw(Canvas canvas, Context context, int x) {
        super.draw(canvas);
        //canvas.rotate(60,positionX,positionY);

        String text = "Health";

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.gameOver);

        float textSize = 30;
        paint.setTextSize(textSize);


        canvas.save();

        canvas.rotate((float)(-1*dDegree1+90), centerX, centerY);
        if(velocityX == 0 && velocityY == 0) {
            canvas.drawBitmap(playerIMGStationary, positionX, positionY, null);
            drawCheck = 0;
        }
        else{
            if(drawCheck<10){
                canvas.drawBitmap(playerIMGLBackMoving, positionX, positionY, null);
                drawCheck++;
            }
            else{
                canvas.drawBitmap(playerIMGRBackMoving, positionX, positionY, null);
                if(drawCheck>=20){
                    drawCheck = 1;
                }
                drawCheck++;
            }
        }
        canvas.restore();
        canvas.drawText(text,10,30, paint);
        healthBar.draw(canvas,155,60);
    }

    public void Draw(Canvas canvas, Context context) {
        super.draw(canvas);
        //canvas.rotate(60,positionX,positionY);

        String text = "Health";

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.gameOver);

        float textSize = 30;
        paint.setTextSize(textSize);


        canvas.save();

        canvas.rotate((float)(-1*dDegree+90), centerX,centerY);
        if(velocityX == 0 && velocityY == 0) {
            canvas.drawBitmap(playerIMGStationary, positionX, positionY, null);
            drawCheck = 0;
        }
        else{
            if(drawCheck<10){
                canvas.drawBitmap(playerIMGLBackMoving, positionX, positionY, null);
                drawCheck++;
            }
            else{
                canvas.drawBitmap(playerIMGRBackMoving, positionX, positionY, null);
                if(drawCheck>=20){
                    drawCheck = 1;
                }
                drawCheck++;
            }
        }
        canvas.restore();
        canvas.drawText(text,10,30, paint);
        healthBar.draw(canvas,155,60);
    }

    public float getPositionY() {
        return positionY;
    }
    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public int getHealthPoint() {
        return healthPoints;
    }

    public void setHealthPoint(int healthPoints) {
        // Only allow positive values
        if (healthPoints >= 0)
            this.healthPoints = healthPoints;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public int width(){
        return playerIMGStationary.getWidth();
    }
    public int height(){
        return playerIMGStationary.getHeight();
    }

    public double getdDegree1(){
        return dDegree1;
    }

    public void setdDegree1(double dDegree1) {
        this.dDegree1 = dDegree1;
    }

    public double getdDegree(){
        return dDegree;
    }

    public void saveState() {
//        private SharedPreferences sharedPreferences;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        SharedPreferences.Editor outState = sharedPreferences.edit();
        outState.putFloat("SPEED_PIXELS_PER_SECOND",  (float)SPEED_PIXELS_PER_SECOND);
        outState.putFloat("MAX_SPEED", (float) MAX_SPEED);
        outState.putFloat("velocityX", (float)velocityX);
        outState.putFloat("velocityY", (float)velocityY);
        outState.putFloat("dDegree", (float)dDegree);
        outState.putFloat("dDegree1", (float)dDegree1);
        outState.putFloat("dRadian", (float)dRadian);
        outState.putInt("MAX_HEALTH_POINTS", MAX_HEALTH_POINTS);
        outState.putInt("healthPoints", healthPoints);
        outState.putInt("drawCheck", drawCheck);
        outState.putFloat("positionX", positionX);
        outState.putFloat("positionY", positionY);
        outState.putFloat("directionX", directionX);
        outState.putFloat("directionY", directionY);
        outState.putFloat("width", width);
        outState.putFloat("height", height);
        healthBar.saveState();
        outState.apply();
        //private Joystick joystick;
        //private Bitmap playerIMGStationary,playerIMGRBackMoving,playerIMGLBackMoving;
    }

    public void restoreState(int width, int height) {
        SharedPreferences savedInstanceState = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        //SharedPreferences.Editor outState = sharedPreferences.edit();
        playerIMGStationary = BitmapFactory.decodeResource(getResources(),R.drawable.playerstationary);
        playerIMGRBackMoving = BitmapFactory.decodeResource(getResources(),R.drawable.playerrbmoving);
        playerIMGLBackMoving = BitmapFactory.decodeResource(getResources(),R.drawable.playerlbmoving);
        SPEED_PIXELS_PER_SECOND = (double)savedInstanceState.getFloat("SPEED_PIXELS_PER_SECOND",400);
        MAX_SPEED = (double)savedInstanceState.getFloat("MAX_SPEED",(float)(SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS));
        velocityX = (double)savedInstanceState.getFloat("velocityX",0);
        velocityY = (double)savedInstanceState.getFloat("velocityY",0);
        dDegree = (double)savedInstanceState.getFloat("dDegree",0);
        dDegree1 = (double)savedInstanceState.getFloat("dDegree1",0);
        dRadian = (double)savedInstanceState.getFloat("dRadian",0);
        MAX_HEALTH_POINTS = savedInstanceState.getInt("MAX_HEALTH_POINTS", 5);
        healthPoints = savedInstanceState.getInt("healthPoints", 5);
        drawCheck = savedInstanceState.getInt("drawCheck",0);
        positionX = savedInstanceState.getFloat("positionX", 200);
        positionY = savedInstanceState.getFloat("positionY",200);
        directionX = savedInstanceState.getFloat("directionX",0);
        directionY = savedInstanceState.getFloat("directionY",0);
        width = savedInstanceState.getInt("width", width);
        height = savedInstanceState.getInt("height", height);
        healthBar.restoreState();
    }

    public void saveState(Bundle outState) {
        outState.putDouble("SPEED_PIXELS_PER_SECOND", SPEED_PIXELS_PER_SECOND);
        outState.putDouble("MAX_SPEED", MAX_SPEED);
        outState.putDouble("velocityX", velocityX);
        outState.putDouble("velocityY", velocityY);
        outState.putDouble("dDegree", dDegree);
        outState.putDouble("dDegree1", dDegree1);
        outState.putDouble("dRadian", dRadian);
        outState.putInt("MAX_HEALTH_POINTS", MAX_HEALTH_POINTS);
        outState.putInt("healthPoints", healthPoints);
        outState.putInt("drawCheck", drawCheck);
        outState.putFloat("positionX", positionX);
        outState.putFloat("positionY", positionY);
        outState.putFloat("directionX", directionX);
        outState.putFloat("directionY", directionY);
        outState.putFloat("width", width);
        outState.putFloat("height", height);
        healthBar.saveState(outState);
        //private Joystick joystick;
        //private Bitmap playerIMGStationary,playerIMGRBackMoving,playerIMGLBackMoving;
    }

    public void restoreState(Bundle savedInstanceState) {
        SPEED_PIXELS_PER_SECOND = savedInstanceState.getDouble("SPEED_PIXELS_PER_SECOND");
        MAX_SPEED = savedInstanceState.getDouble("MAX_SPEED");
        velocityX = savedInstanceState.getDouble("velocityX");
        velocityY = savedInstanceState.getDouble("velocityY");
        dDegree = savedInstanceState.getDouble("dDegree");
        dDegree1 = savedInstanceState.getDouble("dDegree1");
        dRadian = savedInstanceState.getDouble("dRadian");
        MAX_HEALTH_POINTS = savedInstanceState.getInt("MAX_HEALTH_POINTS");
        healthPoints = savedInstanceState.getInt("healthPoints");
        drawCheck = savedInstanceState.getInt("drawCheck");
        positionX = savedInstanceState.getFloat("positionX");
        positionY = savedInstanceState.getFloat("positionY");
        directionX = savedInstanceState.getFloat("directionX");
        directionY = savedInstanceState.getFloat("directionY");
        width = savedInstanceState.getInt("width");
        height = savedInstanceState.getInt("height");
        healthBar.restoreState(savedInstanceState);

        playerIMGStationary = BitmapFactory.decodeResource(getResources(),R.drawable.playerstationary);
        playerIMGRBackMoving = BitmapFactory.decodeResource(getResources(),R.drawable.playerrbmoving);
        playerIMGLBackMoving = BitmapFactory.decodeResource(getResources(),R.drawable.playerlbmoving);
    }
}

