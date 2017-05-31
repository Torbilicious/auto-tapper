package de.binder_net.tapper_test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class FullscreenActivity @Throws(IOException::class)
constructor() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        val mContentView = findViewById(R.id.fullscreen_content)

        val actionBar = supportActionBar
        actionBar?.hide()

        mContentView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {

                val text = String.format(Locale.ENGLISH, "X: %f | Y: %f", event.x, event.y)
                val duration = Toast.LENGTH_SHORT

                Toast.makeText(applicationContext, text, duration).show()
            }

            true
        }


        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                val startTime = System.currentTimeMillis()

                try {
                    val process = Runtime.getRuntime().exec("su")
                    val os = DataOutputStream(process.outputStream)

                    val minX = 0.0f
                    val maxX = 1080.0f
                    val minY = 0.0f
                    val maxY = 1920.0f

                    val rand = Random()

                    val x = rand.nextFloat() * (maxX - minX) + minX
                    val y = rand.nextFloat() * (maxY - minY) + minY

                    val cmd = String.format(Locale.ENGLISH, "/system/bin/input tap %f %f\n", x, y)
                    os.writeBytes(cmd)
                    os.writeBytes("exit\n")
                    os.flush()
                    os.close()
                    process.waitFor()

                    Log.d("My-Tag", cmd)
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                val endTime = System.currentTimeMillis()
                val totalTime = endTime - startTime
                Log.d("My-Tag", String.format(Locale.ENGLISH, "Delta: %d", totalTime))
            }
        }, 1000, 5000)
    }


}
