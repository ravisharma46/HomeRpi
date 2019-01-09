package com.example.apple.homerpi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyPhoneActivity extends AppCompatActivity {

    private String verificationID;
    private FirebaseAuth mAuth;
    private EditText editTextCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        String mobile=getIntent().getStringExtra("mobile");
        sendVerificationCode(mobile);
        mAuth= FirebaseAuth.getInstance();
        editTextCode=findViewById(R.id.editTextCode);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code= editTextCode.getText().toString().trim();

                if(code.isEmpty() || code.length()<6){
                    editTextCode.setError("Enter code...");
                    editTextCode.requestFocus();
                    return;
                }
                verifycode(code);
            }
        });



    }
    private void verifycode(String code){

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationID,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Intent intent= new Intent(verifyPhoneActivity.this,HomeRpi.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                        }
                        else {

                            Toast.makeText(verifyPhoneActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String mobile){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mobile,20,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //Log.i("tag",s);
            super.onCodeSent(s, forceResendingToken);
            Log.e("TAG", "onCodeSent: s - " + s + " : t - " + forceResendingToken);
            verificationID=s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code=phoneAuthCredential.getSmsCode();
            //Log.i("TAG",code);
            if(code!=null){
                editTextCode.setText(code);
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(verifyPhoneActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

}
