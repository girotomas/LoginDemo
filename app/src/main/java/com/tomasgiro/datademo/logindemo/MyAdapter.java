package com.tomasgiro.datademo.logindemo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomas on 10/03/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[][] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ConstraintLayout mConstraintLayout;
        public TextView title;
        public TextView resume;
        public ImageView imageView;
        public Button button;

        public ViewHolder(ConstraintLayout mConstraintLayout) {
            super(mConstraintLayout);
            this.mConstraintLayout = mConstraintLayout;
            this.title = (TextView) mConstraintLayout.getChildAt(1);
            this.resume = (TextView) mConstraintLayout.getChildAt(2);
            this.imageView = (ImageView) mConstraintLayout.getChildAt(0);
            this.button = (Button) mConstraintLayout.getChildAt(3);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[][] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        ViewHolder vh = new ViewHolder(constraintLayout);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView title = (TextView) holder.mConstraintLayout.getChildAt(1);
        title.setText(mDataset[0][position]);
        final TextView resume = (TextView) holder.mConstraintLayout.getChildAt(2);
        resume.setText("Probabilité: " + round(Double.parseDouble(mDataset[1][position]), 2));
        Button confirm = (Button) holder.mConstraintLayout.getChildAt(3);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uid).collection("pokedex").document(mDataset[0][position]);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        if(!documentSnapshot.exists()){
                            documentReference.set(new HashMap<String, Object>() {{
                            put("mDataset[0][position]","1");
                        }}
                            );
                            Log.i("hi: ","new species added: "+mDataset[0][position]);
                        }
                        else{
                            Log.i("hi: ","you all ready have the species!");
                        }

                    }
                });


                Intent intent = new Intent(view.getContext(),ShowWiki.class);
                intent.putExtra("nomEspece",mDataset[0][position]);
                view.getContext().startActivity(intent);
            }
        });

        StorageReference storageReferenceJPG = FirebaseStorage.getInstance().getReference().child("img_" + mDataset[0][position] + "0_" + ".jpg");
        Log.i("hi: ", "img_" + mDataset[0] + "0_" + ".jpg");
        storageReferenceJPG.getBytes(1024 * 1024 * 5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

            }
        });

        StorageReference storageReferencePNG = FirebaseStorage.getInstance().getReference().child("img_" + mDataset[0][position] + "0_" + ".png");
        Log.i("hi: ", "img_" + mDataset[0] + "0_" + ".png");

        storageReferencePNG.getBytes(1024 * 1024 * 5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset[0].length;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}