package com.tomasgiro.datademo.logindemo;

import android.app.ApplicationErrorReport;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.GridView;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tomasgiro.datademo.logindemo.util.IabHelper;
import com.tomasgiro.datademo.logindemo.util.IabResult;
import com.tomasgiro.datademo.logindemo.util.Inventory;
import com.tomasgiro.datademo.logindemo.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Billing extends AppCompatActivity {

    private static final String TAG = "hi: ";
    private static final String SKU = "premium";
    private static final int REQUEST_CODE =1002 ;
    IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        final View view = new GridView(this);

        // ...
        String base64EncodedPublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhsoSFni75IkjqL0bNHXahsn+uA7AB90OfFoNji/rEEktHrgDExVyg6v6hL/qz0hG6cboWlbhvbE5MFOBUwQbBpCwV890X2C0Hklsx4HghUM5YuYiDWomHy8FMpM8WTwNVOPMXyZjxccqKD1vzIv0vwJ/YdAYxsB8i+XRe4AULU+adIFvzH+t7OfO06oqnzLxNFbjEC6WAxiygR47hGU15L2nTazse1CHS9wwD3SQXEmjvfNaQTSzKwvtU5bimGZ3LscDPjZMKirWqdaM5cn//JRQxBKTtzMe+gUZN2xrvBwZv98giaQkJRI3rjr3xn6veZdKxGGQCGRATEOGCfmRgwIDAQAB" ;


        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh no, there was a problem.
                    Log.i(TAG, "Problem setting up In-app Billing: " + result);
                }
                Log.i(TAG, "Hooray, IAB is fully set up!");

                IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                        if (result.isFailure()) {
                            Log.i(TAG, "Failure purchasing!");
                        } else {
                            String uid = FirebaseAuth.getInstance().getUid();
                            Log.i("hi: ",uid);
                            FirebaseFirestore.getInstance().collection("users").document(uid).set(new HashMap<String, Object>() {{
                                put("premium", "1");
                            }});

                        }
                    }
                };
                try {
                    mHelper.launchPurchaseFlow(Billing.this, SKU, REQUEST_CODE,
                            mPurchaseFinishedListener, "");
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                    Log.i(TAG,"an error occurred!");
                }


            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }
}
