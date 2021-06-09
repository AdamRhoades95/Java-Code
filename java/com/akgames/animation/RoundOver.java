package com.akgames.animation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.content.ContextCompat;

import static android.content.Context.MODE_PRIVATE;

public class RoundOver extends View {
    private int akUpW;
    private int akUpH;
    private int shotgunUpW;
    private int shotgunUpH;
    private int uziUpW;
    private int uziUpH;
    private int revUpH;
    private int pistolUpW;
    private int pistolUpH;
    private int revUpW;
    private int damageIncreaseW;
    private int damageIncreaseH;
    private int healW;
    private int playH;
    private int healH;
    private int playW;
    private int clipIncreaseW;
    private int healthIncreaseH;
    private int healthIncreaseW;
    private int clipIncreaseH;
    private Bitmap play, clipIncrease, healthIncrease, heal, max, damageIncrease, akUp,revUp,shotgunUp,uziUp, pistolUp;
    private int x,y;
    private boolean rev,ak,shot,uz;
    private String strRev,strAk,strShot,strUz;
    private int revCost,akCost,shotCost,uzCost;
    private int costClip, costDamage;//for pistol
    private int lastClicked, costHealth, costIncHealth;
    private long points;
    private int revCostClip, revCostDamage;// for Rev
    private int uziCostClip, uziCostDamage;// for Uzi
    private int shotCostClip, shotCostDamage;// for shotgun
    private int akCostClip, akCostDamage;// for ak47


