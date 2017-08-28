package com.jereksel.libresubstratum.rootlesso

import android.content.om.IOverlayManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clz2 = Class.forName("com.android.server.LocalServices")
        val field = clz2.getDeclaredField("sLocalServiceObjects")
        field.isAccessible = true
        val a = field.get(null)

        println(a)

        val clz = Class.forName("android.os.ServiceManager")
        val method = clz.getDeclaredMethod("getService", String::class.java)
//        val overlay = IOverlayManager.Stub.asInterface(method.invoke(null, "overlay") as IBinder)
//        val overlay = getSystemService("overlay") as IOverlayManager

//        println(overlay.getAllOverlays(0))
        finish()
    }
}
