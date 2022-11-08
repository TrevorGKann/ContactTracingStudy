# ContactTracingStudy

## Overview
I am looking to conduct a study to collect data to better measure the efficacy of contact tracing algorithms, and I need your help! 

If you're interested, you can sign up [here]()!

If you've already signed up, you can install our custom app via [this link](https://github.com/TrevorGKann/ContactTracingStudy/raw/main/CoCoT_App.apk), and follow the [installation steps here](app_install.md).

## More Information

<details>
<summary>Background and Study Summary</summary>
COVID-19 is an airborne ilness that has already claimed over 6 million lives globally. 
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
<summary>What Happens at the Study?</summary>
</details>


<details>
<summary>What Kind of Information We Collect</summary>
Our app collects data from the following information from your phone and its sensors:

* phone model
* accelerometer
* gyroscope
* activity
* heading
* magnetometer
* compas
* Bluetooth signal strength to other phones running the app

This information is stored locally on your device until you complete the send-off form, at which point it sends it to me anonymously.
We do not believe the information contained from these sensors can be used to infer sensitive information about you and your name is not linked to your data.
</details>


<details>
<summary>How We Handle Your Information</summary>
The data you provide us is stored locally on a machine at CMU in a locked area. 
The machine it will be retained on is locked with a password and on an encrypted disk. 

We will eventually release the data as a public dataset for future research. 
</details>

<details>
<summary></summary>
</details>
