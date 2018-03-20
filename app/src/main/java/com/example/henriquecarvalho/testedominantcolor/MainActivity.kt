package com.example.henriquecarvalho.testedominantcolor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.widget.TextView
import android.support.v7.widget.Toolbar
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    private var vibrantView: TextView? = null
    private var vibrantLightView: TextView? = null
    private var vibrantDarkView: TextView? = null
    private var mutedView: TextView? = null
    private var mutedLightView: TextView? = null
    private var mutedDarkView: TextView? = null
    private var myToolBar: Toolbar? = null
    private var imageView: ImageView? = null
    private var statusBar: Window? = window

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myToolBar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolBar)
        title = "Title Test"

        initViews()
        paintTextBackground(R.drawable.wallpaper)
    }

    private fun paintTextBackground(drawable: Int) {
        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, drawable)

        Palette.from(bitmap).generate { palette ->
            val defaultValue = 0x000000
            val vibrantLight: Int = palette.getLightVibrantColor(defaultValue)
            val vibrant: Int = palette.getVibrantColor(defaultValue)
            val vibrantDark: Int = palette.getDarkVibrantColor(defaultValue)
            val muted: Int = palette.getMutedColor(defaultValue)
            val mutedLight: Int = palette.getLightMutedColor(defaultValue)
            val mutedDark: Int = palette.getDarkMutedColor(defaultValue)

            vibrantView?.setBackgroundColor(vibrant)
            vibrantLightView?.setBackgroundColor(vibrantLight)
            vibrantDarkView?.setBackgroundColor(vibrantDark)
            mutedView?.setBackgroundColor(muted)
            mutedLightView?.setBackgroundColor(mutedLight)
            mutedDarkView?.setBackgroundColor(mutedDark)

            setToolbarColor(bitmap)
        }

    }

    private fun initViews() {
        vibrantView = findViewById(R.id.vibrantView)
        vibrantLightView = findViewById(R.id.vibrantLightView)
        vibrantDarkView = findViewById(R.id.vibrantDarkView)
        mutedView = findViewById(R.id.mutedView)
        mutedLightView = findViewById(R.id.mutedLightView)
        mutedDarkView = findViewById(R.id.mutedDarkView)

        imageView = findViewById(R.id.imageWallpaper)
        imageView?.tag = "1"
        imageView?.setOnClickListener {
            if (imageView?.tag == "1") {
                imageView!!.setImageResource(R.drawable.wallpaper2)
                imageView?.tag = "2"
                paintTextBackground(R.drawable.wallpaper2)
            } else {
                imageView!!.setImageResource(R.drawable.wallpaper)
                imageView?.tag = "1"
                paintTextBackground(R.drawable.wallpaper)
            }
        }
    }

    private fun setToolbarColor(bitmap: Bitmap) {
        // Generate the palette and get the vibrant swatch
        val vibrantSwatch = createPaletteSync(bitmap).vibrantSwatch

        // Set the toolbar background and text colors.
        // Fall back to default colors if the vibrant swatch is not available.
        with(findViewById<Toolbar>(R.id.my_toolbar)) {
            setBackgroundColor(vibrantSwatch?.rgb ?:
                    ContextCompat.getColor(context, R.color.colorPrimary))
            setTitleTextColor(vibrantSwatch?.titleTextColor ?:
                    ContextCompat.getColor(context, R.color.colorAccent))

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = vibrantSwatch?.rgb ?:
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }
    }

    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

}
