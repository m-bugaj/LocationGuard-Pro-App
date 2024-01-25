package com.example.locationguardpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import com.example.locationguardpro.R.anim

class HelpScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_screen)

        val downArrow = findViewById<ImageButton>(R.id.down_arrow)
        val receivedValue = intent.getIntExtra("isFromHomeScreen", 0)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val noActionAnim = AnimationUtils.loadAnimation(this, anim.noaction)

        downArrow.setOnClickListener {
            val intent = if(receivedValue == 1) {
                Intent(this, HomeScreenActivity::class.java)
            }
            else {
                Intent(this, TrackingScreenActivity::class.java)
            }

            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.noaction, androidx.appcompat.R.anim.abc_slide_out_bottom)
        }
    }
}