package yehonatan.weitzman.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;


public class OpeningActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnSignUp;
    Button btnSigIn;

    FirebasecController firebasecontroller;


    public void onStart() {
        super.onStart();
        firebasecontroller = new FirebasecController(this);

        if (firebasecontroller.currentUser()) {
            Intent intent = new Intent(OpeningActivity.this, MainActivity.class);
            startActivity(intent);

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        btnSignUp = (Button) findViewById(R.id.btnSignUpToActivity);
        btnSigIn = (Button) findViewById(R.id.btnSignInToActivity);

        btnSignUp.setOnClickListener(this);
        btnSigIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (btnSignUp == v) {
            Intent intent = new Intent(OpeningActivity.this, SignUpActivity.class);
            startActivity(intent);

        } else if (btnSigIn == v) {
            Intent intent = new Intent(OpeningActivity.this, SignInActivity.class);
            startActivity(intent);

        }


    }
}