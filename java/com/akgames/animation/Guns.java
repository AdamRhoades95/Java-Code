package com.akgames.animation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import static android.content.Context.MODE_PRIVATE;

public class Guns extends View {
    public static double SPEED_PIXELS_PER_SECOND = 800.0;
    private static float MAX_SPEED = (float)(SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS);
    private float velocityX, velocityY;
    private Bitmap pistol, revolver, ak47, shotgun, uzi, current;
    private float X,Y;
    private float dDegree;
    private double pistolDamage, revDamage, uziDamage, shotDamage, akDamage;
    private double dDegree1;
    private int weaponIndex;
    private int pistolClip, maxPistolClip, revClip, maxRevClip, uziClip,
            maxUziClip, shotClip, maxShotClip, akClip, maxAkClip;
    private Player player;
    private float centerX, centerY;
    private float gunX,gunY;

    public Guns(Context context, Player player, float X, float Y) {
        super(context);
        velocityX = X*MAX_SPEED;
        velocityY = Y*MAX_SPEED;
        pistolClip = 7;
        maxPistolClip = 7;
        revClip = 4;
        maxRevClip = 4;
        uziClip = 15;
        maxUziClip = 15;
        shotClip = 2;
        maxShotClip = 2;
        akClip = 20;
        maxAkClip = 20;
        pistolDamage = 1.5;
        revDamage = 5;
        uziDamage = 1.5;
        shotDamage = 20;
        akDamage = 10;
        this.X = X;
        this.Y = Y;
        dDegree = 0;
        weaponIndex = 0;
        this.player = player;
        pistol = BitmapFactory.decodeResource(getResources(),R.drawable.pistal);
        revolver = BitmapFactory.decodeResource(getResources(),R.drawable.revolver);
        uzi = BitmapFactory.decodeResource(getResources(),R.drawable.uzi);
        shotgun = BitmapFactory.decodeResource(getResources(),R.drawable.shotgun);
        ak47 = BitmapFactory.decodeResource(getResources(),R.drawable.ak);
        current = pistol;

        centerX = player.getCenterX();
        centerY = player.getCenterY();
        this.dDegree = (float)player.getdDegree();

        gunX = player.getPositionX()+((player.width()/2)+(player.width()/4));
        gunY = (player.getPositionY()+(player.height()/2)-(current.getHeight()/2));
    }

    public void Draw (Canvas canvas){
        canvas.save();
        canvas.rotate(-1*dDegree,centerX,centerY);

        if(weaponIndex==0) {
            current = pistol;
        }
        else if(weaponIndex==1){
            current = revolver;
        }
        if(weaponIndex==2) {
            current = uzi;
        }
        else if(weaponIndex==3){
            current = shotgun;
        }
        else if(weaponIndex==4){
            current = ak47;
        }
        canvas.drawBitmap(current, gunX, gunY, null);
        canvas.restore();
    }
    public void Draw (Canvas canvas, int x){
        canvas.save();
        canvas.rotate(-1*dDegree,centerX,centerY);

        if(weaponIndex==0) {
            current = pistol;
        }
        else if(weaponIndex==1){
            current = revolver;
        }
        else if(weaponIndex==2) {
            current = uzi;
        }
        else if(weaponIndex==3){
            current = shotgun;
        }
        else if(weaponIndex==4){
            current = ak47;
        }
        canvas.drawBitmap(current, gunX, gunY, null);
        canvas.restore();
    }

    public void update(Player player,int x) {
//        this.player = player;
//        this.dDegree1 = (float)player.getdDegree1();
//
//        gunX = player.getPositionX()+((player.width()/2)+(player.width()/4));
//        gunY = (player.getPositionY()+(player.height()/2)-(current.getHeight()/2));
        this.player = player;
        centerX = player.getCenterX();
        centerY = player.getCenterY();
        dDegree = (float)player.getdDegree1();
        gunX = player.getPositionX()+((player.width()/2)+(player.width()/4));
        gunY = (player.getPositionY()+(player.height()/2)-(current.getHeight()/2));
    }

    public void update(Player player) {
        this.player = player;
        centerX = player.getCenterX();
        centerY = player.getCenterY();
        dDegree = (float)player.getdDegree();
        gunX = player.getPositionX()+((player.width()/2)+(player.width()/4));
        gunY = (player.getPositionY()+(player.height()/2)-(current.getHeight()/2));
        //System.out.println(Float.toString(dDegree));
    }

