/// Class not usable right now



package com.orka.publicsampletransport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {

    private Button Log;
    private EditText Lid,Lpass;
    private ProgressDialog ps;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){

        }
        Log=(Button) findViewById(R.id.login);
        ps=(ProgressDialog) new ProgressDialog(this);
        Lid=(EditText) findViewById(R.id.emailFieldL);
        Lpass=(EditText) findViewById(R.id.passFieldL);

        Log.setOnClickListener(this);
    }

    private  void  userlogin(){
        String email=Lid.getText().toString();
        String pwd=Lpass.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }


       // ps.show();
        if(email=="admin" || pwd=="admin"){
            Toast.makeText(this," "+email+" "+pwd,Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),PostListActivity.class));
        }
     /*   firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ps.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(),AdminDisplayRecords.class));
                }
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        if(view==Log){
            if(Lid.getText().toString().equals("admin") &&
                    Lpass.getText().toString().equals("admin")) {
                Toast.makeText(getApplicationContext(),
                        "Redirecting...",Toast.LENGTH_SHORT).show();
         //       finish();
                startActivity(new Intent(getApplicationContext(),PostListActivity.class));

            }
            else{
                Toast.makeText(getApplicationContext(),
                        "Wrong ID/PASSWORD",Toast.LENGTH_SHORT).show();
            }
            //userlogin();
        }
    }
}
