# Your Days Are Numbered (App)

## Project Description 
Your favourite card game is back but in an App! Now you can carry the game around with you on your smartphone (Android only, sorry iOS) and play it wherever you are. The same features, same great art, now portable! 

See the original game [here](https://github.com/loheesong/Your_Days_Are_Numbered)!!

See the video demo of how to play [here](https://youtu.be/3tk1_8nWCKc?si=GdqbLdPEep-2ddT-)!

## Patch notes 

### 1.0.0 
First release of the game on Android. 

## Visuals 
In order to fit everything onto a phone screen, the UI had some minor adjustments. See the screenshots below. 

![flow chart](/readme_assets/flowchart.jpg)

## Rules 
Play phase:
- You are given cargo and a given number
- Play cards to achieve that number 
- Cards contain math operations
- Each turn you draw until you get 5 card
- Max hand size: 5
- End of turn: put remaining cards at the bottom of the deck 
- Lose: you lose when you run out of cards, not cargo
- You can concede if you feel like there is no way of winning
- The levels go on infinitely until lose or concede 

Buy phase (in between levels):
- Remaining cargo gets added to next level cargo 
- Use remaining cargo to buy different cards

## Installation
To play the game, make sure your Android phone is on developer mode. Download this repo and compile with Android Studio. Plug in your phone to download the app from the IDE. 

## Technologies Used 
The app was done entirely in Java. Tested using Pixel 5A AVD within Android Studio. 

Developed with: 
-   JDK:  `jbr-17`
-   Android SDK:  `Android 14 UpsideDownCake API Level 34`

## Future Work 
- Statistics (fewest moves, fastest time etc) 
- Release it on Google Play Store 

## Acknowledgement
The original game in Python and Tkinter was made by Daniel Lim, Jean Soh, Kaelen Tay, Mohammad Saif, Toh Siew Hean. 

Card art: [Daniel Lim](https://github.com/xdaniel-lim)

The Android app was done by me. 