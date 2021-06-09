package com.akgames.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

public class Credits extends View {
    private int x, y;
    private Bitmap next,back,home,arcMode, notArcMode, surMode, notSurMode,notRound, round, notKills, kills,notScore, score;
    private Context context;
    private int checkModeType, checkSubType;
    private int ScoreW, ScoreH, KillsW, KillsH, roundW, roundH, surModeW, surModeH, ArcModeW, ArcModeH, backW, backH, homeW, homeH, nextW, nextH;
    public Credits(Context context,int x, int y) {
        super(context);
        this.context = context;
        this.x = x;
        this.y = y;
        checkModeType = 1;
        checkSubType = 1;
        next = BitmapFactory.decodeResource(getResources(),R.drawable.next);
        home = BitmapFactory.decodeResource(getResources(),R.drawable.backtohome);
        back = BitmapFactory.decodeResource(getResources(),R.drawable.back);
        notArcMode = BitmapFactory.decodeResource(getResources(),R.drawable.clickedarcade);
        arcMode = BitmapFactory.decodeResource(getResources(),R.drawable.notclickedarcade);
        surMode = BitmapFactory.decodeResource(getResources(),R.drawable.notclickedsurvival);
        notSurMode = BitmapFactory.decodeResource(getResources(),R.drawable.clickedsurvival);
        notRound = BitmapFactory.decodeResource(getResources(),R.drawable.notroundclicked);
        round = BitmapFactory.decodeResource(getResources(),R.drawable.roundclicked);
        notKills = BitmapFactory.decodeResource(getResources(),R.drawable.notkillsclicked);
        kills = BitmapFactory.decodeResource(getResources(),R.drawable.killsclicked);
        notScore = BitmapFactory.decodeResource(getResources(),R.drawable.notscoreclicked);
        score = BitmapFactory.decodeResource(getResources(),R.drawable.scoreclicked);

        nextH = 0;
        homeH = nextH;
        homeW = (x/2)-((home.getWidth()/2));
        backH = nextH;
        backW = homeW-(back.getWidth());
        nextW = homeW+home.getWidth();
        ArcModeH =nextH+next.getHeight();
        ArcModeW = ((x/2)+20);
        surModeH = ArcModeH;
        surModeW = ((x/2)-(surMode.getWidth()+20));
        roundH = surModeH+ surMode.getHeight();
        roundW = ((x/2)-((round.getWidth()/2)+40))-kills.getWidth();;
        KillsH = roundH;
        KillsW = (x/2)-((kills.getWidth()/2)+20);;
        ScoreH = roundH;
        ScoreW = (x/2)+(kills.getWidth()/2)+20;
    }

    public void setCheckModeType(int checkModeType) {
        this.checkModeType = checkModeType;
    }

    public void setCheckSubType(int checkSubType) {
        this.checkSubType = checkSubType;
    }

    public int getCheckModeType() {
        return checkModeType;
    }

    public int getCheckSubType() {
        return checkSubType;
    }

    public void Draw (Canvas canvas){
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
        canvas.drawRect(0,0,x,y,paint);
//
//        paint.setColor(color);
//

        canvas.drawBitmap(next, nextW, nextH, paint);
        canvas.drawBitmap(home, homeW, homeH, null);
        canvas.drawBitmap(back, backW, backH, null);
        if(checkModeType==1) {
            canvas.drawBitmap(notArcMode, ArcModeW, ArcModeH, null);
            canvas.drawBitmap(surMode, surModeW, surModeH, null);
        }
        else if(checkModeType==2){
            canvas.drawBitmap(arcMode, ArcModeW, ArcModeH, null);
            canvas.drawBitmap(notSurMode, surModeW, surModeH, null);
        }

        if(checkSubType == 1) {
            canvas.drawBitmap(round, roundW, roundH, null);
            canvas.drawBitmap(notKills, KillsW, KillsH, null);
            canvas.drawBitmap(notScore, ScoreW, ScoreH, null);
        }
        else if(checkSubType == 2) {
            canvas.drawBitmap(notRound, roundW, roundH, null);
            canvas.drawBitmap(kills, KillsW, KillsH, null);
            canvas.drawBitmap(notScore, ScoreW, ScoreH, null);
        }
        else if(checkSubType == 3){
            canvas.drawBitmap(notRound, roundW, roundH, null);
            canvas.drawBitmap(notKills, KillsW, KillsH, null);
            canvas.drawBitmap(score, ScoreW, ScoreH, null);
        }
//        canvas.drawBitmap(quit, x/2- 350, y/2-100, null);
    }

    public void restoreState(Bundle savedInstanceState) {
        x = savedInstanceState.getInt("creditsx");
        y = savedInstanceState.getInt("creditsy");

        next = BitmapFactory.decodeResource(getResources(),R.drawable.next);
        //menu = BitmapFactory.decodeResource(getResources(),R.drawable.menu);
        back = BitmapFactory.decodeResource(getResources(),R.drawable.back);
    }

    public void saveState(Bundle outState) {
        outState.putInt("creditsx", x);
        outState.putInt("creditsy", y);
    }

    public int getScoreW() {
        return ScoreW;
    }

    public int getScoreH() {
        return ScoreH;
    }

    public int getKillsW() {
        return KillsW;
    }

    public int getKillsH() {
        return KillsH;
    }

    public int getRoundW() {
        return roundW;
    }

    public int getRoundH() {
        return roundH;
    }

    public int getSurModeW() {
        return surModeW;
    }

    public int getSurModeH() {
        return surModeH;
    }

    public int getArcModeW() {
        return ArcModeW;
    }

    public int getArcModeH() {
        return ArcModeH;
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



    public int notScoreW() {
        return notScore.getWidth();
    }

    public int notScoreH() {
        return notScore.getHeight();
    }

    public int notKillsW() {
        return notKills.getWidth();
    }

    public int notKillsH() {
        return notKills.getHeight();
    }

    public int notRoundW() {
        return roundW;
    }

    public int notRoundH() {
        return round.getHeight();
    }

    public int notSurModeW() {
        return surMode.getWidth();
    }

    public int notSurModeH() {
        return surMode.getHeight();
    }

    public int notArcModeW() {
        return notArcMode.getWidth();
    }

    public int notArcModeH() {
        return notArcMode.getHeight();
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
