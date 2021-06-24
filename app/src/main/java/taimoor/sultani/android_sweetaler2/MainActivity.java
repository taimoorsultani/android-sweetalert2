package taimoor.sultani.android_sweetaler2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import taimoor.sultani.sweetalert2.Constants;
import taimoor.sultani.sweetalert2.Sweetalert;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] btnIds = {
                R.id.basic_test, R.id.styled_text_and_stroke, R.id.basic_test_without_buttons, R.id.under_text_test,
                R.id.error_text_test, R.id.success_text_test, R.id.warning_confirm_test, R.id.warning_cancel_test,
                R.id.custom_img_test, R.id.progress_dialog, R.id.neutral_btn_test, R.id.disabled_btn_test,
                R.id.custom_view_test, R.id.custom_btn_colors_test
        };
        for (Integer id : btnIds) {
            findViewById(id).setOnClickListener(this);
            findViewById(id).setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
        }

        CheckBox dark_style = findViewById(R.id.dark_style);
        dark_style.setOnCheckedChangeListener((buttonView, isChecked) -> Sweetalert.DARK_STYLE = isChecked);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progress_dialog:
                final Sweetalert pDialog = new Sweetalert(this, Sweetalert.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                new CountDownTimer(800 * 7, 800) {
                    public void onTick(long millisUntilFinished) {
                        // you can change the progress bar color by ProgressHelper every 800 millis
                        i++;
                        switch (i) {
                            case 0:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                break;
                            case 2:
                            case 6:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                break;
                        }
                    }

                    public void onFinish() {
                        i = -1;
                        pDialog.setTitleText("Success!")
                                .showConfirmButton(true)
                                .setConfirmText("OK")
                                .changeAlertType(Sweetalert.SUCCESS_TYPE);
                    }
                }.start();
                break;

            case R.id.basic_test:
                Sweetalert sd = new Sweetalert(this);
                sd.setCancelable(true);
                sd.setCanceledOnTouchOutside(true);
                sd.setContentText("Here's a message with buttons");
                sd.showConfirmButton(true);
                sd.setConfirmText("Okay");
                sd.showCancelButton(true);
                sd.setCancelText("Close");
                sd.show();
                break;

            case R.id.basic_test_without_buttons:
                Sweetalert sd2 = new Sweetalert(this);
                sd2.setCancelable(true);
                sd2.setCanceledOnTouchOutside(true);
                sd2.setContentText("Here's a message");
                sd2.showConfirmButton(false);
                sd2.showCancelButton(false);
                sd2.show();
                break;

            case R.id.under_text_test:
                new Sweetalert(this)
                        .setTitleText("Title")
                        .setContentText("It's pretty, isn't it?")
                        .showConfirmButton(true)
                        .setConfirmText("Okay")
                        .showCancelButton(true)
                        .setCancelText("Close")
                        .show();
                break;

            case R.id.styled_text_and_stroke:
                new Sweetalert(this)
                        .setTitleText("<font color='red'>Red</font> title")
                        .setTitleTextSize(32)
                        .setContentText("Big <font color='green'>green </font><b><i> bold</i></b>")
                        .setContentTextSize(21)
                        .setStrokeWidth(2)
                        .show();
                break;

            case R.id.error_text_test:
                new Sweetalert(this, Sweetalert.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .showConfirmButton(true)
                        .setConfirmText("Okay")
                        .showCancelButton(true)
                        .setCancelText("Close")
                        .show();
                break;

            case R.id.success_text_test:
                new Sweetalert(this, Sweetalert.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText("You clicked the button!")
                        .showConfirmButton(true)
                        .setConfirmText("Okay")
                        .showCancelButton(true)
                        .setCancelText("Close")
                        .show();
                break;

            case R.id.warning_confirm_test:
                new Sweetalert(this, Sweetalert.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .showConfirmButton(true)
                        .setConfirmText("NO")
                        .showCancelButton(true)
                        .setCancelButton("Yes, delete it!", sweetAlertDialog -> {
                            // reuse previous dialog instance
                            sweetAlertDialog
                                    .setTitleText("Loading")
                                    .setContentText("Please wait...")
                                    .showCancelButton(false)
                                    .showConfirmButton(false)
                                    .changeAlertType(Sweetalert.PROGRESS_TYPE);
                            new Handler(Looper.myLooper()).postDelayed(() -> sweetAlertDialog
                                    .setTitleText("Deleted!")
                                    .setContentText("Your imaginary file has been deleted!")
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(null)
                                    .setConfirmText("OKAY")
                                    .setCancelText("CLOSE")
                                    .showCancelButton(true)
                                    .showConfirmButton(true)
                                    .changeAlertType(Sweetalert.SUCCESS_TYPE), 3000);
                        })
                        .show();
                break;

            case R.id.warning_cancel_test:
                new Sweetalert(this, Sweetalert.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setCancelText("No, cancel pls!")
                        .setConfirmText("Yes, delete it!")
                        .showCancelButton(true)
                        .showConfirmButton(true)
                        .setCancelClickListener(sDialog -> {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("Cancelled!")
                                    .setContentText("Your imaginary file is safe :)")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(Sweetalert.ERROR_TYPE);
                        })
                        .setConfirmClickListener(sDialog -> sDialog.setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(Sweetalert.SUCCESS_TYPE))
                        .show();
                break;

            case R.id.custom_img_test:
                new Sweetalert(this, Sweetalert.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Sweet!")
                        .setContentText("Here's a custom image.")
                        .setCustomImage(R.drawable.custom_img)
                        .show();
                break;

            case R.id.neutral_btn_test:
                new Sweetalert(this, Sweetalert.NORMAL_TYPE)
                        .setTitleText("Title")
                        .setContentText("Three buttons dialog")
                        .showConfirmButton(true)
                        .showCancelButton(true)
                        .setConfirmText("Confirm")
                        .setCancelText("Cancel")
                        .setNeutralText("Neutral")
                        .show();
                break;

            case R.id.disabled_btn_test:
                final Sweetalert disabledBtnDialog = new Sweetalert(this, Sweetalert.NORMAL_TYPE)
                        .setTitleText("Title")
                        .setContentText("Disabled button dialog")
                        .setConfirmText("OK")
                        .setCancelText("Cancel")
                        .showCancelButton(true)
                        .showConfirmButton(true)
                        .setNeutralText("Neutral");
                disabledBtnDialog.setOnShowListener(dialog -> disabledBtnDialog.getButton(Sweetalert.BUTTON_CONFIRM).setEnabled(false));
                disabledBtnDialog.show();
                break;

            case R.id.custom_view_test:
                final EditText editText = new EditText(this);
                final CheckBox checkBox = new CheckBox(this);
                editText.setText("Some edit text");
                checkBox.setChecked(true);
                checkBox.setText("Some checkbox");

                if (Sweetalert.DARK_STYLE) {
                    editText.setTextColor(Color.WHITE);
                    checkBox.setTextColor(Color.WHITE);
                }

                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editText);
                linearLayout.addView(checkBox);

                Sweetalert dialog = new Sweetalert(this, Sweetalert.NORMAL_TYPE)
                        .setTitleText("Custom view");

                dialog.setCustomView(linearLayout);
                dialog.show();
                break;

            case R.id.custom_btn_colors_test:
                new Sweetalert(this, Sweetalert.NORMAL_TYPE)
                        .setTitleText("Custom view")
                        .showCancelButton(true)
                        .showConfirmButton(true)
                        .setCancelButton("red", null)
                        .setCancelButtonBackgroundColor(Color.RED)
                        .setNeutralButton("cyan", null)
                        .setNeutralButtonBackgroundColor(Color.CYAN)
                        .setConfirmButton("blue", null)
                        .setConfirmButtonBackgroundColor(Color.BLUE)
                        .show();
                break;

        }
    }
}