    public void saveState() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        SharedPreferences.Editor outState = sharedPreferences.edit();
        outState.putInt("uziClip", uziClip);
        outState.putInt("maxRevClip", maxRevClip);
        outState.putInt("revClip", revClip);
        outState.putInt("maxPistolClip", maxPistolClip);
        outState.putInt("pistolClip", pistolClip);
        outState.putInt("maxUziClip", maxUziClip);
        outState.putInt("shotClip", shotClip);
        outState.putInt("maxShotClip", maxShotClip);
        outState.putInt("akClip", akClip);
        outState.putInt("maxAkClip", maxAkClip);
        outState.putInt("weaponIndex", weaponIndex);
        outState.putFloat("SPEED_PIXELS_PER_SECOND", (float) SPEED_PIXELS_PER_SECOND);
        outState.putFloat("pistolDamage", (float)pistolDamage);
        outState.putFloat("revDamage", (float)revDamage);
        outState.putFloat("uziDamage", (float)uziDamage);
        outState.putFloat("shotDamage", (float)shotDamage);
        outState.putFloat("akDamage", (float)akDamage);
        outState.putFloat("dDegree1", (float)dDegree1);
        outState.putFloat("MAX_SPEED", MAX_SPEED);
        outState.putFloat("velocityX", velocityX);
        outState.putFloat("velocityY", velocityY);
        outState.putFloat("Y", Y);
        outState.putFloat("X", X);
        outState.putFloat("dDegree", dDegree);
        outState.apply();

