package com.example.apple.homerpi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeRpi extends AppCompatActivity {

    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference rootRef=db.getReference();
    DatabaseReference fan_switchRef=rootRef.child("Fan");
    DatabaseReference light_switchRef=rootRef.child("Light");
    DatabaseReference wifi_switchRef=rootRef.child("Wifi");



    Switch fan,light,wifi;
    Button signout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_rpi);

        fan= (Switch) findViewById(R.id.fan_button);
        light= (Switch) findViewById(R.id.light_button);
        wifi= (Switch) findViewById(R.id.wifi_button);
        signout=(Button)findViewById(R.id.button_signOut);


        boolean value = true; // default value if no value was found

        final SharedPreferences sharedPreferences1= getSharedPreferences("isChecked", 0);

        value = sharedPreferences1.getBoolean("isChecked", value);
        // retrieve the value of your key
        fan.setChecked(value);

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedPreferences1.edit().putBoolean("isChecked", true).apply();
                    fan_switchRef.setValue("ON");
                    fan.setBackgroundColor(Color.parseColor("#FF80DFB0"));

                    Toast.makeText(HomeRpi.this,"Fan "+fan.getTextOn().toString(),Toast.LENGTH_SHORT).show();

                }else {
                    sharedPreferences1.edit().putBoolean("isChecked", false).apply();;
                    fan_switchRef.setValue("OFF");
                    fan.setBackgroundColor(Color.parseColor("#ed252f"));

                    Toast.makeText(HomeRpi.this,"Fan "+fan.getTextOff().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){


                    light_switchRef.setValue("ON");

                    Toast.makeText(HomeRpi.this,"Light "+light.getTextOn().toString(),Toast.LENGTH_SHORT).show();
                }
                else{


                    light_switchRef.setValue("OFF");
                    Toast.makeText(HomeRpi.this,"Light "+light.getTextOff().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });





        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b  ){


                    wifi_switchRef.setValue("ON");

                    Toast.makeText(HomeRpi.this,"Wifi "+wifi.getTextOn().toString(),Toast.LENGTH_SHORT).show();
                }
                else{


                    wifi_switchRef.setValue("OFF");
                    Toast.makeText(HomeRpi.this,"Wifi "+wifi.getTextOff().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeRpi.this,"Sign Out",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(HomeRpi.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });




    }
}
