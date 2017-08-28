package com.jereksel.libresubstratum.rootlesso

import android.app.Activity
import android.os.Bundle
import java.io.File

class PrepareActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Prepare directories for "service"

        println("A")

        val dataLocation = File(dataDir, "nativebridge")
        val logLocation = File(dataDir, "log")

        logLocation.mkdirs()
        dataLocation.mkdirs()

        //I really, REALLY don't like it, but I'm afraid there isn't another way that isn't overcomplicated
        val bridgeFile = File(dataLocation, "bridge1")
        val bridgeFileLoc = bridgeFile.absolutePath
        val bridge2File = File(dataLocation, "bridge2")

        if (!bridgeFile.exists()) {
            bridgeFile.createNewFile()
        }

        if (!bridge2File.exists()) {
            bridge2File.createNewFile()
        }

        dataDir.setExecutable(true, false)

        dataLocation.setExecutable(true, false)
        dataLocation.setWritable(true, false)
        dataLocation.setReadable(true, false)

        bridgeFile.setExecutable(true, false)
        bridgeFile.setReadable(true, false)

        bridge2File.setExecutable(true, false)
        bridge2File.setReadable(true, false)
        bridge2File.setWritable(true, false)

        "chmod -R 777 $dataLocation".execute()
        "chmod -R 777 $logLocation".execute()

        "chmod 775 $bridgeFile".execute()
        "chmod 777 ${bridge2File.absolutePath}".execute()
//        bridgeFile.setWritable(true, false)
//        bridgeFile.setReadable(true, false)

//        "chmod -R +x $dataDir".execute()
//
//        "chmod -R 777 $bridgeFileLoc".execute()

//        "chgrp -R shell $dataLocation".execute()


        finish()
    }

    private fun String.execute() {
        ProcessBuilder(*this.split(" ").toTypedArray()).start().waitFor()
    }
}

