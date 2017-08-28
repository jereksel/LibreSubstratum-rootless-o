package com.jereksel.libresubstratum.rootlesso

import android.app.Service
import android.content.Intent
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jereksel.libresubstratum.bridgecommon.ILibreSubstratumService
import com.jereksel.libresubstratum.bridgecommon.OverlayInfo
import com.jereksel.libresubstratum.rootlesso.protocol.*
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchService

class RootlessOMSService: Service() {

    companion object {
        val lock = java.lang.Object()
    }

//    val lock = java.lang.Object()

//    lateinit var lock: java.lang.Object

    lateinit var bridge1: File
    lateinit var bridge2: File
    lateinit var p: Path
    lateinit var watch: WatchService

    override fun onCreate() {
        super.onCreate()
        bridge1 = Paths.get(dataDir.absolutePath, "nativebridge", "bridge1").toFile()!!
        bridge2 = Paths.get(dataDir.absolutePath, "nativebridge", "bridge2").toFile()!!
        p = Paths.get(dataDir.absolutePath, "nativebridge")!!
        watch = p.fileSystem.newWatchService()!!
        p.register(watch, StandardWatchEventKinds.ENTRY_MODIFY)
//        lock = java.lang.Object()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int) = Service.START_STICKY

    override fun onBind(intent: Intent) = mBinder

    private val mBinder = object: ILibreSubstratumService.Stub() {
        override fun getOverlayInfo(overlayId: String): OverlayInfo = synchronized(lock) {
            bridge2.writeText("")
            Log.d("RootlessOMSService", "getOverlayInfo")
            bridge1.writeText(jacksonObjectMapper().writeValueAsString(OverlayInfoRequest(overlayId)))
            val ret = (getAnswer() as OverlayInfoResult).overlayInfo
            Log.d("RootlessOMSService", "Message read")
            ret
        }

        override fun getOverlaysForPackage(packageId: String): MutableList<OverlayInfo> = synchronized(lock) {
            bridge2.writeText("")
            Log.d("RootlessOMSService", "getOverlaysForPackage")
            bridge1.writeText(jacksonObjectMapper().writeValueAsString(OverlaysForTargetRequest(packageId)))
            val ret = (getAnswer() as OverlaysForTargetResult).overlays.toMutableList()
            Log.d("RootlessOMSService", "Message read")
            ret
        }

        override fun enableOverlay(overlayId: String) = synchronized(lock) {
            bridge2.writeText("")
            Log.d("RootlessOMSService", "enableOverlay")
            bridge1.writeText(jacksonObjectMapper().writeValueAsString(EnableOverlays(overlayId, true)))
            val answer = getAnswer()
            Log.d("RootlessOMSService", "Message read")
            when(answer) {
                is Success -> return@synchronized
                else -> throw RuntimeException("Unknown answer type: ${answer::class.java.canonicalName}")
            }
        }

        override fun disableOverlay(overlayId: String) = synchronized(lock) {
            bridge2.writeText("")
            Log.d("RootlessOMSService", "disableOverlay")
            bridge1.writeText(jacksonObjectMapper().writeValueAsString(EnableOverlays(overlayId, false)))
            val answer = getAnswer()
            Log.d("RootlessOMSService", "Message read")

            when(answer) {
                is Success -> return@synchronized
                else -> throw RuntimeException("Unknown answer type: ${answer::class.java.canonicalName}")
            }
        }

        override fun installPackage(paths: MutableList<String>) = synchronized(lock) {

            bridge2.writeText("")
            val pack = paths[0]
            val messageObject = InstallPackage(pack)
            val message = jacksonObjectMapper().writeValueAsString(messageObject)

            bridge1.writeText(message)

//            while(true) {
//
//                val key = watch.take()
//
//                if (key != null && key.pollEvents().find { it.context().toString() == "bridge2" } != null) {
//                    key.reset()
//                    break
//                } else {
//                    key.reset()
//                }
//            }


            Log.d("RootlessOMSService", "Message read")

//            val retMessage = bridge2.readText()

            val answer = getAnswer()

//            val answer = jacksonObjectMapper().readValue(retMessage, Message::class.java)

            when(answer) {
                is Success -> Unit
                is FailedInvocation -> throw RuntimeException(answer.message)
                else -> throw RuntimeException("Unknown answer type: ${answer::class.java.canonicalName}")
            }

        }

        override fun uninstallPackages(packages: MutableList<String>) = synchronized(lock) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun restartSystemUI() = Unit
    }

    private fun getAnswer(): Message {

        while (true) {

            val key = watch.take()

            if (key != null && key.pollEvents().find { it.context().toString() == "bridge2" } != null) {
                key.reset()
                break
            } else {
                key.reset()
            }
        }


        Log.d("RootlessOMSService", "Message read")

        do {
            Thread.sleep(10)
        } while (bridge2.readText().isEmpty())

//        Thread.sleep(10)

        val retMessage = bridge2.readText()

        bridge2.writeText("")

        Log.d("RootlessOMSService", "message: $retMessage")

//        val retMessage = "cat $bridge2".execute()

        return jacksonObjectMapper().readValue(retMessage, Message::class.java)
    }

    fun String.execute(): String {
        val proc = ProcessBuilder().command(this.split(" ")).start()
        proc.waitFor()

        val status = proc.exitValue()
        val output = proc.inputStream.bufferedReader().use { it.readText() }
        val error = proc.errorStream.bufferedReader().use { it.readText() }

        if (status != 0) {
            throw InvalidInvocationException(error)
        }

        return output
    }

}