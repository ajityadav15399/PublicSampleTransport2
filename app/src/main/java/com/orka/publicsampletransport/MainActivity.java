package com.orka.publicsampletransport;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int HANDLE_STORAGE_PERMISSION = 1;
    private Button reg,logg;
    private EditText email,pwd;
    private ProgressDialog ps;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        ps = (ProgressDialog) new ProgressDialog(this);
        reg = (Button) findViewById(R.id.register);
        email = (EditText) findViewById(R.id.emailField);
        pwd = (EditText) findViewById(R.id.passField);
        logg = (Button) findViewById(R.id.Log);
        logg.setOnClickListener(this);
        reg.setOnClickListener(this);
    }
    private void registerUser(){
        String eml=email.getText().toString().trim();
        String pass=pwd.getText().toString().trim();
        if(TextUtils.isEmpty(eml)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        ps.setMessage("Registering User...");
        ps.show();
        firebaseAuth.createUserWithEmailAndPassword(eml,pass).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                            ps.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                            ps.dismiss();
                        }

                    }
                });
    }

    private void goToLogin(){
        finish();
        startActivity(new Intent(getApplicationContext(),Login_Option.class));
    }

    @Override
    public void onClick(View view) {
        if(view==reg){
            registerUser();
        }else if(view==logg){
            goToLogin();
        }
    }
}
