package com.akgames.animation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private Button login,register, offline;
    private EditText username, password;
    private CheckBox remember;
    private String user, pass;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        offline = findViewById(R.id.offline);
        remember = findViewById(R.id.remember);
        sharedPreferences = getSharedPreferences("userProfileSaved",MODE_PRIVATE);
        user = "";
        pass = "";
        String check = sharedPreferences.getString("check","").toString();
        String isLoggedOut = sharedPreferences.getString("logout","").toString();
        if(check.equals("t")&& !isLoggedOut.equals("T")) {
            user = sharedPreferences.getString("user", "");
            pass = sharedPreferences.getString("pass", "");
            remember.setChecked(true);

            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("usersprofile");

            reference.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.child("password").getValue().equals(pass)){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if(remember.isChecked()) {
                                editor.putString("user", user);
                                editor.putString("pass", pass);
                                editor.putString("check", "t");
                                editor.apply();
                            }
                            else{
                                editor.putString("user", user);
                                editor.putString("pass", pass);
                                editor.putString("check", "f");
                                editor.apply();

                            }

                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("username", username.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        }
                        else {
                            username.setText("");
                            username.setHint("Username might be Wrong");
                            password.setText("");
                            password.setHint("Password might be Wrong");
                        }
                        //dataSnapshot.child("password").equals();
                    }

                    else{
                        password.setText("");
                        password.setHint("Password might be Wrong");
                        username.setText("");
                        username.setHint("Username might be Wrong");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else{
            SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", "");
                editor.putString("pass", "");
                editor.putString("check", "f");
                editor.putString("logout","");
                editor.apply();
        }
        username.setText(user.toString());
        password.setText(pass.toString());
        //=================Button Events===================
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString().trim();
                pass = password.getText().toString();
                if(user.equals("")){
                    username.setHint("cannot be blank!");
                }
                else if(user.toLowerCase().contains("fuck")){
                    username.setText("");
                    username.setHint("cannot be that");
                }
                else if(user.toLowerCase().contains("shit")){
                    username.setText("");
                    username.setHint("cannot be that");
                }
                else if(user.toLowerCase().contains("pussy")){
                    username.setText("");
                    username.setHint("cannot be that");
                }
                else if(user.toLowerCase().equals("ass")){
                    username.setText("");
                    username.setHint("cannot be that");
                }
                else if(user.toLowerCase().contains("asshole")){
                    username.setText("");
                    username.setHint("cannot be that");
                }
                else if(user.toLowerCase().contains("bastard")){
                    username.setText("");
                    username.setHint("cannot be that");
                }
                else if(user.toLowerCase().contains("cunt")){
                    username.setText("");
                    username.setHint("cannot be that");
                }
                else if(user.toLowerCase().contains("bitch")){
                    username.setText("");
                    username.setHint("cannot be that");
                }
                else if(user.length()>=8){
                    username.setText("");
                    username.setHint("Must be less than 8 letters");
                }
                else{
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("usersprofile");

                    reference.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                if(dataSnapshot.child("password").getValue().equals(pass)){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    if(remember.isChecked()) {
                                        editor.putString("user", user);
                                        editor.putString("pass", pass);
                                        editor.putString("check", "t");
                                        editor.apply();
                                    }
                                    else{
                                        editor.putString("user", user);
                                        editor.putString("pass", pass);
                                        editor.putString("check", "f");
                                        editor.apply();

                                    }

                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.putExtra("username", username.getText().toString().trim());
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    username.setText("");
                                    username.setHint("Username might be Wrong");
                                    password.setText("");
                                    password.setHint("Password might be Wrong");
                                }
                                //dataSnapshot.child("password").equals();
                            }

                            else{
                                password.setText("");
                                password.setHint("Password might be Wrong");
                                username.setText("");
                                username.setHint("Username might be Wrong");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Register.class);
                    startActivity(intent);
                    finish();
            }
        });
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", "guest");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("username", "guest");
                startActivity(intent);
                finish();
            }
        });
    }
}