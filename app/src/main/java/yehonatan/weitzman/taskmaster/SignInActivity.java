package yehonatan.weitzman.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The type Sign in activity.
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The Et email.
     */
    EditText etEmail,
    /**
     * The Et password.
     */
    etPassword;
    /**
     * The Btn sign in.
     */
    Button btnSignIn,
    /**
     * The Btn to sign up.
     */
    btnToSignUp;
    /**
     * The Firebasecontroller.
     */
    FirebaseController firebasecontroller;


    public void onStart() {
        super.onStart();
        firebasecontroller = new FirebaseController(this);

        if (firebasecontroller.currentUser()) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);

        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebasecontroller = new FirebaseController(this);
        initializationView();
    }

    @Override
    public void onClick(View v) {
        if(btnSignIn == v){
            firebasecontroller.signInUser(etEmail.getText().toString(),etPassword.getText().toString());{

            }
        } else if (v==btnToSignUp) {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);

        }
    }

    private void initializationView() {
        etEmail = (EditText) findViewById(R.id.etEmailSignUp);
        etPassword = (EditText) findViewById(R.id.etUserPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        btnToSignUp = findViewById(R.id.btnToSignUp);
        btnToSignUp.setOnClickListener(this);
    }


}