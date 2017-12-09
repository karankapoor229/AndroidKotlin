package me.karankapoor.androidkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreenActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)
        var link = intent.getStringExtra("image_file")
        fun loadImage(imageView: ImageView, picture: String) {
            Picasso.with(imageView.context).load(picture).into(imageView)
        }
        loadImage(img_full, link)
    }
}
