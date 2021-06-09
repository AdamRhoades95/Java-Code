package com.akgames.animation;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private AdView adView;
    private int round, kills, modeCheck;
    private  long totalPoints;
    private boolean dead;
    private InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        totalPoints = getIntent().getLongExtra("totalPoints", 0);
        round = getIntent().getIntExtra("round", 1);
        kills = getIntent().getIntExtra("kills", 0);
        modeCheck = getIntent().getIntExtra("modeCheck", 0);
        dead = getIntent().getBooleanExtra("dead", false);
        //adView = findViewById(R.id.adView);

//        MobileAds.initialize(MainActivity2.this);
//        AdRequest adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);
        try{
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-8676849276961960/4419861001");
        }catch (Exception e){

        }
        //interstitialAd.
        try{
        interstitialAd.loadAd(new AdRequest.Builder().build());
        }catch (Exception e){

        }
        if(interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
        else{
            interstitialAd.setAdListener( new AdListener() {
                @Override
                public void onAdLoaded() {
                    interstitialAd.show();
                    // Showing a simple Toast message to user when an ad is loaded
                    //Toast.makeText ( MainActivity.this, "Interstitial Ad is Loaded", Toast.LENGTH_LONG).show() ;
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    super.onAdClosed();
                    Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                    intent.putExtra("totalPoints", totalPoints);
                    intent.putExtra("round", round);
                    intent.putExtra("kills", kills);
                    intent.putExtra("dead", dead);
                    intent.putExtra("modeCheck", modeCheck);
                    startActivity(intent);
                    finish();
                    // Showing a simple Toast message to user when and ad is failed to load
                    //Toast.makeText ( MainActivity2.this, "Interstitial Ad Failed to Load ", Toast.LENGTH_LONG).show() ;
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                    intent.putExtra("totalPoints", totalPoints);
                    intent.putExtra("round", round);
                    intent.putExtra("kills", kills);
                    intent.putExtra("dead", dead);
                    intent.putExtra("modeCheck", modeCheck);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity2.this,MainActivity.class);
        intent.putExtra("totalPoints", totalPoints);
        intent.putExtra("round", round);
        intent.putExtra("kills", kills);
        intent.putExtra("dead", dead);
        intent.putExtra("modeCheck", modeCheck);
        startActivity(intent);
        finish();
    }
}