package yehonatan.weitzman.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp;
    EditText etUserEmail,etPassword,etUserName;
    FirebasecController firebasecController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        etUserEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etUserName = findViewById(R.id.etUserName);


        firebasecController = new FirebasecController(this);


    }




    @Override
    public void onClick(View v) {
        if(btnSignUp == v && etUserName != null)
        {
           firebasecController.creatUser(etUserEmail.getText().toString(),etPassword.getText().toString(),etUserName.getText().toString());
            Toast.makeText(this,"wellcome!",Toast.LENGTH_LONG).show();
        }
        else if( etUserName == null){
            Toast.makeText(this,"eror, need a userName",Toast.LENGTH_LONG).show();
        }


    }



}


