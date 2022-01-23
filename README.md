# Ticker
[![](https://jitpack.io/v/kshitijskumar/Ticker.svg)](https://jitpack.io/#kshitijskumar/Ticker)

A simple spinner time picker library 

<img src="https://github.com/kshitijskumar/Ticker/blob/main/Time%20Picker%20cover.jpg" height=500> &nbsp;&nbsp;

## Adding dependencies: 
if your gradle version is 7.0 or above :
Add this to your settings.gradle (Project level):
```kotlin
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		....
		maven { url 'https://jitpack.io' }
	}
}
```
If your gradle version is below 7.0 : Add this to your build.gradle (Project level):
```kotlin
allprojects {
	repositories {
		....
		maven { url 'https://jitpack.io' }
	}
}
```
Enable viewBinding in app/build.gradle:
```
buildFeatures {
        viewBinding true
}
```
Add the dependencies in your app/build.gradle:
```kotlin
dependencies {
    ....
    implementation 'com.github.kshitijskumar:Ticker:1.0.0'
}
```

## How to use the library
You can use the ticker component like:
```
<com.example.ticker.core.ui.Ticker
        android:id="@+id/time_picker"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:hoursFormat="hours24"
        app:shouldAmSelected="false"
        app:minutesInterval="1"/>
```
<img src="https://github.com/kshitijskumar/Ticker/blob/main/ticker%20demo%20gif.gif" height=400> &nbsp;&nbsp;

To get the currently selected time: 
```
val timeSelected = binding.timePicker.getCurrentlySelectedTime()
```

To set initial time for the picker:
```
binding.timePicker.setInitialSelectedTime("10:40 Am")
```

