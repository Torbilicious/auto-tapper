package de.binder_net.tapper_test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.stericson.RootShell.exceptions.RootDeniedException
import com.stericson.RootShell.execution.Command
import com.stericson.RootTools.RootTools
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeoutException


class ScreenshotCapturer {
    fun take(): Bitmap {

        //screencap /sdcard/tmp/tmp.png

        val sdcardPath = Environment.getExternalStorageDirectory().path

        rootRun("screencap $sdcardPath/tmp/tmp.png")

        val bitmap = BitmapFactory.decodeFile(File("$sdcardPath/tmp/tmp.png").absolutePath)
        return bitmap
    }

    fun rootRun(vararg commands: String) {

        val command = Command(0, *commands)
        try {
            RootTools.getShell(true).add(command)
        } catch (ex: IOException) {
            ex.printStackTrace()
        } catch (ex: RootDeniedException) {
            ex.printStackTrace()
        } catch (ex: TimeoutException) {
            ex.printStackTrace()
        }
    }
}
