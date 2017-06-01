package de.binder_net.tapper_test

import android.annotation.SuppressLint
import android.graphics.RectF
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.stericson.RootShell.exceptions.RootDeniedException
import com.stericson.RootShell.execution.Command
import com.stericson.RootTools.RootTools
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity() {
    var running: Boolean = false
    var rootAvailable: Boolean = false
    val time: TimeProfiler = TimeProfiler()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val actionBar = supportActionBar
        actionBar?.hide()

        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mContentView: View = findViewById(R.id.fullscreen_content)

        if (RootTools.isRootAvailable()) {
            rootAvailable = RootTools.isAccessGiven()
        } else {

            rootAvailable = false
        }

        log("Root available: $rootAvailable")

        mContentView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {

                log("X: ${event.x} | Y: ${event.y}")

                log("Delta: ${time.end()}")
            }

            true
        }

        Timer().scheduleAtFixedRate(object : TimerTask() {

            var screen: RectF = RectF()
            var minX: Float = 0f
            var maxX: Float = 0f
            var minY: Float = 0f
            var maxY: Float = 0f
            var rand = Random()
            var x: Float = 0f
            var y: Float = 0f

            override fun run() {

                if (!running || !rootAvailable)
                    return

                time.start()

                screen = getViewDimensions(mContentView)

                minX = screen.left
                maxX = screen.right
                minY = screen.top
                maxY = screen.bottom

                rand = Random()

                x = rand.nextFloat() * (maxX - minX) + minX
                y = rand.nextFloat() * (maxY - minY) + minY

                rootRun("/system/bin/input tap $x $y\n")
            }
        }, 1000, 2500)
    }

    override fun onPause() {
        super.onPause()

        running = false
    }

    override fun onResume() {
        super.onResume()

        running = true
    }

    fun log(message: String) {

        Log.d("MainActivity", message)
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

    fun getViewDimensions(view: View): RectF {

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val location = intArrayOf(0, 0)
        view.getLocationOnScreen(location)

        return RectF(location[0].toFloat(), location[1].toFloat(), metrics.widthPixels.toFloat(), metrics.heightPixels.toFloat())
    }
}
