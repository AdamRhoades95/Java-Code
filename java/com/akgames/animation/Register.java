package com.akgames.animation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private Button create, cancel;
    private EditText username, password;
    private String user, pass;
    //private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    //private FirebaseAuth mAuth;
    //private FirebaseAuth mAuth;
// ...
// Initialize Firebase Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         cancel = findViewById(R.id.cancel);
         create = findViewById(R.id.create);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        user = "";
        pass = "";

        //mAuth = FirebaseAuth.getInstance();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString().trim();
                pass = password.getText().toString().trim();

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
                else if(user.equals("guest")){
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
                else if (pass.equals("")){
                    password.setHint("Cannot be Blank");
                }

                else {
                    reference = FirebaseDatabase.getInstance().getReference().child("usersprofile");
                    //reference = rootNode.getReference().child("usersprofile");
                    reference.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                username.setText("");
                                username.setHint("User Taken");
                                //dataSnapshot.child("password").equals();
                            } else {
                                /*final AsyncTask<Void,Void,Void> task = new AsyncTask<Void,Void,Void>(){
                                    protected Void doInBackground(Void... voids) {
                                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass)
                                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d(TAG, "createUserWithEmail:success");
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    updateUI(user);
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    updateUI(null);
                                                }

                                                // ...
                                            }
                                        });*/
                                        try {
                                            reference = FirebaseDatabase.getInstance().getReference().child("usersprofile");
                                            Users users = new Users(user, pass);
                                            reference.push();
                                            reference.child(user).setValue(users);
                                        }
                                        catch(Exception e){
//
                                        }
//
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                intent.putExtra("username", username.getText().toString().trim());
//
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

//
//
//                    //reference.child(user.getUserPassword()).setValue(user);
//
                    });
                }
            }
        });
    }

}