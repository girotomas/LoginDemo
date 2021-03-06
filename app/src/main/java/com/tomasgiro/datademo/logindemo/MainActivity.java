package com.tomasgiro.datademo.logindemo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Main Activity";
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button signIn;
    private Button signUp;
    private TextView userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        email= (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.signIn);
        signUp = (Button) findViewById(R.id.signUp);
        userStatus =(TextView) findViewById(R.id.userStatus);

        Intent intent= new Intent(this, FirebaseLogin.class);
        startActivity(intent);
        finish();

    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            userStatus.setText("Congratulations you successfully signed In! \n your email is "+email);
            Toast.makeText(MainActivity.this, "you are signed in", Toast.LENGTH_SHORT).show();

        }
        else{
            userStatus.setText("You are currently not logged in!");
            Toast.makeText(MainActivity.this, "you are not logged in", Toast.LENGTH_LONG).show();
            Log.i("main tomas", "you are not logged in!");
        }
    }

    void onSignIn(View v){
        String emailText= email.getText().toString();
        String passwordText= password.getText().toString();
        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    void onSignUp(View v){
        String emailText= email.getText().toString();
        String passwordText= password.getText().toString();


        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    void onSignOut(View v){
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }

    void onPlay(View v){

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(this,Game.class);
            startActivity(intent);

        }
        else{
            Toast.makeText(MainActivity.this, "Sorry, you must be logged in to play !", Toast.LENGTH_LONG).show();
        }
    }

    void onSignInWithGoogle(View view){
        Intent intent= new Intent(this, GoogleConnect.class);
        startActivity(intent);
    }
}
