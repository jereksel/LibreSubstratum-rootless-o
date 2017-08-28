#!/usr/bin/env sh

APPPATH=$(pm path com.jereksel.libresubstratum.rootlesso)

CLASSPATH="${APPPATH}:/system/framework/framework-res.apk" /system/bin/app_process32 /system/bin com.jereksel.libresubstratum.rootlesso.MainClass

#adb shell chgrp -R shell $DATAPATH
#
#adb shell chmod 770 -R $DATAPATH