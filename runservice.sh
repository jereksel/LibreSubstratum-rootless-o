#!/usr/bin/env bash

APPPATH=$(adb shell pm path com.jereksel.libresubstratum.rootlesso)

adb shell CLASSPATH="${APPPATH}:/system/framework/framework-res.apk" /system/bin/app_process32 /system/bin com.jereksel.libresubstratum.rootlesso.MainClass

#adb shell chgrp -R shell $DATAPATH
#
#adb shell chmod 770 -R $DATAPATH