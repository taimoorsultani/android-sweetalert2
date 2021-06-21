# Sweet Alert Dialog

SweetAlert for Android, a beautiful and clever alert dialog
Inspired by JavaScript [SweetAlert](https://sweetalert.js.org/guides/)

### A very big thanks to

- [pedant](https://github.com/pedant)
- [F0RIS](https://github.com/F0RIS)
- [nico2sh](https://github.com/nico2sh)
- [croccio](https://github.com/croccio)

**The original project belongs to [pedant](https://github.com/pedant), can be viewed here [pedant/sweet-alert-dialog](https://github.com/pedant/sweet-alert-dialog).**

**[F0RIS](https://github.com/F0RIS) also has added major contribution, which can be view here [F0RIS/sweet-alert-dialog](https://github.com/F0RIS/sweet-alert-dialog).**

#### This is the most advanced and contemporary fork of the apparently dead project

**Added:**

- Ability to set custom view
- More convenient interface to bind listeners (like in AlertDialog)
- Third neutral button with own listener, colors, methods and etc.
- Ability to disable buttons
- Ability to set buttons stroke width
- Dark style of dialogs
- Ability to make dialogs without buttons
- Support of HTML tags
- Ability to set text size
- Ability to set buttons color

Some screenshots of the new features:

<img src="https://cloud.githubusercontent.com/assets/10178982/24260517/c6f72da6-0ffc-11e7-9a16-67fea4010a34.jpg" width="30%"/>

<img src="https://user-images.githubusercontent.com/10178982/59605653-eee87d80-9117-11e9-9421-b116536c9388.png" width="30%"/>

#### Known issues:

- [ ] Bug with buttons height if custom view too big and need scrollview
- [ ] Buttons can handle only one line strings

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Sweet%20Alert%20Dialog-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1065)

## Preview

![image](https://github.com/taimoorsultani/android-sweetalert2/raw/main/preview.gif)

## Setup

The simplest way to use Sweetalert is to add the library as aar dependency to your build.

**Maven**

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    <dependency>
        <groupId>com.github.taimoorsultani</groupId>
        <artifactId>android-sweetalert2</artifactId>
        <version>1.1.0</version>
    </dependency>

**Gradle**

    allprojects {
    	repositories {
    		maven { url 'https://jitpack.io' }
    	}
    }

    dependencies {
        implementation 'com.github.taimoorsultani:android-sweetalert2:1.1.0'
    }

## Usage

Show material progress

    Sweetalert pDialog = new Sweetalert(this, Sweetalert.PROGRESS_TYPE);
    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
    pDialog.setTitleText("Loading");
    pDialog.setCancelable(false);
    pDialog.show();

![image](https://github.com/taimoorsultani/android-sweetalert2/raw/main/play_progress.gif)

You can customize progress bar dynamically with materialish-progress methods via **Sweetalert.getProgressHelper()**:

- resetCount()
- isSpinning()
- spin()
- stopSpinning()
- getProgress()
- setProgress(float progress)
- setInstantProgress(float progress)
- getCircleRadius()
- setCircleRadius(int circleRadius)
- getBarWidth()
- setBarWidth(int barWidth)
- getBarColor()
- setBarColor(int barColor)
- getRimWidth()
- setRimWidth(int rimWidth)
- getRimColor()
- setRimColor(int rimColor)
- getSpinSpeed()
- setSpinSpeed(float spinSpeed)

thanks to the project [materialish-progress](https://github.com/pnikosis/materialish-progress) and [@croccio](https://github.com/croccio) participation.

more usages about progress, please see the sample.

A basic message：

    new Sweetalert(this)
        .setTitleText("Here's a message!")
        .show();

A title with a text under：

    new Sweetalert(this)
        .setTitleText("Here's a message!")
        .setContentText("It's pretty, isn't it?")
        .show();

A error message：

    new Sweetalert(this, Sweetalert.ERROR_TYPE)
        .setTitleText("Oops...")
        .setContentText("Something went wrong!")
        .show();

A warning message：

    new Sweetalert(this, Sweetalert.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmText("Yes,delete it!")
        .show();

A success message：

    new Sweetalert(this, Sweetalert.SUCCESS_TYPE)
        .setTitleText("Good job!")
        .setContentText("You clicked the button!")
        .show();

A message with a custom icon：

    new Sweetalert(this, Sweetalert.CUSTOM_IMAGE_TYPE)
        .setTitleText("Sweet!")
        .setContentText("Here's a custom image.")
        .setCustomImage(R.drawable.custom_img)
        .show();

A message with a custom view：

    final EditText editText = new EditText(this);
    new Sweetalert(this, Sweetalert.NORMAL_TYPE)
            .setTitleText("Custom view")
            .setConfirmText("Ok")
            .setCustomView(editText)
            .show();

Different ways to bind the listener to button：

    new Sweetalert(this, Sweetalert.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmText("Yes,delete it!")
        .setConfirmClickListener(new Sweetalert.OnSweetClickListener() {
            @Override
            public void onClick(Sweetalert sDialog) {
                sDialog.dismissWithAnimation();
            }
        })
        .setCancelButton("Cancel", new Sweetalert.OnSweetClickListener() {
            @Override
            public void onClick(Sweetalert sDialog) {
                sDialog.dismissWithAnimation();
            }
        })
        .show();

Disable button

    final Sweetalert disabledBtnDialog = new Sweetalert(this, Sweetalert.NORMAL_TYPE)
            .setTitleText("Title")
            .setContentText("Disabled button dialog")
            .setConfirmText("Confirm")
            .setCancelText("Cancel")

    disabledBtnDialog.setOnShowListener(new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            disabledBtnDialog.getButton(Sweetalert.BUTTON_CONFIRM).setEnabled(false);
        }
    });
    disabledBtnDialog.show();

**Change** the dialog style upon confirming：

    new Sweetalert(this, Sweetalert.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this file!")
        .setConfirmText("Yes,delete it!")
        .setConfirmClickListener(new Sweetalert.OnSweetClickListener() {
            @Override
            public void onClick(Sweetalert sDialog) {
                sDialog
                    .setTitleText("Deleted!")
                    .setContentText("Your imaginary file has been deleted!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    .changeAlertType(Sweetalert.SUCCESS_TYPE);
            }
        })
        .show();

[more android tech shares: pedant.cn](http://www.pedant.cn)
