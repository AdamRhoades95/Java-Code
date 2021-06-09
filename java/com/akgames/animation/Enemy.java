package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import java.util.Random;

import androidx.core.content.ContextCompat;

public class Enemy extends View {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND*0.6;
    private static final double MAX_SPEED = (SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS)-2;
    private static final double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/80.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private Player player;
    private Bitmap enemyLeft, enemyRight;
    private Canvas canvas;
    private float positionX, positionY;
    private double velocityX, velocityY, dDegree;
    private boolean starter;
    private float health;
    // create instance of Random class
    private Random rand = new Random();
    private int drawCheck;

    // Generate random integers in range 0 to 999


    public Enemy(Context context, Player player, int round) {
        super(context);
        starter = true;
        enemyLeft = BitmapFactory.decodeResource(getResources(),R.drawable.zombieleft);
        enemyRight = BitmapFactory.decodeResource(getResources(),R.drawable.zombieright);
        this.player = player;
        health = round;
        this.dDegree = (float)(player.getdDegree());
        drawCheck = 1;
    }
    public Enemy(Context context, Player player, float round) {
        super(context);
        starter = true;
        enemyLeft = BitmapFactory.decodeResource(getResources(),R.drawable.zombieleft);
        enemyRight = BitmapFactory.decodeResource(getResources(),R.drawable.zombieright);
        this.player = player;
        health = round;
        this.dDegree = (float)(player.getdDegree());
        drawCheck = 1;
    }

    public void setHealth(float health){
        this.health = (float) health;
    }

    public float getHealth(){
        return health;
    }

    public void Draw(Canvas canvas) {
        if(starter){
            starter = false;
            int intX = rand.nextInt(4);
            if(intX == 0){
                positionX = (float)(Math.random()*canvas.getWidth());
                positionY = -100;
            }
            else if (intX == 1){
                positionY = (float)(Math.random()*canvas.getHeight());
                positionX = canvas.getWidth()+100;
            }

            else if(intX == 2){
                positionX = (float)(Math.random()*canvas.getWidth());
                positionY = canvas.getHeight()+100;
            }
            else if (intX == 3){
                positionY = (float)(Math.random()*canvas.getHeight());
                positionX = -100;
            }
        }
        canvas.save();
        canvas.rotate((float)(-1*dDegree+90),positionX+30,positionY+30);
        if(drawCheck<10){
            canvas.drawBitmap(enemyRight, positionX-25, positionY-25, null);
            drawCheck++;
        }
        else{
            canvas.drawBitmap(enemyLeft, positionX-25, positionY-25, null);
            if(drawCheck>=20){
                drawCheck = 1;
            }
            drawCheck++;
        }
        //canvas.drawBitmap(enemy, positionX, positionY,null);
        canvas.restore();
    }



    public static boolean readyToSpawn(int round) {
        //Math.random() >= .99
        if (round>10){
            round = 10;
        }
        if(updatesUntilNextSpawn-(round*10)>150){
            updatesUntilNextSpawn = 150;
        }

        //System.out.println(updatesUntilNextSpawn-(round*10));
        if (updatesUntilNextSpawn-(round*10) <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn --;
            return false;
        }
    }
    public static boolean readyToSpawn(float round) {
        //Math.random() >= .99
        if (round>10){
            round = 10;
        }
        if(updatesUntilNextSpawn-(round*10)>150){
            updatesUntilNextSpawn = 150;
        }

        //System.out.println(updatesUntilNextSpawn-(round*10));
        if (updatesUntilNextSpawn-(round*10) <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn --;
            return false;
        }
    }

    public static boolean readyToSpawn(float round, int x) {
        //Math.random() >= .99
        if (round>15){
            round = 15;
        }
        if(updatesUntilNextSpawn-(round*10)>200){
            updatesUntilNextSpawn = 200;
        }

        //System.out.println(updatesUntilNextSpawn-(round*10));
        if (updatesUntilNextSpawn-(round*10) <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn --;
            return false;
        }
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void update() {
        // =========================================================================================
        //   Update velocity of the enemy so that the velocity is in the direction of the player
        // =========================================================================================
        // Calculate vector from enemy to player (in x and y)
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        // Calculate (absolute) distance between enemy (this) and player
        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);

        // Calculate direction from enemy to player
        double directionX = distanceToPlayerX/distanceToPlayer;
        double directionY = distanceToPlayerY/distanceToPlayer;

        // Set velocity in the direction to the player
        if(distanceToPlayer > 0) { // Avoid division by zero
            velocityX = directionX*MAX_SPEED;
            velocityY = directionY*MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }
        if (directionX > 0 && directionY < 0) {
            dDegree = (-1 * Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360);//Math.toDegrees(Math.atan((directionX)/(directionY))));
        } else if (directionX < 0 && directionY < 0) {
            dDegree = (180 - (Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        } else if (directionX < 0 && directionY > 0) {
            dDegree = (180 + (-1 * Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        } else if (directionX > 0 && directionY > 0) {
            dDegree = (360 - (Math.toDegrees(Math.atan((directionY * 100) / (directionX * 100))) % 360));//Math.toDegrees(Math.atan((directionX)/(directionY))));
        }

        // =========================================================================================
        //   Update position of the enemy
        // =========================================================================================
        positionX += velocityX;
        positionY += velocityY;
    }
}

