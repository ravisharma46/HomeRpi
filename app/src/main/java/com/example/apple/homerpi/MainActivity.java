package com.example.apple.homerpi;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMobile;
    private Button button;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMobile= (EditText) findViewById(R.id.editTextMobile);
        button= (Button) findViewById(R.id.buttonContinue);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mobile=editTextMobile.getText().toString().trim();
                if(mobile.isEmpty() || mobile.length()<10 || mobile.length()>10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }



                Intent intent=new Intent(MainActivity.this,verifyPhoneActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        check_internet_connection();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent= new Intent(this,HomeRpi.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            Toast.makeText(MainActivity.this,"Already In",Toast.LENGTH_SHORT).show();
        }




    }


    private void check_internet_connection(){
        android.net.ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        android.net.NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            Toast.makeText(MainActivity.this, "INTERNET is Available", Toast.LENGTH_SHORT).show();





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

}
