package com.developer.darren.iotapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.darren.iotapplication.data.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WeatherActivity extends AppCompatActivity {

    private TextView tempTV, pressTV;
    private Button signOutBtn;
    private FirebaseDatabase database;
    private DatabaseReference tempRef, pressRef;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tempTV = findViewById(R.id.tempTV);
        pressTV = findViewById(R.id.pressTV);
        signOutBtn = findViewById(R.id.signOutBtn);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Toast.makeText(getApplicationContext(),"Please log in first to access weather activity",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    currentUser = firebaseAuth.getCurrentUser();
                    Constants.UID_CURRENT_USER = currentUser.getUid();
                    tempRef = database.getReference(Constants.UID_CURRENT_USER+"/"+Constants.KEY_TEMP);
                    pressRef = database.getReference(Constants.UID_CURRENT_USER+"/"+Constants.KEY_PRESS);
                    tempRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            float temp = dataSnapshot.getValue(float.class);
                            tempTV.setText("Temperature is "+temp);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    pressRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long pressure = dataSnapshot.getValue(long.class);
                            pressTV.setText("Pressure is "+pressure);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    signOutBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            auth.signOut();
                        }
                    });
                }
            }
        });
    }
}