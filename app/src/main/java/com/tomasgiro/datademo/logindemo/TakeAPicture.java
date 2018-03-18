package com.tomasgiro.datademo.logindemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class TakeAPicture extends AppCompatActivity {
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_apicture);
        dispatchTakePictureIntent();



    }


    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {


        @Override
        protected Long doInBackground(URL... urls) {
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(1, TimeUnit.HOURS)
                    .connectTimeout(1,TimeUnit.HOURS)
                    .build();


            //Drawable drawable = getResources().getDrawable(R.drawable.lol);
            // Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            byte[] bitmapdata = stream.toByteArray();






            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "logo-square.jpeg",
                            RequestBody.create(MEDIA_TYPE_PNG, bitmapdata))
                    .build();

            Request request = new Request.Builder()
                    .url("http://ramp.studio:24000/predict_from_image")
                    .post(requestBody)

                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String text = response.body().string();

                Log.i("hi", text);


                Intent intent = new Intent();
                Log.i("hi: resultSent:" ,text);
//---set the data to pass back---
                intent.putExtra("stringResult",text);
                setResult(RESULT_OK, intent);
//---close the activity---
                finish();




            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Response response = client.newCall(request).execute();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

            return null;
        }







    }



    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("hi", "Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.tomasgiro.datademo.logindemo.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_TAKE_PHOTO){
            Log.i("hi: ","result received!");
            new DownloadFilesTask().execute();



        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



}