        //private Bitmap pistol, revolver, ak47, shotgun, uzi;
    }
    public void restoreState(int j, int k) {
        SharedPreferences savedInstanceState = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        uziClip = savedInstanceState.getInt("uziClip",15);
        maxRevClip = savedInstanceState.getInt("maxRevClip",4);
        revClip = savedInstanceState.getInt("revClip",4);
        maxPistolClip = savedInstanceState.getInt("maxPistolClip",7);
        pistolClip = savedInstanceState.getInt("pistolClip",7);
        maxUziClip = savedInstanceState.getInt("maxUziClip",15);
        shotClip = savedInstanceState.getInt("shotClip",2);
        maxShotClip = savedInstanceState.getInt("maxShotClip",2);
        akClip = savedInstanceState.getInt("akClip",20);
        maxAkClip = savedInstanceState.getInt("maxAkClip",20);
        weaponIndex = savedInstanceState.getInt("weaponIndex",1);
        SPEED_PIXELS_PER_SECOND = savedInstanceState.getFloat("SPEED_PIXELS_PER_SECOND", 800);
        pistolDamage = savedInstanceState.getFloat("pistolDamage",(float)1.5);
        revDamage = savedInstanceState.getFloat("revDamage",5);
        uziDamage = savedInstanceState.getFloat("uziDamage",(float)1.5);
        shotDamage = savedInstanceState.getFloat("shotDamage",20);
        akDamage = savedInstanceState.getFloat("akDamage",10);
        dDegree1 = savedInstanceState.getFloat("dDegree1",0);
        MAX_SPEED = savedInstanceState.getFloat("MAX_SPEED", (float)(SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS));
        velocityX = savedInstanceState.getFloat("velocityX",0);
        velocityY = savedInstanceState.getFloat("velocityY",0);
        Y = savedInstanceState.getFloat("Y", k);
        X = savedInstanceState.getFloat("X", j);
        dDegree = savedInstanceState.getFloat("dDegree",0);
        pistol = BitmapFactory.decodeResource(getResources(),R.drawable.pistal);
        revolver = BitmapFactory.decodeResource(getResources(),R.drawable.revolver);
        uzi = BitmapFactory.decodeResource(getResources(),R.drawable.uzi);
        shotgun = BitmapFactory.decodeResource(getResources(),R.drawable.shotgun);
        ak47 = BitmapFactory.decodeResource(getResources(),R.drawable.ak);
    }
    public void saveState(Bundle outState) {
        outState.putInt("uziClip", uziClip);
        outState.putInt("maxRevClip", maxRevClip);
        outState.putInt("revClip", revClip);
        outState.putInt("maxPistolClip", maxPistolClip);
        outState.putInt("pistolClip", pistolClip);
        outState.putInt("maxUziClip", maxUziClip);
        outState.putInt("shotClip", shotClip);
        outState.putInt("maxShotClip", maxShotClip);
        outState.putInt("akClip", akClip);
        outState.putInt("maxAkClip", maxAkClip);
        outState.putInt("weaponIndex", weaponIndex);
        outState.putDouble("SPEED_PIXELS_PER_SECOND", SPEED_PIXELS_PER_SECOND);
        outState.putDouble("pistolDamage", pistolDamage);
        outState.putDouble("revDamage", revDamage);
        outState.putDouble("uziDamage", uziDamage);
        outState.putDouble("shotDamage", shotDamage);
        outState.putDouble("akDamage", akDamage);
        outState.putDouble("dDegree1", dDegree1);
        outState.putFloat("MAX_SPEED", MAX_SPEED);
        outState.putFloat("velocityX", velocityX);
        outState.putFloat("velocityY", velocityY);
        outState.putFloat("Y", Y);
        outState.putFloat("X", X);
        outState.putFloat("dDegree", dDegree);

        //private Bitmap pistol, revolver, ak47, shotgun, uzi;
    }
    public void restoreState(Bundle savedInstanceState) {
        uziClip = savedInstanceState.getInt("uziClip");
        maxRevClip = savedInstanceState.getInt("maxRevClip");
        revClip = savedInstanceState.getInt("revClip");
        maxPistolClip = savedInstanceState.getInt("maxPistolClip");
        pistolClip = savedInstanceState.getInt("pistolClip");
        maxUziClip = savedInstanceState.getInt("maxUziClip");
        shotClip = savedInstanceState.getInt("shotClip");
        maxShotClip = savedInstanceState.getInt("maxShotClip");
        akClip = savedInstanceState.getInt("akClip");
        maxAkClip = savedInstanceState.getInt("maxAkClip");
        weaponIndex = savedInstanceState.getInt("weaponIndex");
        SPEED_PIXELS_PER_SECOND = savedInstanceState.getDouble("SPEED_PIXELS_PER_SECOND");
        pistolDamage = savedInstanceState.getDouble("pistolDamage");
        revDamage = savedInstanceState.getDouble("revDamage");
        uziDamage = savedInstanceState.getDouble("uziDamage");
        shotDamage = savedInstanceState.getDouble("shotDamage");
        akDamage = savedInstanceState.getDouble("akDamage");
        dDegree1 = savedInstanceState.getDouble("dDegree1");
        MAX_SPEED = savedInstanceState.getFloat("MAX_SPEED");
        velocityX = savedInstanceState.getFloat("velocityX");
        velocityY = savedInstanceState.getFloat("velocityY");
        Y = savedInstanceState.getFloat("Y");
        X = savedInstanceState.getFloat("X");
        dDegree = savedInstanceState.getFloat("dDegree");
        pistol = BitmapFactory.decodeResource(getResources(),R.drawable.pistal);
        revolver = BitmapFactory.decodeResource(getResources(),R.drawable.revolver);
        uzi = BitmapFactory.decodeResource(getResources(),R.drawable.uzi);
        shotgun = BitmapFactory.decodeResource(getResources(),R.drawable.shotgun);
        ak47 = BitmapFactory.decodeResource(getResources(),R.drawable.ak);
    }

    public void setWeaponIndex(int weaponIndex) {
        this.weaponIndex = weaponIndex;
    }
    public float getGunX() {
        return gunX;
    }
    public float getGunY() {
        return gunY;
    }
    public float getCenterX() {
        return centerX;
    }
    public float getCenterY() {
        return centerY;
    }
    public float getdDegree() {
        return dDegree;
    }
    public float getPistolDamage(){
        return (float)pistolDamage;
    }
    public void setPistolDamage(float damage){
        this.pistolDamage = damage;
    }
    public void setMaxPistolClip(int maxPistolClip) {
        this.maxPistolClip = maxPistolClip;
    }
    public void setPistolClip(int pistolClip) {
        this.pistolClip = pistolClip;
    }
    public int getMaxPistolClip() {
        return maxPistolClip;
    }
    public int getPistolClip() {
        return pistolClip;
    }
    public float getRevDamage(){
        return (float)revDamage;
    }
    public void setRevDamage(float damage){
        this.revDamage = damage;
    }
    public void setMaxRevClip(int maxRevClip) {
        this.maxRevClip = maxRevClip;
    }
    public void setRevClip(int revClip) {
        this.revClip = revClip;
    }
    public int getMaxRevClip() {
        return maxRevClip;
    }
    public int getRevClip() {
        return revClip;
    }
    public float getUziDamage(){
        return (float)uziDamage;
    }
    public void setUziDamage(float damage){
        this.uziDamage = damage;
    }
    public void setMaxUziClip(int maxUziClip) {
        this.maxUziClip = maxUziClip;
    }
    public void setUziClip(int uziClip) {
        this.uziClip = uziClip;
    }
    public int getUziClip() {
        return uziClip;
    }
    public int getMaxUziClip() {
        return maxUziClip;
    }
    public float getShotDamage(){
        return (float)shotDamage;
    }
    public void setShotDamage(float damage){
        this.shotDamage = damage;
    }
    public void setMaxShotClip(int maxShotClip) {
        this.maxShotClip = maxShotClip;
    }
    public void setShotClip(int shotClip) {
        this.shotClip = shotClip;
    }
    public int getMaxShotClip() {
        return maxShotClip;
    }
    public int getShotClip() {
        return shotClip;
    }
    public float getAkDamage(){
        return (float)akDamage;
    }
    public void setAkDamage(float damage){
        this.akDamage = damage;
    }
    public void setAkClip(int akClip) {
        this.akClip = akClip;
    }
    public void setMaxAkClip(int maxAkClip) {
        this.maxAkClip = maxAkClip;
    }
    public int getAkClip() {
        return akClip;
    }
    public int getMaxAkClip() {
        return maxAkClip;
    }
    public void setdDegree1(double dDegree1) {
        this.dDegree1 = dDegree1;
    }
}
