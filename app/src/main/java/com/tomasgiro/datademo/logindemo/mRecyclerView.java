package com.tomasgiro.datademo.logindemo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mRecyclerView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String[][] myDataset=new String[][]{{"","","","",""},{"","","","",""}};
    private int GET_THE_FKIN_JSON;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_recycler_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);





        // specify an adapter (see also next example)
        Intent intent = new Intent(this, TakeAPicture.class);
        startActivityForResult(intent,GET_THE_FKIN_JSON);



        //String[][] myDataset= new String[][]{{"hi","lkjljl"},{"hiki","this bird is the shit"}};

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_THE_FKIN_JSON){
            if(resultCode==RESULT_CANCELED){
                finish();
            }
            String myData=data.getStringExtra("stringResult");
            Log.i("data received:", myData);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(myData);
                Log.i("hi", String.valueOf(jsonObject.get("classes")));
                JSONArray jsonArrayClasses = jsonObject.getJSONArray("classes");
                Log.i("jsonArrayClases: ",jsonArrayClasses.getString(0));
                for (int i =0;i<5;i++){
                    myDataset[0][i]=jsonArrayClasses.getString(i);
                }
                JSONArray jsonArrayProbas = jsonObject.getJSONArray("probas");
                for (int i =0;i<5;i++){
                    myDataset[1][i]= jsonArrayProbas.getString(i);
                }
                Log.i("hi: ",myDataset[0].toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }





            mAdapter = new MyAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);





        }
        else{
            Log.i("hi:" , "not the good requestCode");
        }

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
            Log.i("hi: ","ok");
            startActivity(new Intent(this,MaPokedex.class));
        }
        if(item.getItemId()==R.id.take_picture_action){
            Intent intent = new Intent(this, mRecyclerView.class);
            startActivity(intent);
        }
        return false;
    }


}
