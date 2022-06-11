# Crypto-Tracker-App
Crypto Tracker App for set alert cryptocurrencies.

### Detail
Android App named Crypto Tracker App which is responsible for Crypto Currency rate changes. 
The main functionalities of this app is like this:
- Home Page currency rates list for Bitcoin, Ethereum, Ripple
- When user clicks any of this coin list item route to set min and max value alert screen. In this screen user can set max and min value for this coin rate. You need to work in background and check this coin rate. If coin rate is more than max rate or less than min rate alert user with notification. And also save this rate value to show in history page
- In set min and max rate page there should be a button which navigates to this coin history page. In which you need to show last currency rates that you have taken at background.


<img src="https://github.com/Muzafferus/Crypto-Tracker-App/blob/master/images/screenshot_home.jpeg" width="216" height="468"> <img src="https://github.com/Muzafferus/Crypto-Tracker-App/blob/master/images/screenshot_seach.jpeg" width="216" height="468"> <img src="https://github.com/Muzafferus/Crypto-Tracker-App/blob/master/images/screenshot_detail.jpeg" width="216" height="468"> <img src="https://github.com/Muzafferus/Crypto-Tracker-App/blob/master/images/screenshot_alert_history.jpeg" width="216" height="468">

### Api information
- Use this API for coin prices - https://api.coingecko.com/api/v3/simple/price
- For API documentation  - https://www.coingecko.com/ru/api/documentation

### Android Framework

In this folder, you will find projects using the following set of Libraries. 

- MVVM
- Kotlin Coroutines
- Jetpack Navigation
- Jetpack WorkManager
- ViewBinding
- Network: Retrofit2
- Restful API - Apps
- Image loader: Coil
- DI: Hilt
- DataBase: Room
- DB connection: Flow
- UI data connection: LiveData


License
--------

       Copyright 2020 Muzaffar Pashazada
       
       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
       
       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
