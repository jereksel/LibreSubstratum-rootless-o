package com.jereksel.libresubstratum.rootlesso

import android.content.om.IOverlayManager
import android.os.IBinder
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jereksel.libresubstratum.bridgecommon.OverlayInfo
import com.jereksel.libresubstratum.rootlesso.protocol.*
import java.io.File
import java.nio.file.*
import java.util.concurrent.TimeUnit

object MainClass {

    @JvmStatic
    fun main(args: Array<String>) {

        try {

            val clz = Class.forName("android.os.ServiceManager")
            val method = clz.getDeclaredMethod("getService", String::class.java)
            val overlay = IOverlayManager.Stub.asInterface(method.invoke(null, "overlay") as IBinder)

            println(overlay.getAllOverlays(0))

            println("Preparing files")

            val dir = File("/data/data/com.jereksel.libresubstratum.rootlesso/nativebridge")

            val bridge1 = File(dir, "bridge1")
            val bridge1Path = dir.toPath()
            val bridge2 = File(dir, "bridge2")

//            val bridge1inStream = bridge1.inputStream()

//        FileSystems.getDefault().newWatchService().poll()

//            val reader = bridge1inStream.bufferedReader()

            val watch = bridge1Path.fileSystem.newWatchService()
            bridge1Path.register(watch, StandardWatchEventKinds.ENTRY_MODIFY)

            while (true) {

                println("Waiting for events")

                val key = watch.take()

                var l = ""

//                key.pollEvents().forEach { println(it.context()) }

                if (key != null && key.pollEvents().find { it.context().toString() == "bridge1" } != null) {
//                    key.pollEvents().forEach { println(it.context()) }

//                    do {
//
//                    }

//                    var lineN : String?
//
//                    do {
//                        lineN = reader.readLine()
//                    } while (lineN.isNullOrEmpty())
//
//                    val line = lineN!!

                    do {
                        Thread.sleep(100)
                    } while (bridge1.readText().isEmpty())

                    val line = bridge1.readText()

                    Log.d("RootlessOMSServiceCLZ", line)

//                    val line = reader.readLine()

//                    bridge2.writeText("")

//                    if (line != null) {
//                        println("Line: $line")
//                        key.reset()
//                        l = line
//
//                    }

//                    val line = l

                    println("Deserialing: $line")

                    val message = jacksonObjectMapper().readValue(line, Message::class.java)

                    when (message) {
                        is InstallPackage -> {
                            installPackage(message)
                            bridge2.writeText(jacksonObjectMapper().writeValueAsString(Success()))
                        }
                        is EnableOverlays -> {
                            overlay.setEnabled(message.overlayId, message.enable, 0)
                            bridge2.writeText(jacksonObjectMapper().writeValueAsString(Success()))
                        }
                        is OverlayInfoRequest -> {
                            val o = overlay.getOverlayInfo(message.appId, 0)
                            val overlayInfo = OverlayInfo(message.appId, o.isEnabled)
                            bridge2.writeText(jacksonObjectMapper().writeValueAsString(OverlayInfoResult(overlayInfo)))
                        }
                        is OverlaysForTargetRequest -> {
                            val os = (overlay.getOverlayInfosForTarget(message.appId, 0) as List<android.content.om.OverlayInfo>).map { OverlayInfo(it.packageName, it.isEnabled) }
                            bridge2.writeText(jacksonObjectMapper().writeValueAsString(OverlaysForTargetResult(os)))
                        }
                        else -> throw RuntimeException("Unknown message type: ${message::class.java}")
                    }

//                    bridge1.writeText("")

                    key.reset()

                } else {
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
            println(e.message)
        }
    }

    private fun installPackage(message: InstallPackage) {

        val location = message.location

        try {
            "pm install -r $location".execute()
        } catch (e: InvalidInvocationException) {
            println(e.message)
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