package com.tomasgiro.datademo.logindemo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CaptureRequest;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

/**
 * Created by Tomas on 10/03/2018.
 */


public class MyPokedexAdapter extends RecyclerView.Adapter<MyPokedexAdapter.ViewHolder> {
    private String[][] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ConstraintLayout mConstraintLayout;
        public TextView title;
        public TextView text;
        private FrameLayout textFrameLayout;
        private Button button;
        private ImageView imageView;
        public ViewHolder(ConstraintLayout mConstraintLayout) {
            super(mConstraintLayout);
            this.mConstraintLayout = mConstraintLayout;
            this.textFrameLayout = (FrameLayout) mConstraintLayout.getChildAt(1);
            this.imageView = (ImageView) ((FrameLayout) mConstraintLayout.getChildAt(0)).getChildAt(0);
            this.title= (TextView)  textFrameLayout.getChildAt(0);
            this.button =(Button) mConstraintLayout.getChildAt(2);




        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyPokedexAdapter(String[][] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyPokedexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_pokedex_text_view, parent, false);

        ViewHolder vh = new ViewHolder(constraintLayout);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TextView title=(TextView) holder.title;
        final TextView text =(TextView) holder.text;
        title.setText(mDataset[0][position]);
        Log.i("hi:","text set to: "+mDataset[0][position]);
        //text.setText("Infos: "+mDataset[1][position]);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ShowWiki.class);
                intent.putExtra("nomEspece", title.getText());
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
}