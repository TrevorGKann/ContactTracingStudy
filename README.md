# ContactTracingStudy

## Overview
We are looking to conduct a study where we collect data to better measure the efficacy of contact tracing algorithms, and we need your help!

If you're interested, you can sign up [here](https://forms.gle/KyJ7SJerBSGg8eyC7)!
Free food will be provided as well as a chance to win a $50 gift card, some studies have games as well.
You may register for multiple study sessions (you will need to fill out a consent form for each one).

If you've already signed up, you can install our custom app via [this link](https://github.com/TrevorGKann/ContactTracingStudy/raw/main/CoCoT_App.apk), and follow the [installation steps here](app_install.md).

## More Information

<details>
<summary><b>Background and Study Summary</b></summary>

COVID-19 is an airborne-transmitted virus that has already claimed over 6 million lives globally.
However, it is not the first pandemic that humanity has faced and may not be the last. 
Historically, contact tracing was used to combat the spread of ilnesses: by determining the contacts of infected individuals and informing those who may be at risk of their potential exposure, many infected people can self-isolate before they show symptoms or become infectious. 
This was previously done manually by trained healthcare professionals but this makes the process resource intensive to scale.
The rapid development of the COVID-19 pandemic and its fast spread made manual contact tracing less effective on slowing the spread of the virus than previous infectious diseases. 

Some researchers have proposed a solution to this problem though, in the form of automated contact tracing, whereby the prevalence of cellphones in certain communities is leveraged to help determine contacts of those who are infected. 
Many developments have been made in the field of automated contact tracing, such as making it much more privacy preserving, battery efficient, and a distributed protocol, but much work still needs to be done. 
Specifically, the current application used in the USA - Exposure Notification - relies off bluetooth low energy (BLE) signals between devices, which is a notoriously noisy indicator of distances. 
Furthermore, few datasets exist of these BLE signals between devices and none between many people, where contact tracing would be most effective. 
Obtaining this data is critical to studying and accurately benchmarking contact tracing algorithms. 
The current best dataset available was made by the NIST's [Too Close for Too Long (TC4TL) competition](https://tc4tlchallenge.nist.gov/), where pairs of people collected BLE data between one another.
This was a useful step but now data is needed between multiple people to determine the effects of other people or phones in the same room on BLE signals. 

The purpose of this study is to collect such a dataset for our own contact tracing algorithm studies as well as for other researchers to evaluate their algorithms. 
</details>

<details>
<summary><b>What Happens at the Study?</b></summary>

During the study, you will active our custom app to begin collecting sensor data from your phone.
This sensor data is later used to estimate your distance apart from other people in the room.
You'll also have a lanyard with a localizing sensor to tell us your exact distance apart from other people.

Depending on which study(ies) you participate in, we'll ask you to perform some activities (see <i>What are the Study Sessions?</i>).
Once the activities are complete, we'll collect the data from your phones as well as return the lanyards then raffle off some gift cards.
</details>

<details>
<summary><b>What are the Study Sessions?</b></summary>

We will be hosting three (3) studies for you to participate in, you may participate in as many as you'd like.
The three sessions we are host are:

* Indoor Structured
    * In this study, you will be assigned sequential locations in a room. When directed, you will navigate to each location then remain there for 60 seconds to collect data. Once the data has been collected, we will direct you towards the next location.
* Indoor Unstructured
    * We will host an indoor social event and encourage mingling with others and moving around the room. To help with this, we will host an ice breaker.
* Outdoor Unstructured
    * We will host an outdoor social event and provide lawn games (Bocce, Croquet, and Cornhole) to be played.

At each study, you will be outfitted with some sensing hardware to collect data about how you move, your location relative to the environment, and your location relative to others. We will also provide food and a chance to win $50 gift cards at each study.
</details>


<details>
<summary><b>What Kind of Information We Collect</b></summary>

Our app collects data from the following information from your phone and its sensors:

* phone model
* accelerometer
* gyroscope
* activity
* heading
* magnetometer
* compas
* Bluetooth received signal strength (only to other phones running our app)

This information is stored locally on your device until you complete the send-off form, at which point it sends it to me anonymously.
We do not believe the information contained from these sensors can be used to infer sensitive information about you and your name is not linked to your data.
</details>


<details>
<summary><b>How We Handle Your Information</b></summary>

The data you provide us is stored locally on a machine at CMU in a locked area. 
The machine it will be retained on is locked with a password and on an encrypted disk. 

We will eventually release the data as a public dataset for future research. 
</details>

<details>
<summary><b>More Questions & Contact</b></summary>

Feel free to reach out to me at < tkann \[at\] cmu \[dot\] edu > for any more questions about the study.
</details>


<details>
<summary><b></b></summary>


</details>
