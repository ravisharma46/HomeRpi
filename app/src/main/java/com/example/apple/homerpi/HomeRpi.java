package com.example.apple.homerpi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.apple.homerpi.R.*;

public class HomeRpi extends AppCompatActivity {

    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference rootRef=db.getReference();
    DatabaseReference fan_switchRef=rootRef.child("Fan");
    DatabaseReference light_switchRef=rootRef.child("Light");
    DatabaseReference wifi_switchRef=rootRef.child("Wifi");


   // ProgressBar bar;
    Switch fan,light,wifi;
    Button signout;
    ImageView bulb,fan_switch,wifi_switch;
  //  static  int setProgress=0;


    public static final String light_NAME = "MyPrefsFile";
    public static final String wifi_NAME = "MyPrefsFile";

    @Override
    protected void onStart() {
        super.onStart();

        android.net.ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        android.net.NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            Toast.makeText(HomeRpi.this, "INTERNET is Available", Toast.LENGTH_SHORT).show();


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setIcon(R.drawable.internet_logo);
            builder.setTitle("NO INTERNET");
            builder.setMessage("Please Check Your Internet Connection.");
            builder.setPositiveButton("Enable INTERNET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);

                }
            });
            builder.show();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home_rpi);

        fan= (Switch) findViewById(id.fan_button);
        light= (Switch) findViewById(id.light_button);
        wifi= (Switch) findViewById(id.wifi_button);
        signout=(Button)findViewById(id.button_signOut);
        bulb=(ImageView) findViewById(id.bulb_status);
        fan_switch=(ImageView) findViewById(id.fan_status);
        wifi_switch=(ImageView) findViewById(id.wifi_status);
      //  bar=(ProgressBar)findViewById(id.progressBar);


        //fan_switch_status

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

                    fan_switch.setImageResource(drawable.fan_on);
                    Toast.makeText(HomeRpi.this,"Fan "+fan.getTextOn().toString(),Toast.LENGTH_SHORT).show();

                }else {
                    sharedPreferences1.edit().putBoolean("isChecked", false).apply();;
                    fan_switchRef.setValue("OFF");
                    fan_switch.setImageResource(drawable.fan_off);
                    Toast.makeText(HomeRpi.this,"Fan "+fan.getTextOff().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });




        //light_switch_status

        SharedPreferences settings = getSharedPreferences(light_NAME, 0);
        boolean silent = settings.getBoolean("switchkey_light", false);
        light.setChecked(silent);

        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    light_switchRef.setValue("ON");
                    bulb.setImageResource(drawable.bulb_on);
                    Toast.makeText(HomeRpi.this,"Light "+light.getTextOn().toString(),Toast.LENGTH_SHORT).show();
                }
                else{
                    light_switchRef.setValue("OFF");
                    bulb.setImageResource(drawable.bulb_off);
                    Toast.makeText(HomeRpi.this,"Light "+light.getTextOff().toString(),Toast.LENGTH_SHORT).show();
                }
                SharedPreferences settings = getSharedPreferences(light_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("switchkey_light", b);
                editor.commit();
            }
        });






        //wifi_switch_status

        SharedPreferences setting = getSharedPreferences(wifi_NAME, 0);
        boolean silent_wifi = setting.getBoolean("switchkey_wifi", false);
        wifi.setChecked(silent_wifi);

        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b  ){
                    wifi_switchRef.setValue("ON");
                    wifi_switch.setImageResource(drawable.wifi_on);

                    Toast.makeText(HomeRpi.this,"Wifi "+wifi.getTextOn().toString(),Toast.LENGTH_SHORT).show();
                }
                else{
                    wifi_switchRef.setValue("OFF");
                    wifi_switch.setImageResource(drawable.wifi_off);
                    Toast.makeText(HomeRpi.this,"Wifi "+wifi.getTextOff().toString(),Toast.LENGTH_SHORT).show();
                }
                SharedPreferences setting = getSharedPreferences(wifi_NAME, 0);
                SharedPreferences.Editor editor = setting.edit();
                editor.putBoolean("switchkey_wifi", b);
                editor.commit();

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
