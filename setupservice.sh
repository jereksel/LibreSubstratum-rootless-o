#!/usr/bin/env bash

APPPATH=$(adb shell pm path com.jereksel.libresubstratum.rootlesso)
DATAPATH="/data/data/com.jereksel.libresubstratum.rootlesso/nativebridge"

# Find a way to wait for finish()
adb shell am start-activity -n com.jereksel.libresubstratum.rootlesso/.PrepareActivity

sleep 5

BRIDGE2="$DATAPATH/bridge2"

adb shell rm $BRIDGE2

adb shell touch $BRIDGE2

adb shell chmod 775 $BRIDGE2

adb shell am start-activity -n com.jereksel.libresubstratum.rootlesso/.PrepareActivity2
