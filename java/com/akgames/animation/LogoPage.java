package com.akgames.animation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LogoPage extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String user, pass;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_page);
//        TimerTask task = new TimerTask(){
        sharedPreferences = getSharedPreferences("userProfileSaved",MODE_PRIVATE);
//            @Override
//            public void run() {
                //finish();
        String check = sharedPreferences.getString("check","").toString();
        String isLoggedOut = sharedPreferences.getString("logout","").toString();
        if(check.equals("t") && !isLoggedOut.equals("T")) {
            user = sharedPreferences.getString("user", "");
            pass = sharedPreferences.getString("pass", "");
            //remember.setChecked(true);

            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("usersprofile");
            //String status

//            boolean wifi, mobD;
            try {
                ConnectivityManager connMGR = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                //System.out.println(connMGR);
                boolean test = false;
                if(connMGR!=null&& connMGR.getActiveNetworkInfo().isConnected()) {
                    test = connMGR.getActiveNetworkInfo().isConnected();
                }

////            NetworkInfo activityInfo = connMGR.getActiveNetworkInfo();
                //System.out.println(test);
                reference.child(user).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.child("password").getValue().equals(pass)) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("username", user);
                                startActivity(intent);
                                finish();
                            }
                        }
                        //else{System.out.println("fuck");}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }
            catch(Exception e){
                //System.out.println(e);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("username", "guest");
                startActivity(intent);
                finish();
            }
//            if(activityInfo !=null && activityInfo.isConnected()){
//            try{
//               // Process process = java.lang.Runtime.getRuntime().exec("ping https://www.google.com");
////                InetAddress ipaddr = InetAddress.getByName("www.google.com");
////                //URLConnection connection = url.openConnection();
////                //connection.connect();
////                System.out.println(ipaddr);
//
//            }
//            catch (Exception e){
//                System.out.println(e);
//            }
//            }
//            else{
//                System.out.println("no network");
//            }

        }
        else {

            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    startActivity(new Intent(LogoPage.this, Login.class));
                    finish();
                }
            };
            Timer opening = new Timer();
            opening.schedule(task, 3000);
        }
    }
}