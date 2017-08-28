package com.jereksel.libresubstratum.rootlesso

import android.app.Service
import android.content.Intent
import android.util.Log
import com.jereksel.libresubstratum.rootlesso.IRootlessOMSService.Stub

class RootlessOMSService: Service() {

    val lock = java.lang.Object()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent) = mBinder

    private val mBinder = object: Stub() {
        override fun installPackages(apk: String) {
            synchronized(lock) {

                Log.d("installPackages", apk)

            }
        }
    }

}