package com.tomasgiro.datademo.logindemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Settings extends AppCompatActivity {
    Button delete;
    CredentialsClient mCredentialsApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        delete = findViewById(R.id.delete_button);
        mCredentialsApiClient = Credentials.getClient(this);
    }

    public void onDeleteList(View view){
        Log.i("hi: ","delete called");
        String uid =FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("users").document(uid).collection("pokedex").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentSnapshot documentSnapshot: documentSnapshots){
                    documentSnapshot.getReference().delete();
                    Toast.makeText(delete.getContext(),"Votre liste est vide à présent.", Toast.LENGTH_LONG);

                }
            }
        });



    }
    public void onGetPremium(View view){
        startActivity(new Intent(this,Billing.class));
    }

    public  void onSignOut(View view){

        FirebaseAuth.getInstance().signOut();
        mCredentialsApiClient.disableAutoSignIn();
        finish();
    }
}