    public RoundOver(Context context,int height, int width) {
        super(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        this.y = height;
        this.x = width;

        play = BitmapFactory.decodeResource(getResources(),R.drawable.play);
        clipIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.clipincrease);
        damageIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.damageincrease);
        heal = BitmapFactory.decodeResource(getResources(),R.drawable.heal);
        healthIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.healthincrease);
        max = BitmapFactory.decodeResource(getResources(),R.drawable.maxlevel);

        akUp = BitmapFactory.decodeResource(getResources(),R.drawable.akup);
        revUp = BitmapFactory.decodeResource(getResources(),R.drawable.revup);
        shotgunUp = BitmapFactory.decodeResource(getResources(),R.drawable.shotgunup);
        uziUp = BitmapFactory.decodeResource(getResources(),R.drawable.uziup);
        pistolUp = BitmapFactory.decodeResource(getResources(),R.drawable.pistolup);

        damageIncreaseW = ((x/2)-(damageIncrease.getWidth()))-10;
        damageIncreaseH = 70;
        healW = ((x/2)+10);
        healH = damageIncreaseH;

        float textSize = 40;
        if (y>2000) {
            textSize = 90;
        }
            playH = y-play.getHeight()-80;//+50 for smaller

        //playH = y-play.getHeight()-80;//+50 for smaller
        playW = (x/2)-(play.getWidth()/2);
        clipIncreaseW = damageIncreaseW;
        clipIncreaseH = damageIncreaseH+ damageIncrease.getHeight()+(int)(textSize)+10;
        healthIncreaseW = healW;
        healthIncreaseH = healH+heal.getHeight()+(int)(textSize)+10;

        revUpW = x/2- revUp.getWidth()/2;
        pistolUpH = clipIncreaseH+clipIncrease.getHeight()+(int)(textSize)+10;
        pistolUpW = revUpW- (pistolUp.getWidth()+40);
        revUpH = pistolUpH;

        uziUpH = pistolUpH+ pistolUp.getHeight()+(int)textSize+10;
        uziUpW = x/2 - (uziUp.getWidth());
        shotgunUpH = pistolUpH;
        shotgunUpW = revUpW + shotgunUp.getWidth()+40;
        akUpH = uziUpH;
        akUpW = x/2 + 20;

        costIncHealth = 300;
        costHealth = 300;

        costDamage = 300;//for pistol
        costClip = 300;//for pistol

        revCostDamage = 300;//for revolver
        revCostClip = 300;//for revolver

        uziCostDamage = 300;//for uzi
        uziCostClip = 300;//for uzi

        shotCostDamage = 300;//for shot
        shotCostClip = 300;//for shot

        akCostDamage = 300;//for ak47
        akCostClip = 300;//for ak47

        revCost = 500;
        akCost = 10000;
        shotCost = 8000;
        uzCost = 3000;

        lastClicked = 1;

        strAk = "$"+akCost;
        strRev ="$"+revCost;
        strShot = "$"+shotCost;
        strUz = "$"+uzCost;
        ak = true;
        uz = true;
        shot = true;
        rev = true;

        if(y>2000){
            damageIncreaseH+=40;
            damageIncreaseW-=90;
            healH = damageIncreaseH;
            healW+=50;
            clipIncreaseH+=80;
            clipIncreaseW-=90;
            healthIncreaseH = clipIncreaseH;
            healthIncreaseW+=50;
            pistolUpH+=120;
            revUpH = pistolUpH;
            shotgunUpH = pistolUpH;
            uziUpH+=160;
            akUpH = uziUpH;
        }

    }

    public  void Draw(Canvas canvas, Context context, int lastClicked,int height){
        this.lastClicked = lastClicked;
        String strClip = "$"+ costClip;
        String strDamage = "$"+ costDamage;

        String revStrClip = "$"+ revCostClip;
        String revStrDamage = "$"+ revCostDamage;

        String uziStrClip = "$"+ uziCostClip;
        String uziStrDamage = "$"+ uziCostDamage;

        String shotStrClip = "$"+ shotCostClip;
        String shotStrDamage = "$"+ shotCostDamage;

        String akStrClip = "$"+ akCostClip;
        String akStrDamage = "$"+ akCostDamage;

        String strHealth = "$"+ costHealth;
        String strIncHealth = "$"+ costIncHealth;

        Paint paint = new Paint();
        int red = ContextCompat.getColor(context, R.color.gameOver);
        int white = ContextCompat.getColor(context, R.color.white);
        float textSize = 40;
        if (y>2000) {
            textSize = 90;
        }
        paint.setTextSize(textSize);

        if( costHealth<=points){
            paint.setColor(white);
        }
        else if(costHealth>points){
            paint.setColor(red);
        }
        canvas.drawText(strHealth, healW,healH+heal.getHeight()+(int)(textSize), paint);
                //a;
        canvas.drawBitmap(play, playW, playH, null);
        canvas.drawBitmap(damageIncrease,damageIncreaseW,damageIncreaseH,null);
        canvas.drawBitmap(clipIncrease,clipIncreaseW,clipIncreaseH,null);
        canvas.drawBitmap(healthIncrease,healthIncreaseW,healthIncreaseH,null);
        canvas.drawBitmap(heal,healW,healH,null);

        if(costIncHealth<=points){
            paint.setColor(white);
        }
        else if(costIncHealth>points){
            paint.setColor(red);
        }
        canvas.drawText(strIncHealth, healthIncreaseW,healthIncreaseH+healthIncrease.getHeight()+(int)(textSize), paint);

            canvas.drawBitmap(pistolUp, pistolUpW, pistolUpH, null);
            canvas.drawBitmap(revUp, revUpW, revUpH, null);
            canvas.drawBitmap(uziUp, uziUpW, uziUpH, null);
            canvas.drawBitmap(shotgunUp, shotgunUpW, shotgunUpH, null);
            canvas.drawBitmap(akUp, akUpW, akUpH, null);

        if(uz){
            if (points>=uzCost){
                paint.setColor(white);
            }
            else{
                paint.setColor(red);
            }
            canvas.drawText(strUz, uziUpW,uziUpH+uziUp.getHeight()+(int)textSize, paint);
        }
        if(shot){
            if (points>=shotCost){
                paint.setColor(white);
            }
            else{
                paint.setColor(red);
            }
            canvas.drawText(strShot, shotgunUpW,shotgunUpH+shotgunUp.getHeight()+(int)textSize, paint);
        }
        if(rev){
            if (points>=revCost){
                paint.setColor(white);
            }
            else{
                paint.setColor(red);
            }
            canvas.drawText(strRev, revUpW,revUpH+revUp.getHeight()+(int)textSize, paint);
        }
        if(ak){
            if (points>=akCost){
                paint.setColor(white);
            }
            else{
                paint.setColor(red);
            }
            canvas.drawText(strAk, akUpW,akUpH+akUp.getHeight()+(int)textSize, paint);
        }

        if(lastClicked == 1){
            if(costDamage<=points){
                paint.setColor(white);
            }
            else if(costDamage>points){
                paint.setColor(red);
            }
            canvas.drawText(strDamage, damageIncreaseW,damageIncreaseH+damageIncrease.getHeight()+(int)(textSize), paint);

            if( costClip<=points){
                paint.setColor(white);
            }
            else if(costClip>points){

                paint.setColor(red);
            }
            canvas.drawText(strClip, clipIncreaseW,clipIncreaseH+clipIncrease.getHeight()+(int)(textSize), paint);

            akUp = BitmapFactory.decodeResource(getResources(),R.drawable.akup);
            revUp = BitmapFactory.decodeResource(getResources(),R.drawable.revup);
            shotgunUp = BitmapFactory.decodeResource(getResources(),R.drawable.shotgunup);
            uziUp = BitmapFactory.decodeResource(getResources(),R.drawable.uziup);
            pistolUp = BitmapFactory.decodeResource(getResources(),R.drawable.clickedpistol);
        }
        else if(lastClicked == 2){
            if(revCostDamage<=points){
                paint.setColor(white);
                //canvas.drawText(revStrDamage, 10,280, paint);
            }
            else if(revCostDamage>points){
                paint.setColor(red);
                //canvas.drawText(revStrDamage, 10,280, paint);
            }
            canvas.drawText(revStrDamage, damageIncreaseW,damageIncreaseH+damageIncrease.getHeight()+(int)(textSize), paint);

            if( revCostClip<=points){
                paint.setColor(white);
                //canvas.drawText(revStrClip, 10,530, paint);
            }
            else if(revCostClip>points){

                paint.setColor(red);
                //canvas.drawText(revStrClip, 10,530, paint);
            }
            canvas.drawText(revStrClip, clipIncreaseW,clipIncreaseH+clipIncrease.getHeight()+(int)(textSize), paint);

            akUp = BitmapFactory.decodeResource(getResources(),R.drawable.akup);
            revUp = BitmapFactory.decodeResource(getResources(),R.drawable.clickedrev);
            shotgunUp = BitmapFactory.decodeResource(getResources(),R.drawable.shotgunup);
            uziUp = BitmapFactory.decodeResource(getResources(),R.drawable.uziup);
            pistolUp = BitmapFactory.decodeResource(getResources(),R.drawable.pistolup);
        }
        else if(lastClicked == 3){
            if(revCostDamage<=points){
                paint.setColor(white);
                //canvas.drawText(revStrDamage, 10,280, paint);
            }
            else if(uziCostDamage>points){
                paint.setColor(red);
                //canvas.drawText(uziStrDamage, 10,280, paint);
            }
            canvas.drawText(uziStrDamage, damageIncreaseW,damageIncreaseH+damageIncrease.getHeight()+(int)(textSize), paint);

            if( uziCostClip<=points){
                paint.setColor(white);
                //canvas.drawText(uziStrClip, 10,530, paint);
            }
            else if(uziCostClip>points){
                paint.setColor(red);
                //canvas.drawText(uziStrClip, 10,530, paint);
            }
            canvas.drawText(uziStrClip, clipIncreaseW,clipIncreaseH+clipIncrease.getHeight()+(int)(textSize), paint);

            akUp = BitmapFactory.decodeResource(getResources(),R.drawable.akup);
            revUp = BitmapFactory.decodeResource(getResources(),R.drawable.revup);
            shotgunUp = BitmapFactory.decodeResource(getResources(),R.drawable.shotgunup);
            uziUp = BitmapFactory.decodeResource(getResources(),R.drawable.clickeduzi);
            pistolUp = BitmapFactory.decodeResource(getResources(),R.drawable.pistolup);
        }
        else if(lastClicked == 4){
            if(shotCostDamage<=points){
                paint.setColor(white);
                //canvas.drawText(shotStrDamage, 10,280, paint);
            }
            else if(shotCostDamage>points){
                paint.setColor(red);
                //canvas.drawText(shotStrDamage, 10,280, paint);
            }
            canvas.drawText(shotStrDamage, damageIncreaseW,damageIncreaseH+damageIncrease.getHeight()+(int)(textSize), paint);

            if( shotCostClip<=points){
                paint.setColor(white);
                //canvas.drawText(shotStrClip, 10,530, paint);
            }
            else if(shotCostClip>points){
                paint.setColor(red);
                //canvas.drawText(shotStrClip, 10,530, paint);
            }
            canvas.drawText(shotStrClip, clipIncreaseW,clipIncreaseH+clipIncrease.getHeight()+(int)(textSize), paint);

            akUp = BitmapFactory.decodeResource(getResources(),R.drawable.akup);
            revUp = BitmapFactory.decodeResource(getResources(),R.drawable.revup);
            shotgunUp = BitmapFactory.decodeResource(getResources(),R.drawable.clickedshot);
            uziUp = BitmapFactory.decodeResource(getResources(),R.drawable.uziup);
            pistolUp = BitmapFactory.decodeResource(getResources(),R.drawable.pistolup);
        }
        else if(lastClicked == 5){
            if(akCostDamage<=points){
                paint.setColor(white);
                //canvas.drawText(akStrDamage, 10,280, paint);
            }
            else if(akCostDamage>points){
                paint.setColor(red);
                //canvas.drawText(akStrDamage, 10,280, paint);
            }
            canvas.drawText(akStrDamage, damageIncreaseW,damageIncreaseH+damageIncrease.getHeight()+(int)(textSize), paint);

            if( akCostClip<=points){
                paint.setColor(white);
                //canvas.drawText(akStrClip, 10,530, paint);
            }
            else if(akCostClip>points){
                paint.setColor(red);
                //canvas.drawText(akStrClip, 10,530, paint);
            }
            canvas.drawText(akStrClip, clipIncreaseW,clipIncreaseH+clipIncrease.getHeight()+(int)(textSize), paint);
            akUp = BitmapFactory.decodeResource(getResources(),R.drawable.clickedak);
            revUp = BitmapFactory.decodeResource(getResources(),R.drawable.revup);
            shotgunUp = BitmapFactory.decodeResource(getResources(),R.drawable.shotgunup);
            uziUp = BitmapFactory.decodeResource(getResources(),R.drawable.uziup);
            pistolUp = BitmapFactory.decodeResource(getResources(),R.drawable.pistolup);
        }
    }

    public void setLastClicked(int i) {
        lastClicked = 1;
    }

    public void restoreState(Bundle savedInstanceState) {
        play = BitmapFactory.decodeResource(getResources(),R.drawable.play);
        clipIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.clipincrease);
        damageIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.damageincrease);
        heal = BitmapFactory.decodeResource(getResources(),R.drawable.heal);
        healthIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.healthincrease);
        max = BitmapFactory.decodeResource(getResources(),R.drawable.maxlevel);

        akUp = BitmapFactory.decodeResource(getResources(),R.drawable.akup);
        revUp = BitmapFactory.decodeResource(getResources(),R.drawable.revup);
        shotgunUp = BitmapFactory.decodeResource(getResources(),R.drawable.shotgunup);
        uziUp = BitmapFactory.decodeResource(getResources(),R.drawable.uziup);
        pistolUp = BitmapFactory.decodeResource(getResources(),R.drawable.pistolup);

        rev = savedInstanceState.getBoolean("rev");
        ak = savedInstanceState.getBoolean("ak");
        shot = savedInstanceState.getBoolean("shot");
        uz = savedInstanceState.getBoolean("uz");

        strRev = savedInstanceState.getString("strRev");
        strAk = savedInstanceState.getString("strAk");
        strShot = savedInstanceState.getString("strShot");
        strUz = savedInstanceState.getString("strUz");

        x = savedInstanceState.getInt("roundoverx");
        y = savedInstanceState.getInt("roundovery");
        revCost = savedInstanceState.getInt("revCost");
        revCost = savedInstanceState.getInt("akCost");
        shotCost = savedInstanceState.getInt("shotCost");
        uzCost = savedInstanceState.getInt("uzCost");
        costClip = savedInstanceState.getInt("costClip");
        costDamage = savedInstanceState.getInt("costDamage");
        lastClicked = savedInstanceState.getInt("lastClicked");
        costHealth = savedInstanceState.getInt("costHealth");
        costIncHealth = savedInstanceState.getInt("costIncHealth");
        points = savedInstanceState.getLong("points");
        revCostClip = savedInstanceState.getInt("revCostClip");
        revCostDamage = savedInstanceState.getInt("revCostDamage");
        uziCostClip = savedInstanceState.getInt("uziCostClip");
        uziCostDamage = savedInstanceState.getInt("uziCostDamage");
        shotCostClip = savedInstanceState.getInt("shotCostClip");
        shotCostDamage = savedInstanceState.getInt("shotCostDamage");
        akCostClip = savedInstanceState.getInt("akCostClip");
        akCostDamage = savedInstanceState.getInt("akCostDamage");
    }
    public void saveState(Bundle outState) {

        outState.putBoolean("rev", rev);
        outState.putBoolean("ak", ak);
        outState.putBoolean("shot", shot);
        outState.putBoolean("uz", uz);

        outState.putString("strRev", strRev);
        outState.putString("strAk", strAk);
        outState.putString("strShot", strShot);
        outState.putString("strUz", strUz);

        outState.putInt("roundoverx", x);
        outState.putInt("roundovery", y);
        outState.putInt("revCost", revCost);
        outState.putInt("akCost", akCost);
        outState.putInt("shotCost", shotCost);
        outState.putInt("uzCost", uzCost);
        outState.putInt("costClip", costClip);
        outState.putInt("costDamage", costDamage);
        outState.putInt("lastClicked", lastClicked);
        outState.putInt("costHealth", costHealth);
        outState.putInt("costIncHealth", costIncHealth);
        outState.putLong("points", points);
        outState.putInt("revCostClip", revCostClip);
        outState.putInt("revCostDamage", revCostDamage);
        outState.putInt("uziCostClip", uziCostClip);
        outState.putInt("uziCostDamage", uziCostDamage);
        outState.putInt("shotCostClip", shotCostClip);
        outState.putInt("shotCostDamage", shotCostDamage);
        outState.putInt("akCostClip", akCostClip);
        outState.putInt("akCostDamage", akCostDamage);

        //private Bitmap play, clipIncrease, healthIncrease, heal, max, damageIncrease, akUp,revUp,shotgunUp,uziUp, pistolUp;
    }

    public void saveState() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        SharedPreferences.Editor outState = sharedPreferences.edit();
        outState.putBoolean("rev", rev);
        outState.putBoolean("ak", ak);
        outState.putBoolean("shot", shot);
        outState.putBoolean("uz", uz);

        outState.putString("strRev", strRev);
        outState.putString("strAk", strAk);
        outState.putString("strShot", strShot);
        outState.putString("strUz", strUz);

        outState.putInt("roundoverx", x);
        outState.putInt("roundovery", y);
        outState.putInt("revCost", revCost);
        outState.putInt("akCost", akCost);
        outState.putInt("shotCost", shotCost);
        outState.putInt("uzCost", uzCost);
        outState.putInt("costClip", costClip);
        outState.putInt("costDamage", costDamage);
        outState.putInt("lastClicked", lastClicked);
        outState.putInt("costHealth", costHealth);
        outState.putInt("costIncHealth", costIncHealth);
        outState.putLong("points", points);
        outState.putInt("revCostClip", revCostClip);
        outState.putInt("revCostDamage", revCostDamage);
        outState.putInt("uziCostClip", uziCostClip);
        outState.putInt("uziCostDamage", uziCostDamage);
        outState.putInt("shotCostClip", shotCostClip);
        outState.putInt("shotCostDamage", shotCostDamage);
        outState.putInt("akCostClip", akCostClip);
        outState.putInt("akCostDamage", akCostDamage);
        outState.apply();
        //private Bitmap play, clipIncrease, healthIncrease, heal, max, damageIncrease, akUp,revUp,shotgunUp,uziUp, pistolUp;
    }
    public void restoreState() {
        SharedPreferences savedInstanceState = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        play = BitmapFactory.decodeResource(getResources(),R.drawable.play);
        clipIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.clipincrease);
        damageIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.damageincrease);
        heal = BitmapFactory.decodeResource(getResources(),R.drawable.heal);
        healthIncrease = BitmapFactory.decodeResource(getResources(),R.drawable.healthincrease);
        max = BitmapFactory.decodeResource(getResources(),R.drawable.maxlevel);

        akUp = BitmapFactory.decodeResource(getResources(),R.drawable.akup);
        revUp = BitmapFactory.decodeResource(getResources(),R.drawable.revup);
        shotgunUp = BitmapFactory.decodeResource(getResources(),R.drawable.shotgunup);
        uziUp = BitmapFactory.decodeResource(getResources(),R.drawable.uziup);
        pistolUp = BitmapFactory.decodeResource(getResources(),R.drawable.pistolup);

        damageIncreaseW = ((x/2)-(damageIncrease.getWidth()))-10;
        damageIncreaseH = 70;
        healW = ((x/2)+10);
        healH = damageIncreaseH;

        float textSize = 40;
        if (y>2000) {
            textSize = 90;
        }
        playH = y-play.getHeight()-80;
        playW = (x/2)-(play.getWidth()/2);
        clipIncreaseW = damageIncreaseW;
        clipIncreaseH = damageIncreaseH+ damageIncrease.getHeight()+(int)(textSize)+20;
        healthIncreaseW = healW;
        healthIncreaseH = healH+heal.getHeight()+(int)(textSize)+20;

        revUpW = x/2- revUp.getWidth()/2;
        pistolUpH = clipIncreaseH+clipIncrease.getHeight()+(int)(textSize)+30;
        pistolUpW = revUpW- (pistolUp.getWidth()+40);
        revUpH = pistolUpH;

        uziUpH = pistolUpH+ pistolUp.getHeight()+(int)textSize+40;
        uziUpW = x/2 - (uziUp.getWidth());
        shotgunUpH = pistolUpH;
        shotgunUpW = revUpW + shotgunUp.getWidth()+40;
        akUpH = uziUpH;
        akUpW = x/2 + 20;


        rev = savedInstanceState.getBoolean("rev", true);
        ak = savedInstanceState.getBoolean("ak",true);
        shot = savedInstanceState.getBoolean("shot",true);
        uz = savedInstanceState.getBoolean("uz",true);

        revCost = savedInstanceState.getInt("revCost", 500);
        revCost = savedInstanceState.getInt("akCost",10000);
        shotCost = savedInstanceState.getInt("shotCost", 8000);
        uzCost = savedInstanceState.getInt("uzCost",3000);

        strRev = savedInstanceState.getString("strRev", "$"+revCost);
        strAk = savedInstanceState.getString("strAk", "$"+akCost);
        strShot = savedInstanceState.getString("strShot", "$"+shotCost);
        strUz = savedInstanceState.getString("strUz", "$"+uzCost);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        this.y = displayMetrics.heightPixels;
        this.x = displayMetrics.widthPixels;
        x = savedInstanceState.getInt("roundoverx", this.x = displayMetrics.widthPixels);
        y = savedInstanceState.getInt("roundovery", this.y = displayMetrics.heightPixels);
        costClip = savedInstanceState.getInt("costClip", 300);
        costDamage = savedInstanceState.getInt("costDamage",300);
        lastClicked = savedInstanceState.getInt("lastClicked",1);
        costHealth = savedInstanceState.getInt("costHealth",300);
        costIncHealth = savedInstanceState.getInt("costIncHealth",300);
        points = savedInstanceState.getLong("points",300);
        revCostClip = savedInstanceState.getInt("revCostClip",300);
        revCostDamage = savedInstanceState.getInt("revCostDamage",300);
        uziCostClip = savedInstanceState.getInt("uziCostClip",300);
        uziCostDamage = savedInstanceState.getInt("uziCostDamage",300);
        shotCostClip = savedInstanceState.getInt("shotCostClip",300);
        shotCostDamage = savedInstanceState.getInt("shotCostDamage",300);
        akCostClip = savedInstanceState.getInt("akCostClip",300);
        akCostDamage = savedInstanceState.getInt("akCostDamage",300);
    }


    public int getPistolUpW() {
        return pistolUpW;
    }

    public int getPistolUpH() {
        return pistolUpH;
    }

    public int PistolUpW() {
        return pistolUp.getWidth();
    }

    public int PistolUpH() {
        return pistolUp.getHeight();
    }

    public int getRevUpH() {
        return revUpH;
    }

    public int getRevUpW() {
        return revUpW;
    }

    public int RevUpH() {
        return revUp.getHeight();
    }

    public int RevUpW() {
        return revUp.getWidth();
    }

    public int getUziUpW() {
        return uziUpW;
    }

    public int getUziUpH() {
        return uziUpH;
    }

    public int UziUpW() {
        return uziUp.getWidth();
    }

    public int UziUpH() {
        return uziUp.getHeight();
    }

    public int getAkUpW() {
        return akUpW;
    }

    public int getAkUpH() {
        return akUpH;
    }

    public int AkUpW() {
        return akUp.getWidth();
    }

    public int AkUpH() {
        return akUp.getHeight();
    }

    public int getShotgunUpW() {
        return shotgunUpW;
    }

    public int getShotgunUpH() {
        return shotgunUpH;
    }

    public int ShotgunUpW() {
        return shotgunUp.getWidth();
    }

    public int ShotgunUpH() {
        return shotgunUp.getHeight();
    }

    public int getPlay(){
        return play.getWidth();
    }

    public long getPoints() {
        return points;
    }

    public int getCostClip() {
        return costClip;
    }

    public int getCostDamage() {
        return costDamage;
    }

    public int getCostHealth() {
        return costHealth;
    }

    public int getCostIncHealth() {
        return costIncHealth;
    }

    public int getRevCostClip() {
        return revCostClip;
    }

    public int getRevCostDamage() {
        return revCostDamage;
    }

    public int getUziCostClip() {
        return uziCostClip;
    }

    public int getUziCostDamage() {
        return uziCostDamage;
    }

    public int getShotCostClip() {
        return shotCostClip;
    }

    public int getShotCostDamage() {
        return shotCostDamage;
    }

    public int getAkCostClip() {
        return akCostClip;
    }

    public int getAkCostDamage() {
        return akCostDamage;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public void setCostClip(int costClip) {
        this.costClip = costClip;
    }

    public void setCostDamage(int costDamage) {
        this.costDamage = costDamage;
    }

    public void setAkCostClip(int akCostClip) {
        this.akCostClip = akCostClip;
    }

    public void setAkCostDamage(int akCostDamage) {
        this.akCostDamage = akCostDamage;
    }

    public void setRevCostClip(int revCostClip) {
        this.revCostClip = revCostClip;
    }

    public void setRevCostDamage(int revCostDamage) {
        this.revCostDamage = revCostDamage;
    }

    public void setUziCostClip(int uziCostClip) {
        this.uziCostClip = uziCostClip;
    }

    public void setUziCostDamage(int uziCostDamage) {
        this.uziCostDamage = uziCostDamage;
    }

    public void setShotCostClip(int shotCostClip) {
        this.shotCostClip = shotCostClip;
    }

    public void setShotCostDamage(int shotCostDamage) {
        this.shotCostDamage = shotCostDamage;
    }
    public void setCostHealth(int costHealth) {
        this.costHealth = costHealth;
    }

    public void setCostIncHealth(int costIncHealth) {
        this.costIncHealth = costIncHealth;
    }

    public void setAk(boolean ak) {
        this.ak = ak;
    }

    public void setRev(boolean rev) {
        this.rev = rev;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public void setUz(boolean uz) {
        this.uz = uz;
    }

    public boolean getAk() {
        return ak;
    }

    public boolean getUz() {
        return uz;
    }

    public boolean getShot() {
        return shot;
    }

    public boolean getRev() {
        return rev;
    }

    public int getAkCost() {
        return akCost;
    }

    public int getShotCost() {
        return shotCost;
    }

    public int getUzCost() {
        return uzCost;
    }

    public int getRevCost() {
        return revCost;
    }

    public int getDamageIncreaseW() {
        return damageIncreaseW;
    }

    public int DamageIncreaseW() {
        return damageIncrease.getWidth();
    }

    public int DamageIncreaseH() {
        return damageIncrease.getHeight();
    }

    public int getDamageIncreaseH() {
        return damageIncreaseH;
    }

    public int getHealW() {
        return healW;
    }

    public int getPlayH() {
        return playH;
    }

    public int getHealH() {
        return healH;
    }

    public int HealH() {
        return heal.getHeight();
    }

    public int HealW() {
        return heal.getWidth();
    }

    public int getPlayW() {
        return playW;
    }

    public int getClipIncreaseW() {
        return clipIncreaseW;
    }

    public int ClipIncreaseW() {
        return clipIncrease.getWidth();
    }

    public int ClipIncreaseH() {
        return clipIncrease.getHeight();
    }

    public int getHealthIncreaseH() {
        return healthIncreaseH;
    }

    public int HealthIncreaseH() {
        return healthIncrease.getHeight();
    }

    public int HealthIncreaseW() {
        return healthIncrease.getWidth();
    }

    public int getHealthIncreaseW() {
        return healthIncreaseW;
    }

    public int getClipIncreaseH() {
        return clipIncreaseH;
    }
}
