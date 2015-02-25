### FACE SWITCH

![Screenshot](https://raw.githubusercontent.com/Jason-NIU/Face-switch/master/screenshots/app.jpg)
Development environment:
Eclipse Luna, JDK 8u40,Beyond Reality Face SDK, Tobii Eye tracker PCEye Go, Win7

Running Requirement: JDK 8u40 version, you can download it from here https://jdk8.java.net/download.html

## Overview
The face switch is an application that transforms predefined facial gestures to specific keyboard keystrokes. The program is intended to help computer users with limited mobility from the neck down but who have a good level of control of facial muscles.
The software is intended to be used in combination with the Tobii PCEye Go eye tracker (http://www.tobii.com/en/assistive-technology/global/products/hardware/pceye/) and Tobii's accessibility software for computer controlled through gaze: Tobii Gaze Interaction Software 2.6.2. (http://www.tobii.com/en/assistive-technology/global/products/hardware/pceye/support-downloads/)
The program uses the Beyond Reality Face Tracker SDK (https://www.beyond-reality-face.com/ ) to track facial landmark features (lips, eyebrows, face contour etc.) from which it infers face gestures invariant to head position.


The user can select how many gestures the software monitors from 1 to 4 by selecting the check boxes on the left of each gesture name. However, as the number of gestures to be recognized increases, the number of false positives detection during computer interaction increases. We have noted differences among people, with different users getting different degrees of performance for different gestures.

The user can map each gesture to a specific keystroke. By clicking on the drop-down menu of the list of options associated to each gesture, the user can select common keys such as page down or F12 (default switch used by the Tobii Gaze Interaction Software). For customize situations, any keyboard key can be pressed since the drop-down menu is editable.

The user can adjust the sensitivity of the program by moving the slide below the drop-down menu. Values closer to 0 make gestures detection more likely but it also increments the false detection rate.

If the face tracker loses accurate tracking of facial landmarks, the user should exit and reenter the field of view of the camera for restarting the face tracking engine.

If performance degrades over time, the user should press the calibration button to restart the distance thresholds between landmark points used for gesture detection.

If the user wishes to pause the program, there is a corresponding button to do so.

Mappings and sensitivity values can be reset with the corresponding button.

A user can save a specific set of gesture mapping to keystrokes by pressing the button save. In a later session, clicking the button load restores the active gestures, mappings and sensitivity values previously saved.

For facial gestures are permitted:

-Opening mouth: the gesture is completed by opening and closing the mouth.
![Opened mouth](https://raw.githubusercontent.com/Jason-NIU/Face-switch/master/screenshots/open.png)

![Closed mouth](https://raw.githubusercontent.com/Jason-NIU/Face-switch/master/screenshots/normal.png)

-Raising eyebrows: the gesture is completed by raising the eyebrows and dropping them afterwards.
![Raising eyebrows](https://raw.githubusercontent.com/Jason-NIU/Face-switch/master/screenshots/rising%20eyebrow.png)

-Smile: the gesture is completed by generating a simple smile with the lips.
![smile](https://raw.githubusercontent.com/Jason-NIU/Face-switch/master/screenshots/smile.png)


-Snarling: the gesture is completed by making a nose twitch.

![Snarling](https://raw.githubusercontent.com/Jason-NIU/Face-switch/master/screenshots/nose.png)

Below you can find a video that illustrates the performance of the system.

### Video Demonstration

Demo 1 for typing test           
[![DEMO 1](http://img.youtube.com/vi/DXKa6hj2uFg/0.jpg)](http://youtu.be/DXKa6hj2uFg)

http://youtu.be/DXKa6hj2uFg 

Demo 2 for browsing webpages   

[![DEMO 2](http://img.youtube.com/vi/0vOrGOliwR8/0.jpg)](http://youtu.be/0vOrGOliwR8)

http://youtu.be/0vOrGOliwR8

## How to use it

For users, there are four simple steps:
1. Users need download the JDK8u40 from https://jdk8.java.net/download.html, and install it
2. Download the compiled version of our application, it's located in the fold called Compiled version.
3. After users finished the prior two steps, all they need is run the start.bat in the fold, which will execute all relevant programes for users
4. Enjoy it.
