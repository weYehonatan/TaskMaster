package yehonatan.weitzman.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail,etPassword;
    Button btnSignIn;
    FirebasecController firebasecController;
    SharedPreferences sp;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        firebasecController = new FirebasecController(this);
        sp=getSharedPreferences("details1",0);
        String strUserEmail = sp.getString("userEmail",null);
        String strUserPass = sp.getString("userPass",null);



    }

    @Override
    public void onClick(View v) {
        if(btnSignIn == v){
            firebasecController.signInUser(etEmail.getText().toString(),etPassword.getText().toString());{

            }
        }
    }
}