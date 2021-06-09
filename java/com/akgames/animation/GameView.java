package com.akgames.animation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import static android.content.Context.MODE_PRIVATE;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private Context context;
    private Background background;
    private FireBTN fireBTN;
    private int joystickPointerId = 20;
    private int joystick2PointerId = 20;
    private Joystick joystick;
    private Joystick joystick2;
    private float joyX, joyY;
    private Player player;
    private Guns guns;
    private Main_menu mainMenu;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Bullets> spellList = new ArrayList<Bullets>();
    private List<Drops> perkList = new ArrayList<Drops>();
    private int numberOfSpellsToCast = 0;
    private GameOver gameOver;
    private Credits Credits;
    private RoundOver roundEnd;
    private Instruction Instructions;
    private Performance performance;
    private Canvas canvas;
    private boolean fired, onebullet, survivalMode, atMenu, dead, arcadeMode, credits,instructions;
    private DisplayMetrics displayMetrics;
    private int btnCheck, modeCheck;
    private int width, height;
    private int mActivePointerId;
    private int pointerIndex;
    private int kills, round,roundMaxEnemies, roundSpawn, roundEnemyTrack, roundKills;
    private long totalPoints, points;
    private boolean roundOver;
    private int clip, maxClip;
    private int maxhealth;
    private int touchTrack;
    private float roundArc, scaleSize;
    private int firedTrack, touchfired;
    private int[] weapons = {1,0,0,0,0};//check to see if weapons have been bought
    private int weaponLastClicked;
    private int weaponIndex;
    private int instaKill, ammoUnlim;
    private boolean blowNuke;
    private int bulletPointerId;
    private String userProfile;
    private boolean getdb;
    Activity activity;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Query reference1 = null;
    List<LeaderBoardInput> scores = new ArrayList<LeaderBoardInput>();
    private int rank;
    private int shownRank;
    private int lead;
    private int aktime;
    private int uzitime;
    private int revtime;
    private int shotguntime;
    private String strInstaKill;
    private SharedPreferences sharedPreferences;
    private firesounds firesounds1,firesounds2,firesounds3;
    private int soundCount;
    private int delayTime;
    private MainActivity2 MainActivity2;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GameView(Context context, long totalPoints, int round, int kills, boolean dead, int modeCheck) {
        super(context);
        try {
            activity = activity.getParent();
        }catch(Exception e){

        }
        MainActivity2 = new MainActivity2();

        delayTime = 20;
//        firesounds1 = new firesounds(getContext());
//        firesounds2 = new firesounds(getContext());
//        firesounds3 = new firesounds(getContext());
        soundCount = 1;

        sharedPreferences = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        userProfile = sharedPreferences.getString("user","guest");

        this.dead = dead;
        if(dead){
            getdb = false;
            atMenu = false;
            credits = false;
            instructions = false;
            survivalMode = false;
            roundOver = false;
        }
        else{
            getdb = false;
            atMenu = true;
            credits = false;
            instructions = false;
            survivalMode = false;
            roundOver = false;
        }
        this.context = context;
        joystick2PointerId = 20;
        joystickPointerId = 20;
         displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        this.height = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        System.out.println(height);
        if (height>1400){
            scaleSize = 2;//height/1200;
        }
        else{
           scaleSize = 1;
        }
        this.modeCheck = modeCheck;

        roundEnd = new RoundOver(context, height, width);
        Credits = new Credits(context, width,height);
        mainMenu = new Main_menu(context, width,height);
        Instructions = new Instruction(context, width,height);
        performance = new Performance(context, gameLoop);
        gameOver = new GameOver(context, width, height);
        joystick = new Joystick(context,0, height, 70);
        joystick2 = new Joystick(context,width-300, height, 70);
        background = new Background(context);
        player = new Player(context, joystick, width, height);
        player = new Player(context, joystick, joystick2, width, height);
        // Initialize game objects



        guns = new Guns(context, player, width/2, height/2);
        fireBTN = new FireBTN(context,width,height);

        //initialize player's round information to track
        onebullet = true;
        touchfired = 0;
        touchTrack = 5;
        this.kills = kills;
        this.round = round;
        roundArc = (float)0.5;
        roundMaxEnemies = 5;
        roundSpawn = 0;
        roundEnemyTrack = 0;
        roundKills = 0;
        points = 0;
        clip = 7;
        maxhealth = 5;
        maxClip = clip;
        weaponLastClicked = 1;
        weaponIndex = 0;
        blowNuke = false;
        instaKill = 0;
        ammoUnlim = 0;
        setFocusable(true);
        this.totalPoints = totalPoints;
        shownRank=16;
        rank=1;
        aktime = 0;
        uzitime = 0;
        revtime = 0;
        shotguntime = 0;
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
    }

    public void setUser(String user) {
        this.userProfile = user;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Handle user input touch event actions
        if(survivalMode) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = event.getPointerId(0);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    float x1 = event.getX(pointerIndex);
                    float y1 = event.getY(pointerIndex);
//
                    if (joystick.isPressed((double) x1, (double) y1)) {
                        joystickPointerId = 0;
                        //joystickPointerId = event.getPointerId(event.getActionIndex());
                        joystick.setIsPressed(true);
                        joyX = (float) (joystick.getActuatorX());
                        joyY = (float) (joystick.getActuatorY());
                        touchTrack = 0;

                    }
                    else if (x1 > fireBTN.getFireBTNPx() && x1 <fireBTN.getFireBTNPx()+fireBTN.getFireBTNW()&& y1 > fireBTN.getFireBTNPy()&& y1 < fireBTN.getFireBTNPy()+ fireBTN.getFireBTNW()) {
                        // clicked fire
                        if(onebullet) {
                            fired = true;
                            touchfired = 1;
                            bulletPointerId = 0;
                            onebullet = false;
                            btnCheck = 5;
                        }
                        onebullet = true;
                    }
                    else if (x1 > fireBTN.getSwapPx() && x1 <fireBTN.getSwapPx()+fireBTN.getSwapW()&& y1 > fireBTN.getSwapPy()&& y1 < fireBTN.getSwapPy()+ fireBTN.getSwapW()) {
                        //System.out.println("hit swap gun");
                        //btnCheck = 5;
                        //probably could do a run override method :)
                        weaponIndex++;
                        if(weaponIndex==5){weaponIndex = 0;}

                        if(weapons[weaponIndex]==0){//check index
                            weaponIndex++;
                            if(weaponIndex==5){weaponIndex = 0;}

                            if(weapons[weaponIndex]==0){
                                weaponIndex++;
                                if(weaponIndex==5){weaponIndex = 0;}

                                if(weapons[weaponIndex]==0){
                                    weaponIndex++;
                                    if(weaponIndex==5){weaponIndex = 0;}

                                    if(weapons[weaponIndex]==0) {
                                        weaponIndex++;
                                        if (weaponIndex == 5) { weaponIndex = 0; }

                                        if (weapons[weaponIndex] == 0) {
                                            weaponIndex = 0;

                                        }
                                    }
                                }
                            }
                        }

                        guns.setWeaponIndex(weaponIndex);
                        if(weaponIndex==0){
                            clip = guns.getMaxPistolClip();
                        }
                        else if(weaponIndex==1){
                            clip = guns.getMaxRevClip();
                        }
                        else if(weaponIndex==2){
                            clip = guns.getMaxUziClip();
                        }
                        else if(weaponIndex==3){
                            clip = guns.getMaxShotClip();
                        }
                        else if(weaponIndex==4){
                            clip = guns.getMaxAkClip();
                        }
                    }
                    else if (x1 > fireBTN.getReloadPx() && x1 <fireBTN.getReloadPx()+fireBTN.getReloadW()&& y1 > fireBTN.getReloadPy()&& y1 < fireBTN.getReloadPy()+ fireBTN.getReloadW()) {
                        // Joystick was not previously, and is not pressed in this event -> cast spell
                        //System.out.println("hit reload");
                        if(weaponIndex==0){
                            clip = guns.getMaxPistolClip();
                        }
                        else if(weaponIndex==1){
                            clip = guns.getMaxRevClip();
                        }
                        else if(weaponIndex==2){
                            clip = guns.getMaxUziClip();
                        }
                        else if(weaponIndex==3){
                            clip = guns.getMaxShotClip();
                        }
                        else if(weaponIndex==4){
                            clip = guns.getMaxAkClip();
                        }
                        fired = false;
                        delayTime = 20;
                    }

                    return true;

                case MotionEvent.ACTION_POINTER_DOWN:
                    if (touchTrack == 1) {
                        if(weaponIndex !=2 || weaponIndex !=4) {
                            fired = false;
                            onebullet = false;
                        }
                        try {
                            mActivePointerId = event.getPointerId(0);
                            pointerIndex = event.findPointerIndex(mActivePointerId);
                            float x2 = event.getX(pointerIndex);
                            float y2 = event.getY(pointerIndex);
                            if (x2 > fireBTN.getFireBTNPx() && x2 <fireBTN.getFireBTNPx()+fireBTN.getFireBTNW()&& y2 > fireBTN.getFireBTNPy()&& y2 < fireBTN.getFireBTNPy()+ fireBTN.getFireBTNW()) {
                                // clicked fire
                                if(onebullet) {
                                    fired = true;
                                    touchfired = 1;

                                    bulletPointerId = 0;
                                    onebullet = false;
                                    btnCheck = 5;
                                }
                                onebullet = true;
                            }
                            else if (x2 > fireBTN.getSwapPx() && x2 <fireBTN.getSwapPx()+fireBTN.getSwapW()&& y2 > fireBTN.getSwapPy()&& y2 < fireBTN.getSwapPy()+ fireBTN.getSwapW()) {
                                //System.out.println("hit swap gun");
                                //btnCheck = 5;
                                //probably could do a run override method :)
                                weaponIndex++;
                                if(weaponIndex==5){weaponIndex = 0;}

                                if(weapons[weaponIndex]==0){//check index
                                    weaponIndex++;
                                    if(weaponIndex==5){weaponIndex = 0;}

                                    if(weapons[weaponIndex]==0){
                                        weaponIndex++;
                                        if(weaponIndex==5){weaponIndex = 0;}

                                        if(weapons[weaponIndex]==0){
                                            weaponIndex++;
                                            if(weaponIndex==5){weaponIndex = 0;}

                                            if(weapons[weaponIndex]==0) {
                                                weaponIndex++;
                                                if (weaponIndex == 5) { weaponIndex = 0; }

                                                if (weapons[weaponIndex] == 0) {
                                                    weaponIndex = 0;

                                                }
                                            }
                                        }
                                    }
                                }

                                guns.setWeaponIndex(weaponIndex);
                                if(weaponIndex==0){
                                    clip = guns.getMaxPistolClip();
                                }
                                else if(weaponIndex==1){
                                    clip = guns.getMaxRevClip();
                                }
                                else if(weaponIndex==2){
                                    clip = guns.getMaxUziClip();
                                }
                                else if(weaponIndex==3){
                                    clip = guns.getMaxShotClip();
                                }
                                else if(weaponIndex==4){
                                    clip = guns.getMaxAkClip();
                                }
                            }
                            else if (x2 > fireBTN.getReloadPx() && x2 <fireBTN.getReloadPx()+fireBTN.getReloadW()&& y2 > fireBTN.getReloadPy()&& y2 < fireBTN.getReloadPy()+ fireBTN.getReloadW()) {
                                // Joystick was not previously, and is not pressed in this event -> cast spell
                                //System.out.println("hit reload");
                                if(weaponIndex==0){
                                    clip = guns.getMaxPistolClip();
                                }
                                else if(weaponIndex==1){
                                    clip = guns.getMaxRevClip();
                                }
                                else if(weaponIndex==2){
                                    clip = guns.getMaxUziClip();
                                }
                                else if(weaponIndex==3){
                                    clip = guns.getMaxShotClip();
                                }
                                else if(weaponIndex==4){
                                    clip = guns.getMaxAkClip();
                                }
                                fired = false;
                                delayTime = 20;
                            }
                        } catch (Exception e) {

                        }
                    }
                    else if (touchTrack == 0 && joystickPointerId == 0) {
                        try {
                            mActivePointerId = event.getPointerId(joystickPointerId);
                            pointerIndex = event.findPointerIndex(mActivePointerId);
                            float x2 = event.getX(pointerIndex);
                            float y2 = event.getY(pointerIndex);

                            if (joystick.getIsPressed() && joystickPointerId == 0) {
                                touchTrack = 0;
                            }
                            else if (joystick.isPressed((double) x2, (double) y2)) {
                                // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                                joystickPointerId = event.getPointerId(event.getActionIndex());
                                joystick.setIsPressed(true);
                                joyX = (float) (joystick.getActuatorX());
                                joyY = (float) (joystick.getActuatorY());
                                touchTrack = 0;
                            }



                        } catch (Exception e) {

                        }
                    }
                    mActivePointerId = event.getPointerId(1);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    float x = event.getX(pointerIndex);
                    float y = event.getY(pointerIndex);

                    if (x > fireBTN.getFireBTNPx() && x <fireBTN.getFireBTNPx()+fireBTN.getFireBTNW()&& y > fireBTN.getFireBTNPy()&& y < fireBTN.getFireBTNPy()+ fireBTN.getFireBTNW()) {
                        // clicked fire
                        if(onebullet) {
                            fired = true;
                            touchfired = 1;

                            bulletPointerId = 0;
                            onebullet = false;
                            btnCheck = 5;
                        }
                        onebullet = true;
                    }
                    else if (x > fireBTN.getSwapPx() && x <fireBTN.getSwapPx()+fireBTN.getSwapW()&& y > fireBTN.getSwapPy()&& y < fireBTN.getSwapPy()+ fireBTN.getSwapW()) {
                        //System.out.println("hit swap gun");
                        //btnCheck = 5;
                        //probably could do a run override method :)
                        weaponIndex++;
                        if(weaponIndex==5){weaponIndex = 0;}

                        if(weapons[weaponIndex]==0){//check index
                            weaponIndex++;
                            if(weaponIndex==5){weaponIndex = 0;}

                            if(weapons[weaponIndex]==0){
                                weaponIndex++;
                                if(weaponIndex==5){weaponIndex = 0;}

                                if(weapons[weaponIndex]==0){
                                    weaponIndex++;
                                    if(weaponIndex==5){weaponIndex = 0;}

                                    if(weapons[weaponIndex]==0) {
                                        weaponIndex++;
                                        if (weaponIndex == 5) { weaponIndex = 0; }

                                        if (weapons[weaponIndex] == 0) {
                                            weaponIndex = 0;

                                        }
                                    }
                                }
                            }
                        }

                        guns.setWeaponIndex(weaponIndex);
                        if(weaponIndex==0){
                            clip = guns.getMaxPistolClip();
                        }
                        else if(weaponIndex==1){
                            clip = guns.getMaxRevClip();
                        }
                        else if(weaponIndex==2){
                            clip = guns.getMaxUziClip();
                        }
                        else if(weaponIndex==3){
                            clip = guns.getMaxShotClip();
                        }
                        else if(weaponIndex==4){
                            clip = guns.getMaxAkClip();
                        }
                    }
                    else if (x > fireBTN.getReloadPx() && x <fireBTN.getReloadPx()+fireBTN.getReloadW()&& y > fireBTN.getReloadPy()&& y < fireBTN.getReloadPy()+ fireBTN.getReloadW()) {
                        // Joystick was not previously, and is not pressed in this event -> cast spell
                        //System.out.println("hit reload");
                        if(weaponIndex==0){
                            clip = guns.getMaxPistolClip();
                        }
                        else if(weaponIndex==1){
                            clip = guns.getMaxRevClip();
                        }
                        else if(weaponIndex==2){
                            clip = guns.getMaxUziClip();
                        }
                        else if(weaponIndex==3){
                            clip = guns.getMaxShotClip();
                        }
                        else if(weaponIndex==4){
                            clip = guns.getMaxAkClip();
                        }
                        fired = false;
                        delayTime = 20;
                    }

                    else if (joystick.getIsPressed()&&joystickPointerId !=0) {
                        touchTrack = 1;

                    }
                    else if (joystick.isPressed((double) x, (double) y)) {
                        // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                        joystickPointerId = event.getPointerId(event.getActionIndex());
                        joystick.setIsPressed(true);
                        joyX = (float) (joystick.getActuatorX());
                        joyY = (float) (joystick.getActuatorY());
                        touchTrack = 1;
                    }

                    return true;

                case MotionEvent.ACTION_MOVE:
                    if (joystick.getIsPressed()) {
                        // Joystick was pressed previously and is now moved
                        if(touchTrack == 0) {
                            mActivePointerId = event.getPointerId(0);
                            pointerIndex = event.findPointerIndex(mActivePointerId);
                            x = event.getX(pointerIndex);
                            y = event.getY(pointerIndex);
                            joystick.setActuator((double) x, (double) y);
                            //joystick.setActuator((double) event.getX(), (double) event.getY());
                            joyX = (float) (joystick.getActuatorX());
                            joyY = (float) (joystick.getActuatorY());
                        }
                        else if(touchTrack == 1) {
                                try {
                                    mActivePointerId = event.getPointerId(1);
                                    pointerIndex = event.findPointerIndex(mActivePointerId);
                                    x = event.getX(pointerIndex);
                                    y = event.getY(pointerIndex);
                                    joystick.setActuator((double) x, (double) y);
                                    joyX = (float) (joystick.getActuatorX());
                                    joyY = (float) (joystick.getActuatorY());
                                }catch(Exception e){
                                    mActivePointerId = event.getPointerId(0);
                                    pointerIndex = event.findPointerIndex(mActivePointerId);
                                    x = event.getX(pointerIndex);
                                    y = event.getY(pointerIndex);
                                    joystick.setActuator((double) x, (double) y);
                                    joyX = (float) (joystick.getActuatorX());
                                    joyY = (float) (joystick.getActuatorY());
                                }
                            }
                    }

                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    if (joystickPointerId == event.getPointerId(event.getActionIndex())) {
                        joystick.setIsPressed(false);
                        joystick.resetActuator();
                    }

                    if(bulletPointerId == event.getPointerId(event.getActionIndex())){
                        if (!onebullet) {

                                        onebullet = true;
                        }
                        fired = false;
                    }

                    return true;
            }
        }
        else if(credits){
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    try{
                    mActivePointerId = event.getPointerId(0);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    float x1 = event.getX(pointerIndex);
                    float y1 = event.getY(pointerIndex);
                    if (x1 < Credits.getBackW()+Credits.notBackW() && y1 < Credits.getBackH()+Credits.notBackH()) {
                        if(shownRank<=16){
                            rank=1;
                            shownRank=16;
                        }
                        else{
                            rank-=15;
                            shownRank-=15;
                        }

                        // Joystick was not previously, and is not pressed in this event -> cast spell
                    }
                    else if (x1 > Credits.getHomeW() && y1 < Credits.getHomeH()+Credits.notHomeH()&& x1 < Credits.getHomeW()+Credits.notHomeW()) {
                        atMenu = true;
                        //getdb = true;
                        rank=1;
                        shownRank=1;
                        credits = false;
                        // Joystick was not previously, and is not pressed in this event -> cast spell
                    }
                    else if (x1 > Credits.getNextW() && y1 < Credits.getNextH()+Credits.notNextH() && scores.size()>shownRank) {
                        shownRank+=15;
                    }
                    else if (x1 > Credits.getSurModeW() && x1 < Credits.getSurModeW()+Credits.notSurModeW() && y1 > Credits.getSurModeH() && y1 < Credits.getSurModeH()+Credits.notSurModeH()) {
                        Credits.setCheckModeType(1);
                        //hit survival
                        shownRank=16;
                        rank=1;
                        getdb = true;
                        scores.clear();
                        if(!userProfile.equals("guest")) {
                            leaderboardupdate();
                        }
                    }
                    else if (x1 > Credits.getArcModeW() && x1 < Credits.getArcModeW()+Credits.notArcModeW() && y1 > Credits.getArcModeH() && y1 < Credits.getArcModeH()+Credits.notArcModeH()) {
                        Credits.setCheckModeType(2);
                        //hit survival
                        shownRank=16;
                        rank=1;
                        getdb = true;
                        scores.clear();
                        if(!userProfile.equals("guest")) {
                            leaderboardupdate();
                        }
                    }
                    else if (x1 > Credits.getRoundW() && x1 < Credits.getRoundW()+Credits.notRoundW() && y1 > Credits.getRoundH() && y1 < Credits.getRoundH()+Credits.notRoundH()) {
                        Credits.setCheckSubType(1);
                        //hit round
                        shownRank=16;
                        rank=1;
                        getdb = true;
                        scores.clear();
                        if(!userProfile.equals("guest")) {
                            leaderboardupdate();
                        }
                    }
                    else if (x1 > Credits.getKillsW() && x1 < Credits.getKillsW()+Credits.notKillsW() && y1 > Credits.getKillsH() && y1 < Credits.getKillsH()+Credits.notKillsH()) {
                        Credits.setCheckSubType(2);
                        //hit kills
                        shownRank=16;
                        rank=1;
                        getdb = true;
                        scores.clear();
                        if(!userProfile.equals("guest")) {
                            leaderboardupdate();
                        }
                    }
                    else if (x1 > Credits.getScoreW() && x1 < Credits.getScoreW()+Credits.notScoreW() && y1 > Credits.getScoreH() && y1 < Credits.getScoreH()+Credits.notScoreH()) {
                        Credits.setCheckSubType(3);
                        //hit score
                        shownRank=16;
                        rank=1;
                        getdb = true;
                        scores.clear();
                        if(!userProfile.equals("guest")) {
                            leaderboardupdate();
                        }
                    }
                    return true;
                    }catch(Exception a){}
            }
        }
        else if(instructions){
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = event.getPointerId(0);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    float x1 = event.getX(pointerIndex);
                    float y1 = event.getY(pointerIndex);
                    if (x1 < Instructions.getBackW()+Instructions.notBackW() && y1 < Instructions.getBackH()+Instructions.notBackH()) {

                        Instructions.setDisplaycode(0);
                    }
                    else if (x1 > Instructions.getHomeW() && y1 < Instructions.getHomeH()+Instructions.notHomeH()&& x1 < Instructions.getHomeW()+Instructions.notHomeW()) {
                        //getdb = true;

                        instructions = false;
                        atMenu = true;
                        // Joystick was not previously, and is not pressed in this event -> cast spell
                    }
                    else if (x1 > Instructions.getNextW() && y1 < Instructions.getNextH()+Instructions.notNextH()) {
                        Instructions.setDisplaycode(1);
                    }

                    return true;
            }
        }
        else if(arcadeMode) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:

                    mActivePointerId = event.getPointerId(0);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    float x1 = event.getX(pointerIndex);
                    float y1 = event.getY(pointerIndex);

                        if (joystick.isPressed((double) x1, (double) y1)) {
                        // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                        joystickPointerId = 0;
                        touchTrack = 0;
                        joystick.setActuator((double) x1, (double) y1);
                        joystick.setIsPressed(true);


                    } else if (joystick2.getIsPressed()) {
                        joystick2PointerId = 0;
                        touchTrack = 1;
                        joystick2.setActuator((double) x1, (double) y1);
                        joystick2.setIsPressed(true);
                        joyX = (float) (joystick2.getActuatorX());
                        joyY = (float) (joystick2.getActuatorY());
                        fired = true;
                    } else if (joystick2.isPressed((double) x1, (double) y1)) {
                        // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                        joystick2PointerId = 0;
                        touchTrack = 1;
                        joystick2.setActuator((double) x1, (double) y1);
                        joystick2.setIsPressed(true);
                        joyX = (float) (joystick2.getActuatorX());
                        joyY = (float) (joystick2.getActuatorY());
                        fired = true;
                    }
                    return true;

                case MotionEvent.ACTION_POINTER_DOWN:
                    try {
                        mActivePointerId = event.getPointerId(0);
                        pointerIndex = event.findPointerIndex(mActivePointerId);
                        x1 = event.getX(pointerIndex);
                        y1 = event.getY(pointerIndex);

//                        if (joystick.getIsPressed()) {
//                            joystickPointerId = mActivePointerId;
//                            touchTrack = 0;
//                            joystick.setActuator((double) x1, (double) y1);
//                            joystick.setIsPressed(true);
//                        } else
                            if (joystick.isPressed((double) x1, (double) y1)) {
                            // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                            joystickPointerId = mActivePointerId;
                            touchTrack = 0;
                            joystick.setActuator((double) x1, (double) y1);
                            joystick.setIsPressed(true);
                            //joyX = (float) (joystick.getActuatorX());
                            //joyY = (float) (joystick.getActuatorY());
                            //fired = false;

                        } else if (joystick2.getIsPressed()) {
                            joystick2PointerId = mActivePointerId;
                            touchTrack = 1;
                            joystick2.setActuator((double) x1, (double) y1);
                            joystick2.setIsPressed(true);
                            joyX = (float) (joystick2.getActuatorX());
                            joyY = (float) (joystick2.getActuatorY());
                            fired = true;
                        } else if (joystick2.isPressed((double) x1, (double) y1)) {
                            // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                            joystick2PointerId = mActivePointerId;
                            touchTrack = 1;
                            joystick2.setActuator((double) x1, (double) y1);
                            joystick2.setIsPressed(true);
                            joyX = (float) (joystick2.getActuatorX());
                            joyY = (float) (joystick2.getActuatorY());
                            fired = true;
                        }
                    } catch (Exception e) {

                    }
                    try {
                        mActivePointerId = event.getPointerId(1);
                        pointerIndex = event.findPointerIndex(mActivePointerId);
                        float x2 = event.getX(pointerIndex);
                        float y2 = event.getY(pointerIndex);

                        if (touchTrack == 1) {
                            if (joystick.isPressed((double) x2, (double) y2)) {
                                // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                                joystickPointerId = mActivePointerId;
                                joystick.setActuator((double) x2, (double) y2);
                                joystick.setIsPressed(true);
                                //joyX = (float) (joystick.getActuatorX());
                                //joyY = (float) (joystick.getActuatorY());
                                //fired = false;

                            }
                        } else if (touchTrack == 0) {
                            if (joystick2.isPressed((double) x2, (double) y2)) {
                                // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                                joystick2PointerId = mActivePointerId;
                                joystick2.setActuator((double) x2, (double) y2);
                                joystick2.setIsPressed(true);
                                joyX = (float) (joystick2.getActuatorX());
                                joyY = (float) (joystick2.getActuatorY());
                                fired = true;
                            }
                        }
                    } catch (Exception e) {

                    }

                case MotionEvent.ACTION_MOVE:
