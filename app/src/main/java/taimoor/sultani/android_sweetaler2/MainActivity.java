package taimoor.sultani.android_sweetaler2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import taimoor.sultani.customlog.CustomLogs;
import taimoor.sultani.sweetalert2.Sweetalert;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button loading_sweetalert, basic_sweetalert, error_sweetalert, delete_confirmation_sweetalert, custom_sweetalert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading_sweetalert = findViewById(R.id.loading_sweetalert);
        basic_sweetalert = findViewById(R.id.basic_sweetalert);
        error_sweetalert = findViewById(R.id.error_sweetalert);
        delete_confirmation_sweetalert = findViewById(R.id.delete_confirmation_sweetalert);
        custom_sweetalert = findViewById(R.id.custom_sweetalert);

        loading_sweetalert.setOnClickListener(this);
        basic_sweetalert.setOnClickListener(this);
        error_sweetalert.setOnClickListener(this);
        delete_confirmation_sweetalert.setOnClickListener(this);
        custom_sweetalert.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        CustomLogs.e("Id is: " + id);
        CustomLogs.d("Id is: " + id);
        switch (id) {
            case R.id.loading_sweetalert: {
                showLoadingSweetalert();
                break;
            }
            case R.id.basic_sweetalert: {
                showBasicSweetalert();
                break;
            }
            case R.id.error_sweetalert: {
                showErrorSweetalert();
                break;
            }
            case R.id.delete_confirmation_sweetalert: {
                showDeleteSweetalert();
                break;
            }
            case R.id.custom_sweetalert: {
                showCustomSweetalert();
                break;
            }
        }
    }

    private void showLoadingSweetalert() {
        Sweetalert pDialog = new Sweetalert(this, Sweetalert.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismissWithAnimation();
            }
        }, 2000);
    }

    private void showBasicSweetalert() {
        new Sweetalert(this)
                .setTitleText("Here's a message!")
                .show();
    }

    private void showErrorSweetalert() {
        new Sweetalert(this, Sweetalert.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Something went wrong!")
                .show();
    }

    private void showDeleteSweetalert() {
        new Sweetalert(this, Sweetalert.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new Sweetalert.OnSweetClickListener() {
                    @Override
                    public void onClick(Sweetalert sDialog) {
                        sDialog.setTitleText("Loading").setContentText("Please wait...").changeAlertType(Sweetalert.PROGRESS_TYPE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                sDialog
                                        .setTitleText("Deleted!")
                                        .setContentText("Your imaginary file has been deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(Sweetalert.SUCCESS_TYPE);
                            }
                        }, 2000);
                    }
                })
                .show();
    }

    private void showCustomSweetalert() {
        Sweetalert.DARK_STYLE = true;
        new Sweetalert(this, Sweetalert.ERROR_TYPE)
                .setTitleText("<font color='red'>Red</font> title")
                .setContentText("Big <font color='green'>green </font><b><i> bold</i></b>")
                .setContentTextSize(21)
                .setStrokeWidth(2)
                .show();
        Sweetalert.DARK_STYLE = false;
    }
}