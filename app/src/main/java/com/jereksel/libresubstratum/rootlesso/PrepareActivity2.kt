package com.jereksel.libresubstratum.rootlesso

import android.app.Activity
import android.os.Bundle
import java.io.File

class PrepareActivity2 : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("A")

        val dataLocation = File(dataDir, "nativebridge")

        dataLocation.mkdirs()

        //I really, REALLY don't like it, but I'm afraid there isn't another way that isn't overcomplicated
        val bridge2File = File(dataLocation, "bridge2")
//        val bridgeFileLoc = bridgeFile.absolutePath

//        if (!bridgeFile.exists()) {
//            bridgeFile.createNewFile()
//        }

//        dataDir.setExecutable(true, false)
//
//        dataLocation.setExecutable(true, false)
//        dataLocation.setWritable(true, false)
//        dataLocation.setReadable(true, false)
//
//        bridgeFile.setExecutable(true, false)
//        bridgeFile.setReadable(true, false)

        "chmod -R 775 $dataLocation".execute()
        "chmod 777 ${bridge2File.absolutePath}".execute()

        //Prepare directories for "service"
//
//        println("A")
//
//        val dataLocation = File(dataDir, "nativebridge")
//
//        dataLocation.mkdirs()
//
//        //I really, REALLY don't like it, but I'm afraid there isn't another way that isn't overcomplicated
//        val bridgeFile = File(dataLocation, "bridge1")
//        val bridgeFileLoc = bridgeFile.absolutePath
//
//        if (!bridgeFile.exists()) {
//            bridgeFile.createNewFile()
//        }
//
//        dataDir.setExecutable(true, false)
//
//        dataLocation.setExecutable(true, false)
////        dataLocation.setWritable(true, false)
////        dataLocation.setReadable(true, false)
//
//        bridgeFile.setExecutable(true, false)
//        bridgeFile.setReadable(true, false)
//
//        "chmod 775 $bridgeFile".execute()
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