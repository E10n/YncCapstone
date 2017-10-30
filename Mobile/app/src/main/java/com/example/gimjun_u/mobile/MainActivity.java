package com.example.gimjun_u.mobile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;
import com.example.gimjun_u.mobile.SamplePresenter;
import com.example.gimjun_u.mobile.SamplePresenter.SampleView;
import com.example.gimjun_u.mobile.SqlLite;

import java.util.Random;


public class MainActivity extends LocationBaseActivity implements SampleView{

    @Bind(R.id.result)
    TextView result;

    @Bind(R.id.locationText)
    TextView locationText;

    private ProgressDialog progressDialog;
    private SamplePresenter samplePresenter;
    private SqlLite sqlLite;

    private boolean running;
    Random mRand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Reprint.initialize(this);
        samplePresenter = new SamplePresenter(this);
        getLocation();


        if (running) {
            cancel();
        } else {
            start();
        }

    }

    private void start() {
        if (Reprint.isHardwarePresent()){
            if (Reprint.hasFingerprintRegistered()){
                mRand = new Random();
                String nFinger = RandFinger(2,5);
                result.setText("Please put "+nFinger+" to Device");
                running = true;
                fingerprintRegistration();
            }
            else if (!Reprint.hasFingerprintRegistered()){
                running = false;
                alertFingerprint();
            }
        }
        else if (!Reprint.isHardwarePresent()){
            finish();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        samplePresenter.destroy();
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.defaultConfiguration("Gimme the permission!", "Would you mind to turn GPS on?");
    }

    @Override
    public void onLocationChanged(Location location) {
        samplePresenter.onLocationChanged(location);
    }

    @Override
    public void onLocationFailed(@FailType int failType) {
        samplePresenter.onLocationFailed(failType);
    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        samplePresenter.onProcessTypeChanged(processType);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissProgress();
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public String getText() {
        return locationText.getText().toString();
    }

    @Override
    public void setText(String text) {
        locationText.setText(text);
    }

    @Override
    public void updateProgress(String text) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(text);
        }
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public String RandFinger(int N,int M) {

        int nResult = mRand.nextInt(N);
        int mResult = mRand.nextInt(M);
        String[] handNum = {"Left","Right"};
        String[] fingerNum = {"thumb","index finger","middle finger","ring finger","little finger"};

        return handNum[nResult]+" "+fingerNum[mResult];

    }

    private void fingerprintRegistration() {
        Reprint.authenticate(new AuthenticationListener() {
            @Override
            public void onSuccess(int moduleTag) {
                showSuccess();
            }

            @Override
            public void onFailure(AuthenticationFailureReason failureReason, boolean fatal,
                                  CharSequence errorMessage, int moduleTag, int errorCode) {
                showError(failureReason, fatal, errorMessage, errorCode);
            }
        });
    }

    public void alertFingerprint(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fingerprints not registered");
        builder.setMessage("Are you going to register?");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        finish();
                        break;
                }
            }
        };
        builder.setPositiveButton("Registration",dialogClickListener);
        builder.setNegativeButton("Exit",dialogClickListener);
        builder.create().show();
    }

    private void cancel() {
        result.setText("Fingerprint authentication failure");
        running = false;
        Reprint.cancelAuthentication();
    }

    private void showSuccess() {
        result.setText("Fingerprint authentication success");
        running = false;
    }

    private void showError(AuthenticationFailureReason failureReason, boolean fatal,
                           CharSequence errorMessage, int errorCode) {
        result.setText(errorMessage);

        if (fatal) {
            running = false;
        }
    }

}
