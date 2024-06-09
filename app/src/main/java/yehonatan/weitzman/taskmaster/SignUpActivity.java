package yehonatan.weitzman.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The type Sign up activity.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The Btn sign up.
     */
    Button btnSignUp,
    /**
     * The Btn to sign in.
     */
    btnToSignIn;
    /**
     * The Et user email.
     */
    EditText etUserEmail,
    /**
     * The Et password.
     */
    etPassword,
    /**
     * The Et user name.
     */
    etUserName;
    /**
     * The Firebase controller.
     */
    FirebaseController firebaseController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseController = new FirebaseController(this);
        initializationView();

    }
    @Override
    public void onClick(View v) {
        if(btnSignUp == v && etUserName != null)
        {
            firebaseController.creatUser(etUserEmail.getText().toString(),etPassword.getText().toString(),etUserName.getText().toString());
            Toast.makeText(this,"wellcome!",Toast.LENGTH_LONG).show();
        }
        else if( etUserName == null){
            Toast.makeText(this,"eror, need a userName",Toast.LENGTH_LONG).show();
        } else if (v == btnToSignIn) {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        }


    }
    private void initializationView() {
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        btnToSignIn = findViewById(R.id.btnToSignIn);
        btnToSignIn.setOnClickListener(this);
        etUserEmail = (EditText)findViewById(R.id.etEmailSignUp);
        etPassword = (EditText)findViewById(R.id.etUserPassword);
        etUserName = findViewById(R.id.etUserName);
    }






}


