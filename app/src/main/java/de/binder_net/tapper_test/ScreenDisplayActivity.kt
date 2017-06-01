package de.binder_net.tapper_test

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView

class ScreenDisplayActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_screen_display)

        val imageView: ImageView = findViewById(R.id.imageView) as ImageView

        val image = ScreenshotCapturer().take()
        imageView.setImageBitmap(image)
    }
}
