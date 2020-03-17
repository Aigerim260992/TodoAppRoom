package com.geektech.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText editPhone, editCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    LinearLayout codeLinear, numberLinear;
    String codeVerificator;
    private TextView timerTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        editPhone = findViewById(R.id.editPhone);
        editCode = findViewById(R.id.editCode);
        codeLinear = findViewById(R.id.code_linear);
        numberLinear = findViewById(R.id.number_linear);
        timerTv = findViewById(R.id.timerTv);



        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.e("TAG", "onVerificationCompleted");
                signIn(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e("TAG", "onVerificationFailed: " + e.getMessage());

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeVerificator =s;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };

    }
    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PhoneActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                } else{
                    Toast.makeText(PhoneActivity.this, "Ошибка" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void onClickContinue(View view) {
        String phone = editPhone.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60,
                TimeUnit.SECONDS, this, callbacks);
        codeLinear.setVisibility(View.VISIBLE);
        numberLinear.setVisibility(View.GONE);
        timerForCode();

    }


    public void onClickCode(View view) {
        String code =  editCode.getText().toString();
        if(TextUtils.isEmpty(code)){
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeVerificator, code);
        signIn(credential);

    }

    private void timerForCode(){
        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTv.setText("" + (millisUntilFinished/1000));

            }

            @Override
            public void onFinish() {
                codeLinear.setVisibility(View.GONE);
                numberLinear.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}
