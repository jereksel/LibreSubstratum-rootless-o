package com.jereksel.libresubstratum.rootlesso

import android.content.om.IOverlayManager
import android.os.IBinder
import java.io.File
import java.nio.file.*
import java.util.concurrent.TimeUnit

object MainClass {

    @JvmStatic
    fun main(args: Array<String>) {

        try {

            val clz = Class.forName("android.os.ServiceManager")
            val method = clz.getDeclaredMethod("getService", String::class.java)
            val overlay =  IOverlayManager.Stub.asInterface(method.invoke(null, "overlay") as IBinder)

            println(overlay.getAllOverlays(0))

            println("Preparing files")

            val dir = File("/data/data/com.jereksel.libresubstratum.rootlesso/nativebridge")

            val bridge1 = File(dir, "bridge1")
            val bridge1Path = dir.toPath()
            val bridge2 = File(dir, "bridge2")

            val bridge1inStream = bridge1.inputStream()

//        FileSystems.getDefault().newWatchService().poll()

            val reader = bridge1inStream.bufferedReader()

            val watch = bridge1Path.fileSystem.newWatchService()
            bridge1Path.register(watch, StandardWatchEventKinds.ENTRY_MODIFY)

            while (true) {

                println("Waiting for events")

                val key = watch.take()

//                key.pollEvents().forEach { println(it.context()) }

                if (key != null && key.pollEvents().find { it.context().toString() == "bridge1" } != null) {
//                    key.pollEvents().forEach { println(it.context()) }
                    val line = reader.readLine()

//                    bridge2.writeText("")

                    if (line != null) {
                        println("Line: $line")
                    }

                    key.reset()
                }
            }

//        while (true) {
//            val line = reader.readLine()
//            if (line.isNullOrEmpty()) {
//                println("Empty line")
//                Thread.sleep(100)
//            } else {
//                println("Read line: $line")
//            }
//        }

//        try {
//            println("Hello world")
//            val clz = Class.forName("android.os.ServiceManager")
//            val method = clz.methods.first { it.name == "listServices" }
////            val method = clz.getMethod("listServices", Array<String>::class.java)
//
//            val arr = method.invoke(null) as Array<String>
//
//            println(arr)
//            println(arr.joinToString())
//
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    fun String.execute() {
        val proc = ProcessBuilder().command(this.split(" ")).start()
        proc.waitFor()

        val status = proc.exitValue()
        val output = proc.inputStream.bufferedReader().use { it.readText() }
        val error = proc.errorStream.bufferedReader().use { it.readText() }

        if (status != 0) {
            throw InvalidInvocationException(error)
        }

    }

}