//                    try {
//                        mActivePointerId = event.getPointerId(0);
//                        pointerIndex = event.findPointerIndex(mActivePointerId);
//                        float x2 = event.getX(pointerIndex);
//                        float y2 = event.getY(pointerIndex);
//
//                        if (touchTrack == 0) {
//                            if (joystick.getIsPressed()) {
//                                joystickPointerId = mActivePointerId;
//                                joystick.setActuator((double) x2, (double) y2);
//                                joystick.setIsPressed(true);
//                            } else if (joystick.isPressed((double) x2, (double) y2)) {
//                                // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
//                                joystickPointerId = event.getPointerId(event.getActionIndex());
//                                joystick.setActuator((double) x2, (double) y2);
//                                joystick.setIsPressed(true);
//                                //joyX = (float) (joystick.getActuatorX());
//                                //joyY = (float) (joystick.getActuatorY());
//                                //fired = false;
//
//                            }
//                        } else if (touchTrack == 1) {
//                            if (joystick2.getIsPressed()) {
//                                //joyX = (float) (joystick2.getActuatorX());
//                                //joyY = (float) (joystick2.getActuatorY());
//                                joystick2PointerId = mActivePointerId;
//                                joystick2.setActuator((double) x2, (double) y2);
//                                joystick2.setIsPressed(true);
//                                joyX = (float) (joystick2.getActuatorX());
//                                joyY = (float) (joystick2.getActuatorY());
//                                fired = true;
//                            } else if (joystick2.isPressed((double) x2, (double) y2)) {
//                                //joyX = (float) (joystick2.getActuatorX());
//                                //joyY = (float) (joystick2.getActuatorY());
//                                joystick2PointerId = mActivePointerId;
//                                joystick2.setActuator((double) x2, (double) y2);
//                                joystick2.setIsPressed(true);
//                                joyX = (float) (joystick2.getActuatorX());
//                                joyY = (float) (joystick2.getActuatorY());
//                                fired = true;
//                            }
//
//                        }
//                    } catch (Exception e) {
//
//                    }
//
//                    try {
//                        mActivePointerId = event.getPointerId(1);
//                        pointerIndex = event.findPointerIndex(mActivePointerId);
//                        float x2 = event.getX(pointerIndex);
//                        float y2 = event.getY(pointerIndex);
//
//                        if (touchTrack == 1) {
////                            pointerIndex = event.findPointerIndex(joystickPointerId);
////                            x1 = event.getX(pointerIndex);
////                            y1 = event.getY(pointerIndex);
//                            if (joystick.getIsPressed()) {
//                                joystickPointerId = mActivePointerId;
//                                joystick.setActuator((double) x2, (double) y2);
//                                joystick.setIsPressed(true);
//                            } else if (joystick.isPressed((double) x2, (double) y2)) {
//                                // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
//                                joystickPointerId = mActivePointerId;
//                                joystick.setActuator((double) x2, (double) y2);
//                                joystick.setIsPressed(true);
//                                //joyX = (float) (joystick.getActuatorX());
//                                //joyY = (float) (joystick.getActuatorY());
//                                //fired = false;
//
//                            }
//                        } else if (touchTrack == 0) {
//                            if (joystick2.getIsPressed()) {
//                                //joyX = (float) (joystick2.getActuatorX());
//                                //joyY = (float) (joystick2.getActuatorY());
//                                joystick2PointerId = mActivePointerId;
//                                joystick2.setActuator((double) x2, (double) y2);
//                                joystick2.setIsPressed(true);
//                                joyX = (float) (joystick2.getActuatorX());
//                                joyY = (float) (joystick2.getActuatorY());
//                                fired = true;
//                            }
//                        }
//                    } catch (Exception e) {
//
//                    }

                    if (joystick2.getIsPressed()) {
                        //mActivePointerId = event.getPointerId(joystick2PointerId);
                        pointerIndex = event.findPointerIndex(joystick2PointerId);
                        float x2 = event.getX(pointerIndex);
                        float y2 = event.getY(pointerIndex);

                        joystick2.setActuator((double) x2, (double) y2);
                        joystick2.setIsPressed(true);
                        joyX = (float) (joystick2.getActuatorX());
                        joyY = (float) (joystick2.getActuatorY());
                    }

                    if (joystick.getIsPressed()) {
                        //mActivePointerId = event.getPointerId(joystickPointerId);
                        pointerIndex = event.findPointerIndex(joystickPointerId);
                        float x2 = event.getX(pointerIndex);
                        float y2 = event.getY(pointerIndex);

                        joystick.setActuator((double) x2, (double) y2);
                        joystick.setIsPressed(true);
                    }
                    return true;

                case MotionEvent.ACTION_UP:

                case MotionEvent.ACTION_POINTER_UP:


                    //mActivePointerId = event.getPointerId(0);
                    //pointerIndex = event.findPointerIndex(mActivePointerId);

                    if (event.getPointerId(event.getActionIndex()) == 0) {
                        if (touchTrack == 0 && event.getPointerId(event.getActionIndex()) == joystickPointerId) {
                            joystick.setIsPressed(false);
                            joystick.resetActuator();
                        } else if (touchTrack == 1 && event.getPointerId(event.getActionIndex()) == joystick2PointerId) {
                            joystick2.setIsPressed(false);
                            joystick2.resetActuator();
                        }

                    }
                    else if (event.getPointerId(event.getActionIndex()) == 1) {
                        if (touchTrack == 1 && event.getPointerId(event.getActionIndex()) == joystickPointerId) {
                            joystick.setIsPressed(false);
                            joystick.resetActuator();
                        } else if (touchTrack == 0 && event.getPointerId(event.getActionIndex()) == joystick2PointerId) {
                            joystick2.setIsPressed(false);
                            joystick2.resetActuator();
                        }


                    }



                    return true;
            }
        }
        else if(dead) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = event.getPointerId(0);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    float x1 = event.getX(pointerIndex);
                    float y1 = event.getY(pointerIndex);
                    if (y1> gameOver.getAgainH()&&y1< gameOver.getAgainH()+gameOver.getAgain()){
                        //play
                        if(modeCheck==1) {
                            onebullet = true;
                            touchfired = 0;
                            touchTrack = 5;
                            kills = 0;
                            round = 1;
                            roundArc = (float)0.5;
                            roundMaxEnemies = 5;
                            roundSpawn = 0;
                            roundEnemyTrack = 0;
                            roundKills = 0;
                            points = 0;
                            weaponIndex = 0;
                            blowNuke = false;
                            instaKill = 0;
                            ammoUnlim = 0;
                            totalPoints = 0;
                            aktime = 0;
                            uzitime = 0;
                            revtime = 0;
                            shotguntime = 0;
                            kills = 0;
                            round = 1;
                            roundMaxEnemies = 5;
                            roundSpawn = 0;
                            roundEnemyTrack = 0;
                            roundKills = 0;
                            points = 0;
                            clip = 7;
                            maxhealth = 5;
                            maxClip = clip;
                            joystickPointerId = 20;
                            joystick2PointerId = 20;
                            roundEnd = new RoundOver(context, height, width);
                            joystick = new Joystick(context,0, height, 70);
                            player = new Player(context, joystick, width, height);
                            guns = new Guns(context, player, width/2, height/2);
                            player.setHealthPoint(maxhealth);
                            survivalMode = true;
                            dead = false;
                        }
                        else if(modeCheck==2) {
                            onebullet = true;
                            touchfired = 0;
                            touchTrack = 5;
                            kills = 0;
                            round = 1;
                            roundArc = (float)0.5;
                            roundMaxEnemies = 5;
                            roundSpawn = 0;
                            roundEnemyTrack = 0;
                            roundKills = 0;
                            points = 0;
                            weaponIndex = 0;
                            blowNuke = false;
                            instaKill = 0;
                            ammoUnlim = 0;
                            totalPoints = 0;

                            aktime = 0;
                            uzitime = 0;
                            revtime = 0;
                            shotguntime = 0;


                            kills = 0;
                            round = 1;
                            roundMaxEnemies = 5;
                            roundSpawn = 0;
                            roundEnemyTrack = 0;
                            roundKills = 0;
                            points = 0;
                            clip = 7;
                            maxhealth = 5;
                            maxClip = clip;
                            joystickPointerId = 20;
                            joystick2PointerId = 20;
                            roundEnd = new RoundOver(context, height, width);

                            //player = new Player(context, joystick, width, height);

                            joystick2PointerId = 20;
                            joystickPointerId = 20;
                            joystick = new Joystick(context,0, height, 70);
                            joystick2 = new Joystick(context,width-300, height, 70);
                            //player = new Player(context, joystick, width, height);
                            player = new Player(context, joystick, joystick2, width, height);
                            // Initialize game objects
                            guns = new Guns(context, player, width/2, height/2);
                            dead = false;
                            arcadeMode = true;

                            //initialize player's round information to track
                        }
                        //player = new Player(context, joystick, width, height);
                    }
                    else if (y1> gameOver.getQuitH()&&y1< gameOver.getQuitH()+gameOver.getQuit()){
                        //quit game
                        atMenu = true;
                        survivalMode = false;
                        arcadeMode = false;
                        dead = false;
                        //tester
                    }

                    return true;

                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
            }
        }
        else if(roundOver) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = event.getPointerId(0);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    float x1 = event.getX(pointerIndex);
                    float y1 = event.getY(pointerIndex);

                    if (y1> roundEnd.getPlayH()&&x1> roundEnd.getPlayW()&&x1< roundEnd.getPlayW()+roundEnd.getPlay()){
                        //play
                        //System.out.println("hit play");
                        roundOver = false;
                        if(modeCheck==1) {
                            survivalMode = true;
                        }
                        else if (modeCheck == 2){
                            arcadeMode = true;
                        }
                        player.setPositionX(width/2);
                        player.setPositionY(height/2);
                    }
                    else if (y1< roundEnd.getDamageIncreaseH()+roundEnd.DamageIncreaseH()&&y1> roundEnd.getDamageIncreaseH()&&
                            x1> roundEnd.getDamageIncreaseW()&&x1< roundEnd.getDamageIncreaseW()+roundEnd.DamageIncreaseW()){
                        //System.out.println("hit dam incr");
                            if(weaponLastClicked == 1) {
                                if(points>=roundEnd.getCostDamage()) {
                                    points -= roundEnd.getCostDamage();
                                    roundEnd.setPoints(points);
                                    roundEnd.setCostDamage(roundEnd.getCostDamage() * 2);
                                    guns.setPistolDamage((float) (guns.getPistolDamage() + 1.5));
                                }
                            }
                            else if(weaponLastClicked == 2) {
                                if(points>=roundEnd.getRevCostDamage()) {
                                    points -= roundEnd.getRevCostDamage();
                                    roundEnd.setPoints(points);
                                    roundEnd.setRevCostDamage(roundEnd.getRevCostDamage() * 2);
                                    guns.setRevDamage((float) (guns.getRevDamage() + 1.5));
                                }
                            }
                            else if(weaponLastClicked == 3) {
                                if(points>=roundEnd.getUziCostDamage()) {
                                    points -= roundEnd.getUziCostDamage();
                                    roundEnd.setPoints(points);
                                    roundEnd.setUziCostDamage(roundEnd.getUziCostDamage() * 2);
                                    guns.setUziDamage((float) (guns.getUziDamage() + 1.5));
                                }
                            }
                            else if(weaponLastClicked == 4) {
                                if(points>=roundEnd.getShotCostDamage()) {
                                    points -= roundEnd.getShotCostDamage();
                                    roundEnd.setPoints(points);
                                    roundEnd.setShotCostDamage(roundEnd.getShotCostDamage() * 2);
                                    guns.setShotDamage((float) (guns.getShotDamage() + 1.5));
                                }
                            }
                            else if(weaponLastClicked == 5) {
                                if(points>=roundEnd.getAkCostDamage()) {
                                    points -= roundEnd.getAkCostDamage();
                                    roundEnd.setPoints(points);
                                    roundEnd.setAkCostDamage(roundEnd.getAkCostDamage() * 2);
                                    guns.setAkDamage((float) (guns.getAkDamage() + 1.5));
                                }
                            }
                    }

                    else if (y1< roundEnd.getClipIncreaseH()+roundEnd.ClipIncreaseH()&&y1> roundEnd.getClipIncreaseH()&&
                            x1> roundEnd.getClipIncreaseW()&&x1< roundEnd.getClipIncreaseW()+roundEnd.ClipIncreaseW()){
                        //System.out.println("hit clip incr");
                        if(weaponLastClicked == 1) {
                            if(points>=roundEnd.getCostClip()) {
                                points -= roundEnd.getCostClip();
                                roundEnd.setPoints(points);
                                roundEnd.setCostClip(roundEnd.getCostClip() +100);
                                guns.setMaxPistolClip(guns.getMaxPistolClip()+1);
                                guns.setPistolClip(guns.getMaxPistolClip());
                            }
                        }
                        else if(weaponLastClicked == 2) {
                            if(points>=roundEnd.getRevCostClip()) {
                                points -= roundEnd.getRevCostClip();
                                roundEnd.setPoints(points);
                                roundEnd.setRevCostClip(roundEnd.getRevCostClip()+100);
                                guns.setMaxRevClip(guns.getMaxRevClip()+1);
                                guns.setRevClip(guns.getMaxRevClip());
                            }
                        }
                        else if(weaponLastClicked == 3) {
                            if(points>=roundEnd.getUziCostDamage()) {
                                points -= roundEnd.getUziCostDamage();
                                roundEnd.setPoints(points);
                                roundEnd.setUziCostClip(roundEnd.getUziCostClip() +100);
                                guns.setMaxUziClip(guns.getMaxUziClip()+1);
                                guns.setUziClip(guns.getMaxUziClip());
                            }
                        }
                        else if(weaponLastClicked == 4) {
                            if(points>=roundEnd.getShotCostClip()) {
                                points -= roundEnd.getShotCostClip();
                                roundEnd.setPoints(points);
                                roundEnd.setShotCostClip(roundEnd.getShotCostClip() +100);
                                guns.setMaxShotClip(guns.getMaxShotClip()+1);
                                guns.setShotClip(guns.getMaxShotClip());
                            }
                        }
                        else if(weaponLastClicked == 5) {
                            if(points>=roundEnd.getAkCostClip()) {
                                points -= roundEnd.getAkCostClip();
                                roundEnd.setPoints(points);
                                roundEnd.setAkCostClip(roundEnd.getAkCostClip() +100);
                                guns.setMaxAkClip(guns.getMaxAkClip()+1);
                                guns.setAkClip(guns.getMaxAkClip());
                            }
                        }
                    }

                    else if (y1< roundEnd.getHealH()+roundEnd.HealH()&&y1> roundEnd.getHealH()&&
                            x1> roundEnd.getHealW()&&x1< roundEnd.getHealW()+roundEnd.HealW()){
                        //heal
                        //System.out.println("hit heal");
                        if(points>=roundEnd.getCostHealth()) {
                            points -= roundEnd.getCostHealth();
                            roundEnd.setPoints(points);
                            roundEnd.setCostHealth(roundEnd.getCostHealth() * 2);
                            player.setHealthPoint(maxhealth);
                        }
                    }

                    else if (y1< roundEnd.getHealthIncreaseH()+roundEnd.HealthIncreaseH()&&y1> roundEnd.getHealthIncreaseH()&&
                            x1> roundEnd.getHealthIncreaseW()&&x1< roundEnd.getHealthIncreaseW()+roundEnd.HealthIncreaseW()){
                        //System.out.println("hit incr health");
                        if(points>=roundEnd.getCostIncHealth()) {
                            points -= roundEnd.getCostIncHealth();
                            roundEnd.setPoints(points);
                            roundEnd.setCostIncHealth(roundEnd.getCostIncHealth() * 3);
                            maxhealth++;
                            player.setHealthPoint(player.getHealthPoint() + 1);
                        }
                    }

                    else if (y1< roundEnd.getPistolUpH()+roundEnd.PistolUpH()&&y1> roundEnd.getPistolUpH()&&
                            x1> roundEnd.getPistolUpW()&&x1< roundEnd.getPistolUpW()+roundEnd.PistolUpW()){
                        weaponLastClicked = 1;
                        //System.out.println("hit pistol");
                    }//hit pistol

                    else if (y1< roundEnd.getRevUpH()+roundEnd.RevUpH()&&y1> roundEnd.getRevUpH()&&
                            x1> roundEnd.getRevUpW()&&x1< roundEnd.getRevUpW()+roundEnd.RevUpW()){
                        //System.out.println("hit rev");
                        if(roundEnd.getRev()){
                            if(points>=roundEnd.getRevCost()) {
                                roundEnd.setRev(false);
                                points -= roundEnd.getRevCost();
                                roundEnd.setPoints(points);
                                weapons[1]= 2;
                                weaponLastClicked = 2;
                            }
                        }
                        else{
                            weaponLastClicked = 2;
                        }
                    }//hit revolver

                    else if (y1< roundEnd.getUziUpH()+roundEnd.UziUpH()&&y1> roundEnd.getUziUpH()&&
                            x1> roundEnd.getUziUpW()&&x1< roundEnd.getUziUpW()+roundEnd.UziUpW()){
                        //System.out.println("hit uzi");
                        if(roundEnd.getUz()){
                            if(points>=roundEnd.getUzCost()) {
                                roundEnd.setUz(false);
                                points -= roundEnd.getUzCost();
                                roundEnd.setPoints(points);
                                weapons[2]= 3;
                                weaponLastClicked = 3;
                            }
                        }
                        else{
                            weaponLastClicked =3;
                        }

                    }//hit uzi

                    else if (y1< roundEnd.getShotgunUpH()+roundEnd.ShotgunUpH()&&y1> roundEnd.getShotgunUpH()&&
                            x1> roundEnd.getShotgunUpW()&&x1< roundEnd.getShotgunUpW()+roundEnd.ShotgunUpW()){
                        //System.out.println("hit shtg");
                        if(roundEnd.getShot()){
                            if(points>=roundEnd.getShotCost()) {
                                roundEnd.setShot(false);
                                points -= roundEnd.getShotCost();
                                roundEnd.setPoints(points);
                                weapons[3]= 4;
                                weaponLastClicked = 4;
                            }
                        }
                        else{
                            weaponLastClicked = 4;
                        }
                    }//hit shotgun

                    else if (y1< roundEnd.getAkUpH()+roundEnd.AkUpH()&&y1> roundEnd.getAkUpH()&&
                            x1> roundEnd.getAkUpW()&&x1< roundEnd.getAkUpW()+roundEnd.AkUpW()){
                        if(roundEnd.getAk()){
                            //System.out.println("hit ak");
                            if(points>=roundEnd.getAkCost()) {
                                roundEnd.setAk(false);
                                points -= roundEnd.getAkCost();
                                roundEnd.setPoints(points);
                                weapons[4]= 5;
                                weaponLastClicked = 5;
                            }
                        }
                        else{
                            weaponLastClicked = 5;
                        }
                    }//hit ak47

                    return true;

                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
            }
        }
        else if(atMenu) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = event.getPointerId(0);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    float x1 = event.getX(pointerIndex);
                    float y1 = event.getY(pointerIndex);
                    if (x1< mainMenu.getLogoutW()&&y1< mainMenu.getLogoutH()){

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("logout", "T");
                        editor.apply();
                        Intent intent = new Intent(getContext(), Login.class);
                        getContext().startActivity(intent);
                        //startActivity(new Intent(GameView.this, Login.class));
                        //finish();
//                        Intent myIntent = new Intent(this.activity, Login.class);
//                        //myIntent.putExtra("key", value); //Optional parameters
//                        this.activity.startActivity(myIntent);
                    }
                    else if (y1> mainMenu.getSurvivalH()&&y1< mainMenu.getArcadeH()-20){
                        //play survival
                        modeCheck = 1;
                        onebullet = true;
                        touchfired = 0;
                        touchTrack = 5;
                        kills = 0;
                        round = 1;
                        roundArc = (float)0.5;
                        roundMaxEnemies = 5;
                        roundSpawn = 0;
                        roundEnemyTrack = 0;
                        roundKills = 0;
                        points = 0;
                        weaponIndex = 0;
                        blowNuke = false;
                        instaKill = 0;
                        ammoUnlim = 0;
                        totalPoints = 0;
                        aktime = 0;
                        uzitime = 0;
                        revtime = 0;
                        shotguntime = 0;
                        kills = 0;
                        round = 1;
                        roundMaxEnemies = 5;
                        roundSpawn = 0;
                        roundEnemyTrack = 0;
                        roundKills = 0;
                        clip = 7;
                        maxhealth = 5;
                        maxClip = clip;
                        joystickPointerId = 20;
                        joystick2PointerId = 20;
                        roundEnd = new RoundOver(context, height, width);
                        joystick = new Joystick(context,0, height, 70);
                        player = new Player(context, joystick, width, height);
                        guns = new Guns(context, player, width/2, height/2);
                        player.setHealthPoint(maxhealth);
                        atMenu = false;
                        dead = false;
                        survivalMode = true;
                    }
                    else if (y1> mainMenu.getArcadeH()&&y1< mainMenu.getInstructionsH()-20){
                        //play arcade
                        modeCheck = 2;
                        onebullet = true;
                        touchfired = 0;
                        touchTrack = 5;
                        kills = 0;
                        round = 1;
                        roundArc = (float)0.5;
                        roundMaxEnemies = 5;
                        roundSpawn = 0;
                        roundEnemyTrack = 0;
                        roundKills = 0;
                        points = 0;
                        weaponIndex = 0;
                        blowNuke = false;
                        instaKill = 0;
                        ammoUnlim = 0;
                        totalPoints = 0;
                        aktime = 0;
                        uzitime = 0;
                        revtime = 0;
                        shotguntime = 0;
                        kills = 0;
                        round = 1;
                        roundMaxEnemies = 5;
                        roundSpawn = 0;
                        roundEnemyTrack = 0;
                        roundKills = 0;
                        points = 0;
                        clip = 7;
                        maxhealth = 5;
                        maxClip = clip;
                        joystickPointerId = 20;
                        joystick2PointerId = 20;
                        roundEnd = new RoundOver(context,height, width);
                        joystick2 = new Joystick(context,width-300, height, 70);
                        joystick = new Joystick(context,0, height, 70);
                        player.setHealthPoint(maxhealth);
                        player = new Player(context, joystick, joystick2, width, height);
                        guns = new Guns(context, player, width/2, height/2);
                        dead = false;
                        atMenu = false;
                        arcadeMode = true;
                    }
                    else if (y1> mainMenu.getInstructionsH()&&y1< mainMenu.getCreditsH()-20){
                        //instructions
                        atMenu = false;
                        instructions = true;
                    }
                    else if (y1> mainMenu.getCreditsH()&&y1< height-20){
                        //credits
                        credits = true;
                        shownRank=16;
                        rank=1;
                        atMenu = false;
                        getdb = true;
                        leaderboardupdate();
                    }

                    return true;

                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");
        try{
            gameLoop.startLoop();
        }catch (Exception e){

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceDestroyed()");

    }

    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        try{
            super.draw(canvas);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(survivalMode) {
            background.Draw(canvas);
            // Draw game objects

            try {
                for (Drops perk : perkList) {
                    perk.Draw(canvas);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                for (Enemy enemy : enemyList) {
                    enemy.Draw(canvas);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                for (Bullets bullet : spellList) {
                    bullet.Draw(canvas, guns);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            // Draw game panels

            try {
                guns.Draw(canvas);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                player.Draw(canvas, context);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                joystick.Draw(canvas);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                if (btnCheck > 0) {
                    fireBTN.Draw(canvas, 1);
                    btnCheck--;
                } else {
                    fireBTN.Draw(canvas, 0);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            // Draw Game over if the player is dead

            try {
                String kills = "Kills:" + roundKills;
                String rounds = "Level:" + round;
                String bullets;
                String strAmmoUnlim;


                if (instaKill > 0) {
                    strInstaKill = "Insta Kill" + instaKill / 60;
                } else {
                    strInstaKill = "";
                }

                if (ammoUnlim > 0) {
                    bullets = "Ammo:";
                    strAmmoUnlim = "max ammo " + ammoUnlim / 60;
                } else {
                    strAmmoUnlim = "";
                    bullets = "Ammo:" + clip;
                }

                String strPoints = "Points:" + points;
                Paint paint = new Paint();
                float textSize = 30*scaleSize;
                paint.setTextSize(textSize);

                canvas.drawText(kills, width/2-40, textSize, paint);
                //canvas.drawText(Integer.toString(height), 300, 70+textSize, paint);
                canvas.drawText(rounds, width - 200, textSize, paint);
                canvas.drawText(strInstaKill, 10, 100+textSize, paint);
                if (ammoUnlim > 0) {
                    canvas.drawText(strAmmoUnlim, 10, 60+textSize, paint);
                } else {
                    canvas.drawText(bullets, 10, 60+textSize, paint);
                }
                canvas.drawText(strPoints, width/2-40, 60+textSize, paint);
            }catch (Exception e){
                e.printStackTrace();
            }

            ammoUnlim--;
            instaKill--;
        }

        else if(credits){
            Credits.Draw(canvas);

            lead = Credits.getKillsH()+Credits.notKillsH()+30;
            Paint paint = new Paint();
            int color = ContextCompat.getColor(context, R.color.gameOver);

            paint.setColor(color);
            float textSize = 30;
            paint.setTextSize(textSize);
            canvas.drawText("Rank", 5,lead, paint);
            canvas.drawText("ID", 140,lead, paint);
            canvas.drawText("Level", 270,lead, paint);
            canvas.drawText("Kills", 400,lead, paint);
            canvas.drawText("Score", 580,lead, paint);
            canvas.drawText("============================================================================================================", -10,lead+20, paint);
            if(!userProfile.equals("guest")) {
                lead += 45;
                int index = 1;
                try {
                    if (!getdb) {
                        for (LeaderBoardInput score : scores) {
                            try {

                                if (index >= rank && index < shownRank) {
                                    String user = score.getUsername().toString();
                                    String mode = score.getMode().toString();
                                    String level = score.getLevel().toString();
                                    String kills = score.getKills().toString();
                                    String points = score.getPoints().toString();


                                    canvas.drawText(Integer.toString(rank), 5, lead, paint);
                                    canvas.drawText("| " + user, 90, lead, paint);
                                    canvas.drawText("| " + level, 240, lead, paint);
                                    canvas.drawText("| " + kills, 350, lead, paint);
                                    canvas.drawText("| " + points, 530, lead, paint);
                                    canvas.drawText("=============================================================================", -10, lead + 30, paint);
                                    lead += 55;
                                    rank++;

                                }
                                index++;
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                catch (Exception e) {
                    canvas.drawText("Database did not load correctly", 5,lead+50, paint);
                }

            }
            else{
                canvas.drawText("Not online", 5,lead+50, paint);
            }
                rank=shownRank-15;


            //
        }

        else if(instructions){
            Instructions.Draw(canvas);
        }

        else if(arcadeMode) {
            try {
                background.Draw(canvas);
            }catch(Exception e){
                System.out.println("back issue");
            }
            // Draw game objects

            try {
                for (Enemy enemy : enemyList) {
                    enemy.Draw(canvas);
                }

                for (Bullets bullet : spellList) {
                    bullet.Draw(canvas, 1);
                }

                for (Drops perk : perkList) {
                    perk.Draw(canvas);
                }
            }catch(Exception e){
                System.out.println("list issue");
            }
            // Draw game panels

            try {
                joystick.Draw(canvas);
                joystick2.Draw(canvas);
                player.Draw(canvas, context, 1);

            }catch(Exception e){
                System.out.println("joy or player issue");
        }
            if(aktime > 0) {
                weaponIndex = 4;
            }
            else if(uzitime > 0) {
                weaponIndex = 2;
            }
            else if(shotguntime > 0) {
                weaponIndex = 3;
            }
            else if(revtime > 0) {
                weaponIndex = 1;
            }
            else{
                weaponIndex = 0;
            }

            try {
                guns.setWeaponIndex(weaponIndex);
                guns.Draw(canvas, 1);
            }catch(Exception e){
                System.out.println("gun issue");
            }
            //performance.draw(canvas);


            String kills = "Kills:"+ roundKills;
            String rounds = "Level:"+ round;
            //String bullets = "joy1:  "+ joystickPointerId + "     joy2:  "+ joystick2PointerId;
            String strPoints = "Points:"+ points;

            String maxki = ""+roundMaxEnemies;
            Paint paint = new Paint();
            int color = ContextCompat.getColor(context, R.color.gameOver);

            float textSize = 30*scaleSize;
            paint.setTextSize(textSize);

            if(instaKill>0){
                strInstaKill = "Insta Kill"+ instaKill/60;
            }
            else{
                strInstaKill = "";
            }
            canvas.drawText(strInstaKill, 10, 90+textSize, paint);
            canvas.drawText(kills, width/2-100,30, paint);
            canvas.drawText(rounds, width-180,30, paint);
            //canvas.drawText(bullets, 10,200, paint);
            canvas.drawText(strPoints, width/2-100,60+textSize, paint);

            if(instaKill>=-1) {
                instaKill--;
            }
            if(aktime>=-1) {
                aktime--;
            }
            if(uzitime>=-1) {
                uzitime--;
            }
            if(shotguntime>=-1) {
                shotguntime--;
            }
            if(revtime>=-1) {
                revtime--;
            }
            // Draw Game over if the player is dead
        }

        else if (dead){
            gameOver.Draw(canvas);
        }

        else if(atMenu){
            mainMenu.Draw(canvas);
        }

        else if(roundOver){
            if(player.getHealthPoint()>0) {
                String strPoints = "$" + points;

                Paint paint = new Paint();
                int color = ContextCompat.getColor(context, R.color.white);

                paint.setColor(color);

                float textSize = 50;
                if(height>2000){
                    textSize = 100;
                }
                paint.setTextSize(textSize);

                canvas.drawText(strPoints, width / 2 - 100, textSize, paint);

                roundEnd.Draw(canvas, context, weaponLastClicked,height);
            }
            else{
                dead = true;
                roundOver = false;
            }
        }

    }

    public void leaderboardupdate(){
        try {
            if (!userProfile.equals("guest")) {
                if (Credits.getCheckModeType() == 1) {
                    if (Credits.getCheckSubType() == 1) {
                        reference1 = FirebaseDatabase.getInstance()
                                .getReference().child("scores").orderByChild("level");
                    } else if (Credits.getCheckSubType() == 2) {
                        reference1 = FirebaseDatabase.getInstance()
                                .getReference().child("scores").orderByChild("kills");
                    } else if (Credits.getCheckSubType() == 3) {
                        reference1 = FirebaseDatabase.getInstance()
                                .getReference().child("scores").orderByChild("points");
                    }
                } else if (Credits.getCheckModeType() == 2) {
                    if (Credits.getCheckSubType() == 1) {
                        reference1 = FirebaseDatabase.getInstance()
                                .getReference().child("Rscores").orderByChild("level");
                    } else if (Credits.getCheckSubType() == 2) {
                        reference1 = FirebaseDatabase.getInstance()
                                .getReference().child("Rscores").orderByChild("kills");
                    } else if (Credits.getCheckSubType() == 3) {
                        Query reference1 = FirebaseDatabase.getInstance()
                                .getReference().child("Rscores").orderByChild("points");
                    }
                }

                reference1.limitToFirst(1000).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (getdb) {
                            scores.clear();

                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                try {
                                    LeaderBoardInput leaderBoardInput = child.getValue(LeaderBoardInput.class);


                                    long round1 = Long.parseLong(leaderBoardInput.getLevel()) + 99999;
                                    long roundKills1 = Long.parseLong(leaderBoardInput.getKills()) + 999999;
                                    long points1 = Long.parseLong(leaderBoardInput.getPoints()) + 99999999;
                                    String oundkills = Long.toString(roundKills1);
                                    String oints = Long.toString(points1);
                                    String ound = Long.toString(round1);
                                    scores.add(new LeaderBoardInput(leaderBoardInput.getUsername(), leaderBoardInput.getMode(), oundkills, oints, ound));
                                    getdb = false;
                                } catch (Exception e) {

                                }
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }catch(Exception e){

        }
    }

    public void update() {
        if(survivalMode) {
            // Stop updating the game if the player is dead

                if (player.getHealthPoint() <= 0) {
                    weaponLastClicked = 1;
                    survivalMode = false;
                    joyY = 0;
                    joyX = 0;
                    player.reset();
                    spellList.clear();
                    enemyList.clear();
                    perkList.clear();
                    weapons[0] = 1;
                    weapons[1] = 0;
                    weapons[2] = 0;
                    weapons[3] = 0;
                    weapons[4] = 0;
                    weaponIndex = 0;
                    roundEnd.setLastClicked(1);
                    player = new Player(context, joystick, width, height);
                    guns = new Guns(context, player, width / 2, height / 2);

                    if (roundKills > 0 &&!userProfile.equals("guest")) {
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference().child("scores");

                        round = round - 99999;
                        roundKills = roundKills - 999999;
                        points = totalPoints - 99999999;
                        String oundkills = Integer.toString(roundKills);
                        String oints = Long.toString(points);
                        String ound = Integer.toString(round);


                        LeaderBoardInput leaderBoardInput = new LeaderBoardInput(userProfile, "SM", oundkills, oints, ound);
                        reference.child(userProfile + System.currentTimeMillis()).setValue(leaderBoardInput);
                    }

                    touchTrack = 0;
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    gameLoop.pauseLoop();
                    passToMainActivity();
                    //gameLoop.runAd(getContext());
                    return;
                }

                else {
                    if (blowNuke) {
                        kills += enemyList.size();
                        roundEnemyTrack += enemyList.size();
                        points += 400;
                        totalPoints += 400;
                        enemyList.clear();
                        blowNuke = false;
                    }
                    if (roundMaxEnemies > roundSpawn) {
                        if (Enemy.readyToSpawn(round)) {
                            enemyList.add(new Enemy(getContext(), player, round));
                            roundSpawn++;
                        }

                    }
                    if (fired) {
                        if (clip > 0 || ammoUnlim > 0) {

                            if (joyX == 0 && joyY == 0) {
                                joyX = 2;
                                if(weaponIndex == 2 || weaponIndex == 4){
                                    if(delayTime%12==0 && weaponIndex == 2) {
                                        if (ammoUnlim <= 0) {
                                            clip--;
                                        }
                                        spellList.add(new Bullets(getContext(), player, joyX, joyY, guns));
                                        delayTime = 1;
                                        sound();
                                    }
                                    else if(delayTime%10==0 && weaponIndex == 4) {
                                        if (ammoUnlim <= 0) {
                                            clip--;
                                        }
                                        spellList.add(new Bullets(getContext(), player, joyX, joyY, guns));
                                        delayTime = 1;
                                        sound();
                                    }
                                    else{
                                        delayTime++;
                                    }
                                }
                                else {
                                    if (ammoUnlim <= 0) {
                                        clip--;
                                    }
                                    sound();
                                    spellList.add(new Bullets(getContext(), player, joyX, joyY, guns));
                                }

                            }
                            else {
                                joyX *= 5;
                                joyY *= 5;

                                if(weaponIndex == 2 || weaponIndex == 4){
                                    if(delayTime%20==0) {
                                        if (ammoUnlim <= 0) {
                                            clip--;
                                        }
                                        spellList.add(new Bullets(getContext(), player, joyX, joyY, guns));
                                        delayTime = 1;
                                        sound();
                                    }
                                    else{
                                        delayTime++;
                                    }
                                }
                                else {
                                    if (ammoUnlim <= 0) {
                                        clip--;
                                    }
                                    sound();
                                    spellList.add(new Bullets(getContext(), player, joyX, joyY, guns));
                                }
                                joyX /= 5;
                                joyY /= 5;
                            }



                            if (weaponIndex < 2 || weaponIndex == 3) {
                                fired = false;
                            }
                        }
                    }
                    if (roundEnemyTrack >= roundMaxEnemies && !dead) {
                        survivalMode = false;
                        roundOver = true;
                        spellList.clear();
                        roundEnemyTrack = 0;
                        roundSpawn = 0;
                        round++;
                        roundMaxEnemies = 5;
                        roundMaxEnemies = roundMaxEnemies * round;
                        roundEnd.setPoints(points);
                        perkList.clear();
                        ammoUnlim = 0;
                        instaKill = 0;
                        blowNuke = false;

                        touchTrack = 0;
                        joystick.setIsPressed(false);
                        joystick.resetActuator();

                    }
                }

            // Update game state
            try {
                joystick.update();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                player.update();
            }catch (Exception e){e.printStackTrace();}
            try {
                guns.update(player);
            }catch (Exception e){e.printStackTrace();}

            // Update states of all enemies
            try {
                for (Enemy enemy : enemyList) {
                    enemy.update();
                }
            }catch (Exception e){e.printStackTrace();}

            try {
                for (Bullets bullet : spellList) {
                    bullet.update();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Iterator<Drops> iteratorPerk = perkList.iterator();
            try {
                while (iteratorPerk.hasNext()) {
                    Drops perk = iteratorPerk.next();
                    if (perk.getX() <= player.getPositionX() + 50 && perk.getY() <= player.getPositionY() + 50 &&
                            perk.getX() + 60 >= player.getPositionX() && perk.getY() + 80 >= player.getPositionY()) {
                        if (perk.getPerkT() == 1) {
                            instaKill = 480;
                        } else if (perk.getPerkT() == 3) {
                            ammoUnlim = 480;
                        } else {
                            blowNuke = true;
                        }
                        iteratorPerk.remove();
                    }
                    if (perk.getTime() <= 0) {
                        iteratorPerk.remove();
                    }

                }
            }catch (Exception e){e.printStackTrace();}

            // Update states of all



            Iterator<Enemy> iteratorEnemy = enemyList.iterator();
            try {
                while (iteratorEnemy.hasNext()) {
                    Enemy enemy = iteratorEnemy.next();

                    if (enemy.getPositionX() + 45 > player.getPositionX() && enemy.getPositionX() < player.getPositionX() + 45) {
                        if (enemy.getPositionY() + 45 > player.getPositionY() && enemy.getPositionY() < player.getPositionY() + 45) {
                            // Remove enemy if it collides with the player
                            iteratorEnemy.remove();
                            roundEnemyTrack++;
                            player.setHealthPoint(player.getHealthPoint() - 1);
                            continue;
                        }
                    }

                    Iterator<Bullets> iteratorSpell = spellList.iterator();
                    while (iteratorSpell.hasNext()) {
                        Bullets spell = iteratorSpell.next();
                        // Remove enemy if it collides with a spell
                        if (spell.getPositionX() + 7 > enemy.getPositionX() && spell.getPositionX() < enemy.getPositionX() + 100) {
                            if (spell.getPositionY() + 3 > enemy.getPositionY() && spell.getPositionY() < enemy.getPositionY() + 100) {
                                iteratorSpell.remove();
                                if (instaKill > 0) {
                                    enemy.setHealth(0);
                                } else {
                                    if (weaponIndex == 0) {
                                        enemy.setHealth(enemy.getHealth() - guns.getPistolDamage());
                                    } else if (weaponIndex == 1) {
                                        enemy.setHealth(enemy.getHealth() - guns.getRevDamage());
                                    } else if (weaponIndex == 2) {
                                        enemy.setHealth(enemy.getHealth() - guns.getUziDamage());
                                    } else if (weaponIndex == 3) {
                                        enemy.setHealth(enemy.getHealth() - guns.getShotDamage());
                                    } else if (weaponIndex == 4) {
                                        enemy.setHealth(enemy.getHealth() - guns.getAkDamage());
                                    }
                                }


                                if (enemy.getHealth() <= 0) {
                                    if (Math.random() > .9) {
                                        perkList.add(new Drops(getContext(), enemy));
                                    }


                                    iteratorEnemy.remove();
                                    roundKills++;
                                    roundEnemyTrack++;
                                    points += 75;
                                    totalPoints += 75;
                                    roundEnd.setPoints(points);
                                } else {
                                    points += 10;
                                    totalPoints += 10;
                                    roundEnd.setPoints(points);
                                }

                                break;
                            }
                        }
                        try {
                            if (spell.getPositionX() < 0 - 100 || spell.getPositionX() > width + 100 || spell.getPositionY() < 0 - 100 || spell.getPositionY() > height + 100) {
                                iteratorSpell.remove();
                            }
                        }catch (Exception e){ }
                    }
                }
            }catch (Exception e){

            }

//            Iterator<Bullets> iteratorSpell = spellList.iterator();
//            try {
//                while (iteratorSpell.hasNext()) {
//                    Bullets spell = iteratorSpell.next();
//                    // Remove enemy if it collides with a spell
//                    if (spell.getPositionX() < 0 - 100 || spell.getPositionX() > width + 100 || spell.getPositionY() < 0 - 100 || spell.getPositionY() > height + 100) {
//                        iteratorSpell.remove();
//                    }
//                }
//            }catch (Exception e){
//
//            }
        }
        else if(arcadeMode) {
            // Stop updating the game if the player is dead
            if (player.getHealthPoint() <= 0) {
                arcadeMode = false;
                dead = true;
                joyY = 0;
                joyX = 0;
                if(roundKills>0 &&!userProfile.equals("guest")) {
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("Rscores");


                    round = round - 99999;
                    roundKills = roundKills - 999999;
                    points = totalPoints - 99999999;
                    String oundkills = Integer.toString(roundKills);
                    String oints = Long.toString(points);
                    String ound = Integer.toString(round);


                    LeaderBoardInput leaderBoardInput = new LeaderBoardInput(userProfile, "RM", oundkills, oints, ound);
                    reference.child(userProfile + System.currentTimeMillis()).setValue(leaderBoardInput);
                }
                player.reset();
                spellList.clear();
                enemyList.clear();
                perkList.clear();
                player = new Player(context, joystick, joystick2, width, height);
                guns = new Guns(context, player, width/2, height/2);
                passToMainActivity();
                return;
            }
            else if (roundEnemyTrack >= roundMaxEnemies && !dead){
                roundEnemyTrack = 0;
                roundSpawn = 0;
                round++;
                roundMaxEnemies = 5;
                roundMaxEnemies= roundMaxEnemies*round;
                spellList.clear();
            }
            else {

                if (blowNuke) {
                    roundEnemyTrack += enemyList.size();
                    points += 400;
                    totalPoints += 400;
                    kills += enemyList.size();
                    enemyList.clear();
                    blowNuke = false;
                }
                // Update game state
                joystick.update();
                joystick2.update();
                try {
                    player.update(1);
                } catch (Exception e) {

                }
                guns.update(player, 1);

                if (roundMaxEnemies > roundSpawn) {
                    if (Enemy.readyToSpawn(roundArc, 1)) {
                        roundArc += .2;

                        enemyList.add(new Enemy(getContext(), player, roundArc));
                        roundSpawn++;

                    }
                }

                // Update states of all enemies
                try {
                    for (Enemy enemy : enemyList) {
                        enemy.update();
                    }
                }catch (Exception e){

                }

                if (joystick2.getIsPressed()) {
                    if (firedTrack >= 10) {
                        firedTrack = 0;
                        if (joyX == 0 && joyY == 0) {
                            joyX = 2;
                            spellList.add(new Bullets(getContext(), player, joyX, joyY, joystick2, guns));
                        } else {
                            joyX *= 5;
                            joyY *= 5;
                            spellList.add(new Bullets(getContext(), player, joyX, joyY, joystick2, guns));

                            joyX /= 5;
                            joyY /= 5;
                        }
                        fired = false;
                        try {
                            if (soundCount == 1) {
                                firesounds1.Destroy();
                                firesounds1 = new firesounds(getContext());
                                firesounds1.isFired();
                                soundCount++;
                            } else if (soundCount == 2) {
                                firesounds2.Destroy();
                                firesounds2 = new firesounds(getContext());
                                firesounds2.isFired();
                                soundCount++;
                            } else if (soundCount == 3) {
                                firesounds3.Destroy();
                                firesounds3 = new firesounds(getContext());
                                firesounds3.isFired();
                                soundCount = 1;
                            }
                        }
                        catch (Exception e){}

                    } else {
                        firedTrack++;
                        fired = false;
                    }
                }

                try {
                    for (Bullets bullet : spellList) {
                        bullet.update();
                    }
                }catch (Exception e){

                }

                Iterator<Drops> iteratorPerk = perkList.iterator();
                try {
                    while (iteratorPerk.hasNext()) {
                        Drops perk = iteratorPerk.next();
                        if (perk.getX() <= player.getPositionX() + 50 && perk.getY() <= player.getPositionY() + 50 &&
                                perk.getX() + 60 >= player.getPositionX() && perk.getY() + 80 >= player.getPositionY()) {

                            if (perk.getPerkT() == 1) {
                                instaKill = 480;
                            } else if (perk.getPerkT() == 2) {
                                blowNuke = true;
                            } else if (perk.getPerkT() == 3) {
                                aktime = 480;
                                uzitime = 0;
                                revtime = 0;
                                shotguntime = 0;
                            } else if (perk.getPerkT() == 4) {
                                uzitime = 480;
                                aktime = 0;
                                revtime = 0;
                                shotguntime = 0;
                            } else if (perk.getPerkT() == 5) {
                                revtime = 480;
                                aktime = 0;
                                uzitime = 0;
                                shotguntime = 0;
                            } else if (perk.getPerkT() == 6) {
                                shotguntime = 480;
                                aktime = 0;
                                uzitime = 0;
                                revtime = 0;
                            } else if (perk.getPerkT() == 7) {
                                player.setHealthPoint(maxhealth);
                            } else if (perk.getPerkT() == 8) {
                                maxhealth++;
                                player.setHealthPoint(player.getHealthPoint() + 1);
                            } else if (perk.getPerkT() == 9) {
                                guns.setPistolDamage((float) (guns.getPistolDamage() + 1.5));
                            }

                            iteratorPerk.remove();
                        }
                        if (perk.getTime() <= 0) {
                            iteratorPerk.remove();
                        }

                    }
                }catch (Exception e){

                }

                // Iterate through enemyList and Check for collision between each enemy and the player and
                // spells in spellList.
                Iterator<Enemy> iteratorEnemy = enemyList.iterator();

                try {
                    while (iteratorEnemy.hasNext()) {
                        Enemy enemy = iteratorEnemy.next();
                        if (enemy.getPositionX() + 30 > player.getPositionX() && enemy.getPositionX() < player.getPositionX() + 30) {
                            if (enemy.getPositionY() + 30 > player.getPositionY() && enemy.getPositionY() < player.getPositionY() + 30) {
                                // Remove enemy if it collides with the player
                                iteratorEnemy.remove();
                                roundEnemyTrack++;
                                player.setHealthPoint(player.getHealthPoint() - 1);
                            }
                        }

                        Iterator<Bullets> iteratorSpell = spellList.iterator();
                        while (iteratorSpell.hasNext()) {
                            Bullets spell = iteratorSpell.next();
                            // Remove enemy if it collides with a spell
                            if (spell.getPositionX() + 7 > enemy.getPositionX() && spell.getPositionX() < enemy.getPositionX() + 100) {
                                if (spell.getPositionY() + 3 > enemy.getPositionY() && spell.getPositionY() < enemy.getPositionY() + 100) {
                                    iteratorSpell.remove();

                                    if (instaKill > 0) {
                                        enemy.setHealth(0);
                                    } else if (weaponIndex == 0) {
                                        enemy.setHealth(enemy.getHealth() - guns.getPistolDamage());
                                    } else if (weaponIndex == 1) {
                                        enemy.setHealth(enemy.getHealth() - (float) (guns.getPistolDamage() * 1.2));
                                    } else if (weaponIndex == 2) {
                                        enemy.setHealth(enemy.getHealth() - (float) (guns.getPistolDamage() * 1.4));
                                    } else if (weaponIndex == 3) {
                                        enemy.setHealth(enemy.getHealth() - (float) (guns.getPistolDamage() * 2));
                                    } else if (weaponIndex == 4) {
                                        enemy.setHealth(enemy.getHealth() - (float) (guns.getPistolDamage() * 1.7));
                                    }

                                    if (enemy.getHealth() <= 0) {
                                        if (Math.random() > .9) {
                                            perkList.add(new Drops(getContext(), enemy, 1));
                                        }


                                        iteratorEnemy.remove();
                                        roundKills++;
                                        roundEnemyTrack++;
                                        points += 75;
                                        totalPoints += 75;
                                        roundEnd.setPoints(points);
                                    } else {
                                        points += 10;
                                        totalPoints += 10;
                                        roundEnd.setPoints(points);
                                    }

                                    break;
                                }
                            }
                        }
                    }
                }catch (Exception e){

                }

                Iterator<Bullets> iteratorSpell = spellList.iterator();
                try {
                    while (iteratorSpell.hasNext()) {
                        Bullets spell = iteratorSpell.next();
                        // Remove enemy if it collides with a spell
                        if (spell.getPositionX() < 0 - 100 || spell.getPositionX() > width + 100 || spell.getPositionY() < 0 - 100 || spell.getPositionY() > height + 100) {
                            iteratorSpell.remove();
                        }
                    }
                }catch (Exception e){

                }
            }
        }
    }

    public void sound(){
        try {
            if (soundCount == 1) {
                firesounds1.Destroy();
                firesounds1 = new firesounds(getContext());
                firesounds1.isFired();
                soundCount++;
            } else if (soundCount == 2) {
                firesounds2.Destroy();
                firesounds2 = new firesounds(getContext());
                firesounds2.isFired();
                soundCount++;
            } else if (soundCount == 3) {
                firesounds3.Destroy();
                firesounds3 = new firesounds(getContext());
                firesounds3.isFired();
                soundCount = 1;
            }
        }catch (Exception e){

        }
    }

    public void pause(int x){

        gameLoop.pauseLoop();
        //gameLoop.
    }
    public void resume(int x) {
        try {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
            //gameLoop.startLoop();

        }catch(Exception e){

        }
    }
    public void pause() {
        try {
            SharedPreferences.Editor outState = sharedPreferences.edit();
            firesounds1.Destroy();
            firesounds2.Destroy();
            firesounds3.Destroy();
            roundSpawn -= enemyList.size();
            spellList.clear();
            enemyList.clear();
            perkList.clear();
            scores.clear();
            outState.putInt("touchfired", touchfired);
            outState.putInt("touchTrack", touchTrack);
            outState.putInt("kills", kills);
            outState.putInt("round", round);
            outState.putInt("roundMaxEnemies", roundMaxEnemies);
            outState.putInt("roundSpawn", roundSpawn);
            outState.putInt("roundEnemyTrack", roundEnemyTrack);
            outState.putInt("roundKills", roundKills);
            outState.putLong("points", points);
            outState.putInt("clip", clip);
            outState.putInt("maxhealth", maxhealth);
            outState.putInt("weaponLastClicked", weaponLastClicked);
            outState.putInt("weaponIndex", weaponIndex);
            outState.putInt("instaKill", instaKill);
            outState.putInt("ammoUnlim", ammoUnlim);
            outState.putLong("totalPoints", totalPoints);
            outState.putInt("shownRank", shownRank);
            outState.putInt("rank", rank);
            outState.putInt("aktime", aktime);
            outState.putInt("uzitime", uzitime);
            outState.putInt("revtime", revtime);
            outState.putInt("shotguntime", shotguntime);
            outState.putInt("joystickPointerId", joystickPointerId);
            outState.putInt("joystick2PointerId", joystick2PointerId);
            outState.putInt("btnCheck", btnCheck);
            outState.putInt("modeCheck", modeCheck);
            outState.putInt("width", width);
            outState.putInt("height", height);
            outState.putInt("mActivePointerId", mActivePointerId);
            outState.putInt("pointerIndex", pointerIndex);
            outState.putInt("firedTrack", firedTrack);
            outState.putInt("bulletPointerId", bulletPointerId);
            outState.putInt("lead", lead);
            outState.putInt("delayTime", delayTime);

            outState.putFloat("joyX", joyX);
            outState.putFloat("joyY", joyY);
            outState.putFloat("roundArc", roundArc);///check type

            outState.putString("user", userProfile);
            outState.putString("strInstaKill", strInstaKill);

            outState.putBoolean("onebullet", onebullet);
            outState.putBoolean("blowNuke", blowNuke);
            outState.putBoolean("getdb", getdb);
            outState.putBoolean("fired", fired);
            outState.putBoolean("onebullet", onebullet);
            outState.putBoolean("survivalMode", survivalMode);
            outState.putBoolean("arcadeMode", arcadeMode);
            outState.putBoolean("roundOver", roundOver);
            player.saveState();
            guns.saveState();
            roundEnd.saveState();
            outState.apply();
            //gameLoop.saveState(outState);
            //maxClip = clip;
            //gameOver.saveState(outState);
            //Credits.saveState(outState);
            //Instructions.saveState(outState);
            //performance.saveState(outState);
            //mainMenu.saveState(outState);

        }catch(Exception e){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resume() {
        try {
            SharedPreferences savedInstanceState = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);

            delayTime = 20;
            firesounds1 = new firesounds(getContext());
            firesounds2 = new firesounds(getContext());
            firesounds3 = new firesounds(getContext());
            soundCount = 1;
            getdb = false;
            atMenu = true;
            credits = false;
            instructions = false;
            survivalMode = false;
            roundOver = false;
            dead = false;
            this.context = context;
            joystick2PointerId = 20;
            joystickPointerId = 20;
            displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            this.height = displayMetrics.heightPixels;
            this.width = displayMetrics.widthPixels;
            roundEnd = new RoundOver(context, height, width);
            Credits = new Credits(context, width,height);
            mainMenu = new Main_menu(context, width,height);
            Instructions = new Instruction(context, width,height);
            performance = new Performance(context, gameLoop);
            gameOver = new GameOver(context, width, height);
            joystick = new Joystick(context,0, height, 70);
            joystick2 = new Joystick(context,width-300, height, 70);
            background = new Background(context);
            //player = new Player(context, joystick, width, height);
            //player = new Player(context, joystick, joystick2, width, height);
            // Initialize game objects
            guns = new Guns(context, player, width/2, height/2);
            fireBTN = new FireBTN(context,width,height);
            //initialize player's round information to track
            onebullet = true;
            touchfired = 0;
            touchTrack = 5;
            kills = 0;
            round = 1;
            roundArc = (float)0.5;
            roundMaxEnemies = 5;
            roundSpawn = 0;
            roundEnemyTrack = 0;
            roundKills = 0;
            points = 0;
            clip = 7;
            maxhealth = 5;
            maxClip = clip;
            weaponLastClicked = 1;
            weaponIndex = 0;
            blowNuke = false;
            instaKill = 0;
            ammoUnlim = 0;
            setFocusable(true);
            totalPoints = 0;
            shownRank=16;
            rank=1;
            aktime = 0;
            uzitime = 0;
            revtime = 0;
            shotguntime = 0;




            enemyList = new ArrayList<Enemy>();
            spellList = new ArrayList<Bullets>();
            perkList = new ArrayList<Drops>();
            int[] weapons = {1,0,0,0,0};//check to see if weapons have been bought
            scores = new ArrayList<LeaderBoardInput>();

            player.restoreState(width, height);
            //fireBTN.restoreState();
            guns.restoreState((int)width/2,(int)height/2);

            touchfired = savedInstanceState.getInt("touchfired",0);
            touchTrack = savedInstanceState.getInt("touchTrack",5);
            kills = savedInstanceState.getInt("kills",0);
            round = savedInstanceState.getInt("round",1);
            roundMaxEnemies = savedInstanceState.getInt("roundMaxEnemies",5);
            roundSpawn = savedInstanceState.getInt("roundSpawn",0);
            roundEnemyTrack = savedInstanceState.getInt("roundEnemyTrack",0);
            roundKills = savedInstanceState.getInt("roundKills",0);
            points = savedInstanceState.getLong("points",0);
            clip = savedInstanceState.getInt("clip",7);
            maxhealth = savedInstanceState.getInt("maxhealth",5);
            weaponLastClicked = savedInstanceState.getInt("weaponLastClicked",1);
            weaponIndex = savedInstanceState.getInt("weaponIndex",1);
            instaKill = savedInstanceState.getInt("instaKill",0);
            ammoUnlim = savedInstanceState.getInt("ammoUnlim",0);
            totalPoints = savedInstanceState.getLong("totalPoints",0);
            shownRank = savedInstanceState.getInt("shownRank",16);
            rank = savedInstanceState.getInt("rank",1);
            aktime = savedInstanceState.getInt("aktime",0);
            uzitime = savedInstanceState.getInt("uzitime",0);
            revtime = savedInstanceState.getInt("revtime",0);
            shotguntime = savedInstanceState.getInt("shotguntime",0);
            joystickPointerId = savedInstanceState.getInt("joystickPointerId",20);
            joystick2PointerId = savedInstanceState.getInt("joystick2PointerId",20);
            btnCheck = savedInstanceState.getInt("btnCheck", 0);
            mActivePointerId = savedInstanceState.getInt("mActivePointerId",0);
            pointerIndex = savedInstanceState.getInt("pointerIndex",0);
            firedTrack = savedInstanceState.getInt("firedTrack",0);
            bulletPointerId = savedInstanceState.getInt("bulletPointerId",0);
            lead = savedInstanceState.getInt("lead", Credits.getKillsH()+Credits.notKillsH()+30);
            delayTime = savedInstanceState.getInt("delayTime",20);

            joyX = savedInstanceState.getFloat("joyX",0);
            joyY = savedInstanceState.getFloat("joyY",0);
            roundArc = savedInstanceState.getFloat("roundArc",(float)0.5);///check type

            userProfile = savedInstanceState.getString("user",sharedPreferences.getString("user","error"));
            strInstaKill = savedInstanceState.getString("strInstaKill","");

            onebullet = savedInstanceState.getBoolean("onebullet", true);
            blowNuke = savedInstanceState.getBoolean("blowNuke",false);
            getdb = savedInstanceState.getBoolean("getdb",false);
            fired = savedInstanceState.getBoolean("fired",false);
            survivalMode = savedInstanceState.getBoolean("survivalMode", false);
            arcadeMode = savedInstanceState.getBoolean("arcadeMode",false);
            roundOver = savedInstanceState.getBoolean("roundOver",false);

            maxClip = clip;
            player.restoreState(width,height);
            guns.restoreState(width,height);
            roundEnd.restoreState();
            //mainMenu.restoreState(savedInstanceState);
            //gameOver.restoreState(savedInstanceState);
            //Instructions.restoreState(savedInstanceState);
            if(!survivalMode&& !arcadeMode && !roundOver){
                atMenu = true;
            }
            else{
                if(survivalMode) {
                    modeCheck = savedInstanceState.getInt("modeCheck",1);
                }
                else if(arcadeMode){
                    modeCheck = savedInstanceState.getInt("modeCheck",2);
                }
            }


            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
            //gameLoop.startLoop();

        }catch(Exception e){

        }
    }

    public void saveState(final Bundle outState) {
        getHolder().removeCallback(this);
            final AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>(){
                protected Void doInBackground(Void... voids) {
                    try {

                        gameLoop.pauseLoop();
                        gameLoop = null;
                        roundSpawn -= enemyList.size();
                        spellList.clear();
                        enemyList.clear();
                        perkList.clear();
                        scores.clear();
                        outState.putInt("touchfired", touchfired);
                        outState.putInt("touchTrack", touchTrack);
                        outState.putInt("kills", kills);
                        outState.putInt("round", round);
                        outState.putInt("roundMaxEnemies", roundMaxEnemies);
                        outState.putInt("roundSpawn", roundSpawn);
                        outState.putInt("roundEnemyTrack", roundEnemyTrack);
                        outState.putInt("roundKills", roundKills);
                        outState.putLong("points", points);
                        outState.putInt("clip", clip);
                        outState.putInt("maxhealth", maxhealth);
                        outState.putInt("weaponLastClicked", weaponLastClicked);
                        outState.putInt("weaponIndex", weaponIndex);
                        outState.putInt("instaKill", instaKill);
                        outState.putInt("ammoUnlim", ammoUnlim);
                        outState.putLong("totalPoints", totalPoints);
                        outState.putInt("shownRank", shownRank);
                        outState.putInt("rank", rank);
                        outState.putInt("aktime", aktime);
                        outState.putInt("uzitime", uzitime);
                        outState.putInt("revtime", revtime);
                        outState.putInt("shotguntime", shotguntime);
                        outState.putInt("joystickPointerId", joystickPointerId);
                        outState.putInt("joystick2PointerId", joystick2PointerId);
                        outState.putInt("btnCheck", btnCheck);
                        outState.putInt("modeCheck", modeCheck);
                        outState.putInt("width", width);
                        outState.putInt("height", height);
                        outState.putInt("mActivePointerId", mActivePointerId);
                        outState.putInt("pointerIndex", pointerIndex);
                        outState.putInt("firedTrack", firedTrack);
                        outState.putInt("bulletPointerId", bulletPointerId);
                        outState.putInt("lead", lead);
                        outState.putInt("delayTime", delayTime);

                        outState.putFloat("joyX", joyX);
                        outState.putFloat("joyY", joyY);
                        outState.putFloat("roundArc", roundArc);///check type

                        outState.putString("user", userProfile);
                        outState.putString("strInstaKill", strInstaKill);

                        outState.putBoolean("onebullet", onebullet);
                        outState.putBoolean("blowNuke", blowNuke);
                        outState.putBoolean("getdb", getdb);
                        outState.putBoolean("fired", fired);
                        outState.putBoolean("onebullet", onebullet);
                        outState.putBoolean("survivalMode", survivalMode);
                        outState.putBoolean("arcadeMode", arcadeMode);
                        outState.putBoolean("roundOver", roundOver);
                        //gameLoop.saveState(outState);
                        //maxClip = clip;
                        player.saveState(outState);
                        guns.saveState(outState);
                        roundEnd.saveState(outState);
                        firesounds1.Destroy();
                        firesounds2.Destroy();
                        firesounds3.Destroy();
                        //gameOver.saveState(outState);
                        //Credits.saveState(outState);
                        //Instructions.saveState(outState);
                        //performance.saveState(outState);
                        //mainMenu.saveState(outState);
                    } catch (Exception e) {

                    }
                    return null;
                }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                    }
                };
            task.execute();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void restoreState(final Bundle savedInstanceState) {
        try {

            //AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>(){
                //protected Void doInBackground(Void... voids) {
                    try {


            //gameLoop.restoreState(savedInstanceState, surfaceHolder);
            firesounds1 = new firesounds(getContext());
            firesounds2 = new firesounds(getContext());
            firesounds3 = new firesounds(getContext());
            soundCount = 1;
            //roundEnd = new RoundOver(context);
            Credits = new Credits(context, width,height);
            mainMenu = new Main_menu(context, width,height);
            Instructions = new Instruction(context, width,height);
            //performance = new Performance(context, gameLoop);
            gameOver = new GameOver(context, width, height);
            joystick = new Joystick(context,0, height, 70);
            joystick2 = new Joystick(context,width-300, height, 70);
            background = new Background(context);
            //Instructions.saveState(outState);
            enemyList = new ArrayList<Enemy>();
            spellList = new ArrayList<Bullets>();
            perkList = new ArrayList<Drops>();
            int[] weapons = {1,0,0,0,0};//check to see if weapons have been bought
            scores = new ArrayList<LeaderBoardInput>();
            sharedPreferences = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
            userProfile = sharedPreferences.getString("user","guest");
            setFocusable(true);
            player.restoreState(savedInstanceState);
            //fireBTN.restoreState(savedInstanceState);
            guns.restoreState(savedInstanceState);

            touchfired = savedInstanceState.getInt("touchfired");
            touchTrack = savedInstanceState.getInt("touchTrack");
            kills = savedInstanceState.getInt("kills");
            round = savedInstanceState.getInt("round");
            roundMaxEnemies = savedInstanceState.getInt("roundMaxEnemies");
            roundSpawn = savedInstanceState.getInt("roundSpawn");
            roundEnemyTrack = savedInstanceState.getInt("roundEnemyTrack");
            roundKills = savedInstanceState.getInt("roundKills");
            points = savedInstanceState.getLong("points");
            clip = savedInstanceState.getInt("clip");
            maxhealth = savedInstanceState.getInt("maxhealth");
            weaponLastClicked = savedInstanceState.getInt("weaponLastClicked");
            weaponIndex = savedInstanceState.getInt("weaponIndex");
            instaKill = savedInstanceState.getInt("instaKill");
            ammoUnlim = savedInstanceState.getInt("ammoUnlim");
            totalPoints = savedInstanceState.getLong("totalPoints");
            shownRank = savedInstanceState.getInt("shownRank");
            rank = savedInstanceState.getInt("rank");
            aktime = savedInstanceState.getInt("aktime");
            uzitime = savedInstanceState.getInt("uzitime");
            revtime = savedInstanceState.getInt("revtime");
            shotguntime = savedInstanceState.getInt("shotguntime");
            joystickPointerId = savedInstanceState.getInt("joystickPointerId");
            joystick2PointerId = savedInstanceState.getInt("joystick2PointerId");
            btnCheck = savedInstanceState.getInt("btnCheck");
            modeCheck = savedInstanceState.getInt("modeCheck");
            width = savedInstanceState.getInt("width");
            height = savedInstanceState.getInt("height");
            mActivePointerId = savedInstanceState.getInt("mActivePointerId");
            pointerIndex = savedInstanceState.getInt("pointerIndex");
            firedTrack = savedInstanceState.getInt("firedTrack");
            bulletPointerId = savedInstanceState.getInt("bulletPointerId");
            lead = savedInstanceState.getInt("lead");
            delayTime = savedInstanceState.getInt("delayTime");

            joyX = savedInstanceState.getFloat("joyX");
            joyY = savedInstanceState.getFloat("joyY");
            roundArc = savedInstanceState.getFloat("roundArc");///check type

            userProfile = savedInstanceState.getString("user");
            strInstaKill = savedInstanceState.getString("strInstaKill");

            onebullet = savedInstanceState.getBoolean("onebullet");
            blowNuke = savedInstanceState.getBoolean("blowNuke");
            getdb = savedInstanceState.getBoolean("getdb");
            fired = savedInstanceState.getBoolean("fired");
            onebullet = savedInstanceState.getBoolean("onebullet");
            survivalMode = savedInstanceState.getBoolean("survivalMode");
            arcadeMode = savedInstanceState.getBoolean("arcadeMode");
            roundOver = savedInstanceState.getBoolean("roundOver");
            maxClip = clip;
            roundEnd.restoreState(savedInstanceState);

            if(!survivalMode&& !arcadeMode && !roundOver){
                atMenu = true;
            }

                    }catch(Exception e){

                    }

                    //return null;
                //}

//                @Override
//                protected void onPostExecute(Void aVoid) {
//                    super.onPostExecute(aVoid);
//
//                }
//            };
//            task.execute();
//            SurfaceHolder surfaceHolder = getHolder();
//            surfaceHolder.addCallback(this);
//            gameLoop = new GameLoop(this, surfaceHolder);
            //gameLoop.startLoop();
        }catch (Exception e){

System.out.println(e);
        }
    }

    private void passToMainActivity()  {
        //gameLoop.pauseLoop();
        gameLoop.pauseLoop();

        //gameLoop.runAd(getContext());
        //dead = true;
        ((MainActivity) context).callMe(true, totalPoints, round, kills, modeCheck);
    }
}