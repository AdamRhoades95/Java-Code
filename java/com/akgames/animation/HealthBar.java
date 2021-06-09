package com.akgames.animation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import static android.content.Context.MODE_PRIVATE;

public class HealthBar extends View {
    private Player player;
    private Paint borderPaint, healthPaint;
    private int width, height, margin; // pixel value
    private float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
    private float distanceToPlayer = 30;
    private float healthPointPercentage;
    private int healthColor;
    private int borderColor;
    private float borderLeft, borderTop, borderRight, borderBottom;

    public HealthBar(Context context, Player player) {
        super(context);
        this.player = player;
        this.width = 100;
        this.height = 20;
        this.margin = 2;

        this.borderPaint = new Paint();
        borderColor = ContextCompat.getColor(context, R.color.healthBarBorder);
        borderPaint.setColor(borderColor);

        this.healthPaint = new Paint();
        healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);
        healthPaint.setColor(healthColor);
    }


    public void draw(Canvas canvas, float x, float y) {
        //float x = (float) player.getPositionX()+25;
        //float y = (float) player.getPositionY()+25;
        distanceToPlayer = 30;
        healthPointPercentage = (float) player.getHealthPoint()/player.MAX_HEALTH_POINTS;

        // Draw border
        borderLeft = x - width/2;
        borderRight = x + width/2;
        borderBottom = y - distanceToPlayer;
        borderTop = borderBottom - height;
        canvas.drawRect(borderLeft, borderTop, borderRight, borderBottom, borderPaint);

        // Draw health

        healthWidth = width - 2*margin;
        healthHeight = height - 2*margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth*healthPointPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;
        canvas.drawRect(healthLeft, healthTop, healthRight, healthBottom, healthPaint);
    }

    public void saveState() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        SharedPreferences.Editor outState = sharedPreferences.edit();
        outState.putInt("healthx", width);
        outState.putInt("healthy", height);
        outState.putInt("margin", margin);
        outState.putInt("healthColor", healthColor);
        outState.putInt("borderColor", borderColor);
        outState.putFloat("healthLeft", healthLeft);
        outState.putFloat("healthTop", healthTop);
        outState.putFloat("healthRight", healthRight);
        outState.putFloat("healthBottom", healthBottom);
        outState.putFloat("healthWidth", healthWidth);
        outState.putFloat("healthHeight", healthHeight);
        outState.putFloat("healthRight", healthRight);
        outState.putFloat("healthBottom", healthBottom);
        outState.putFloat("healthWidth", healthWidth);
        outState.putFloat("healthHeight", healthHeight);
        outState.putFloat("distanceToPlayer", distanceToPlayer);
        outState.putFloat("healthPointPercentage", healthPointPercentage);
        outState.putFloat("borderLeft", borderLeft);
        outState.putFloat("borderTop", borderTop);
        outState.putFloat("borderRight", borderRight);
        outState.putFloat("borderBottom", borderBottom);
        outState.apply();
    }
    public void restoreState() {
        SharedPreferences savedInstanceState = getContext().getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        width = savedInstanceState.getInt("healthx",100);
        height = savedInstanceState.getInt("healthy",20);
        margin = savedInstanceState.getInt("margin", 2);
        distanceToPlayer = savedInstanceState.getFloat("distanceToPlayer", 30);
        healthColor = savedInstanceState.getInt("healthColor", ContextCompat.getColor(getContext(), R.color.healthBarHealth));
        borderColor = savedInstanceState.getInt("borderColor", ContextCompat.getColor(getContext(), R.color.healthBarBorder));
        healthWidth = savedInstanceState.getFloat("healthWidth", width - 2*margin);
        healthHeight = savedInstanceState.getFloat("healthHeight", height - 2*margin);
        borderLeft = savedInstanceState.getFloat("borderLeft", 155 - width/2);
        borderRight = savedInstanceState.getFloat("borderRight", 155 + width/2);
        borderBottom = savedInstanceState.getFloat("borderBottom", 60 - distanceToPlayer);
        borderTop = savedInstanceState.getFloat("borderTop", borderBottom - height);
        healthLeft = savedInstanceState.getFloat("healthLeft", borderLeft + margin);
        healthPointPercentage = savedInstanceState.getFloat("healthPointPercentage", (float) player.getHealthPoint()/player.MAX_HEALTH_POINTS);
        healthRight = savedInstanceState.getFloat("healthRight", healthLeft + healthWidth*healthPointPercentage);
        healthBottom = savedInstanceState.getFloat("healthBottom", borderBottom - margin);
        healthTop = savedInstanceState.getFloat("healthTop", healthBottom - healthHeight);
        healthRight= savedInstanceState.getFloat("healthRight", healthLeft + healthWidth*healthPointPercentage);
    }
    public void saveState(Bundle outState) {
        outState.putInt("healthx", width);
        outState.putInt("healthy", height);
        outState.putInt("margin", margin);
        outState.putInt("healthColor", healthColor);
        outState.putInt("borderColor", borderColor);
        outState.putFloat("healthLeft", healthLeft);
        outState.putFloat("healthTop", healthTop);
        outState.putFloat("healthRight", healthRight);
        outState.putFloat("healthBottom", healthBottom);
        outState.putFloat("healthWidth", healthWidth);
        outState.putFloat("healthHeight", healthHeight);
        outState.putFloat("healthRight", healthRight);
        outState.putFloat("healthBottom", healthBottom);
        outState.putFloat("healthWidth", healthWidth);
        outState.putFloat("healthHeight", healthHeight);
        outState.putFloat("distanceToPlayer", distanceToPlayer);
        outState.putFloat("healthPointPercentage", healthPointPercentage);
        outState.putFloat("borderLeft", borderLeft);
        outState.putFloat("borderTop", borderTop);
        outState.putFloat("borderRight", borderRight);
        outState.putFloat("borderBottom", borderBottom);
    }

    public void restoreState(Bundle savedInstanceState) {
        width = savedInstanceState.getInt("healthx");
        height = savedInstanceState.getInt("healthy");
        margin = savedInstanceState.getInt("margin");
        healthColor = savedInstanceState.getInt("healthColor");
        borderColor = savedInstanceState.getInt("borderColor");
        healthLeft = savedInstanceState.getFloat("healthLeft");
        healthTop = savedInstanceState.getFloat("healthTop");
        healthRight = savedInstanceState.getFloat("healthRight");
        healthBottom = savedInstanceState.getFloat("healthBottom");
        healthWidth = savedInstanceState.getFloat("healthWidth");
        healthHeight = savedInstanceState.getFloat("healthHeight");
        healthRight= savedInstanceState.getFloat("healthRight");
        healthBottom = savedInstanceState.getFloat("healthBottom");
        healthWidth = savedInstanceState.getFloat("healthWidth");
        healthHeight = savedInstanceState.getFloat("healthHeight");
        distanceToPlayer = savedInstanceState.getFloat("distanceToPlayer");
        healthPointPercentage = savedInstanceState.getFloat("healthPointPercentage");
        borderLeft = savedInstanceState.getFloat("borderLeft");
        borderTop = savedInstanceState.getFloat("borderTop");
        borderRight = savedInstanceState.getFloat("borderRight");
        borderBottom = savedInstanceState.getFloat("borderBottom");
    }
}
