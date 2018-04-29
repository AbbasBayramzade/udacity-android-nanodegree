# Gradle for Android and Java

This project contains an app with multiple flavors that uses
multiple libraries and Google Cloud Endpoints. The finished app consist
of four modules. A Java library that provides jokes, a Google Cloud Endpoints
(GCE) project that serves those jokes, an Android Library containing an
activity for displaying jokes, and an Android app that fetches jokes from the
GCE module and passes them to the Android Library for display.


## Components

- [x] Project contains a Java library for supplying jokes
- [x] Project contains an Android library with an activity that displays jokes passed to it as intent extras.
- [x] Project contains a Google Cloud Endpoints module that supplies jokes from the Java library. Project loads jokes from GCE module via an async task.
- [x] Project contains connected tests to verify that the async task is indeed loading jokes.
- [x] Project contains paid/free flavors. The paid flavor has no ads, and no unnecessary dependencies.
- [x] Project contains signing configurations.

## Behavior

- [x] App retrieves jokes from Google Cloud Endpoints module and displays them via an Activity from the Android Library.


### Project Overview

Multi-project build looks like this. 

![screenshot](https://github.com/amatanat/udacity-android-nanodegree/blob/master/BuildItBigger/ph4.png)


### App Screenshots

See app screenshots [here](https://github.com/amatanat/udacity-android-nanodegree/tree/master/BuildItBigger/screenshots)
