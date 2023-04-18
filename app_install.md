# App Instalation Guide

First, download the app from [this link](https://github.com/TrevorGKann/ContactTracingStudy/raw/main/CoCoT_App.apk). 
Then, either click the bubble that said it was downloaded or go to the "Files" app on your phone and click on the downloaded APK.
Your phone may ask you to give permissions to install apps downloaded from the internet, temporarily allow this (you should probably set it to off after you install our app).
Select yes, and install the app. 
When you run the app for the first time, it will ask for Bluetooth permissions, please say yes as the app will not function without this permission. 
Your app is installed and ready to go!

## Using the Capability Tester

The app also comes with a "Phone Capability Launcher" that will tell you if your android is new enough to run the data collection app. 
To use this, follow the installation instructions above, then use the secondary launcher provided with the app.
Make sure your Bluetooth is turned on and accept any permissions that the app requests.
Then press the button to see if your device is capable and the result will be shown on screen. 

This app only requests permissions to ensure the phone is capable of allowing them, but do note that this app and the main launcher share the same permissions. 
You can see why we request them below. 

## Using the App

We will walk you through this process during the study, but also explain it here.

At the start of the study, please launch the app and fill in the "Your participant ID number" form with the same number that is labeled to the front of your lanyard. 
Once you have done this, press the "START" button. 
Your device will now begin recording data.
Please *DO NOT LOCK YOUR DEVICE* during the study. 
You can put your phone in your pocket and the screen will automatically darken and prevent you from touching the app, but do not press the power button of your device (Android specifications are ambiguous in the locked state and some devices stop recording data). 
If the app is working, you should see several sensor values and other phone IDs populate the screen, if not let a staff member know. 
When the study has finished, you may press the "STOP!" button on the app. 

The app does not record sensitive information; it does record redouts from the following sensors: Gyroscope, accelerometer, heading, activity, magnetometer, compas, Bluetooth, & phone model (e.g. Google Pixel-3).

## Why We Require Permissions

Bluetooth: The goal of the study is to study how Bluetooth low energy (BLE) signals behave between phones in different environments and with different actions.
We require BLE for this reason.
Our app only emits BLE signals with a specific tag, and we only scan for BLE signals with the same tag, no other signals.
Our app does not connect to any other BLE devices, it is not aware of currently/previously connected devices, nor can it access those devices.

Location: certain android versions require the use of fine location services when scanning for other nearby Bluetooth packets. 
To ensure CoCoT is runnable on a variety of phones, we request location services.
Note that CoCoT does not utilize the GPS of your phone. 

Our app does not require any other permissions.
