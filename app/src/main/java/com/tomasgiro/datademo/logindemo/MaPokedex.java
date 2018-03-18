package com.tomasgiro.datademo.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MaPokedex extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter pokedexAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String[][] myDataset;
    private ArrayList<String> nomsDEspeces= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_pokedex);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_pokedex_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);



        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        String uid = FirebaseAuth.getInstance().getUid();
        CollectionReference collectionReference =FirebaseFirestore.getInstance().collection("users").document(uid).collection("pokedex");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for(DocumentSnapshot documentSnapshot: documentSnapshots.getDocuments()){
                    nomsDEspeces.add(documentSnapshot.getId());
                    Log.i("hi: adding: ",documentSnapshot.getId());

                }

                myDataset= new String[1][nomsDEspeces.size()];
                for(int i=0; i<nomsDEspeces.size(); i++){
                    myDataset[0][i]=nomsDEspeces.get(i);
                }
                Log.i("hi: noms especes.size: ", String.valueOf(nomsDEspeces.size()));

                if(myDataset[0].length==0){
                    startActivity(new Intent(mRecyclerView.getContext(),WelcomeScreen.class));
                }

                pokedexAdapter = new MyPokedexAdapter(myDataset);
                mRecyclerView.setAdapter(pokedexAdapter);

            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
        Log.i("hi: ", "item clicked in menu!");
        if(item.getItemId()==R.id.action_favorite){

        }
        if(item.getItemId()==R.id.take_picture_action){
            Intent intent = new Intent(this, mRecyclerView.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.action_settings){
            startActivity(new Intent(this,Settings.class));
        }
        return false;
    }

}
