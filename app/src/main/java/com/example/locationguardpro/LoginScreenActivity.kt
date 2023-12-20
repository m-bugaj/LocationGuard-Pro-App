package com.example.locationguardpro

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.locationguardpro.ui.theme.LocationGuardProTheme

class LoginScreenActivity :  AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        val loginButton = findViewById<Button>(R.id.login_button)
        val logoutButton = findViewById<Button>(R.id.logout_button)
        val numberTextBox = findViewById<EditText>(R.id.numberTextBox)
        val passwordTextBox = findViewById<EditText>(R.id.passwordTextBox)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        loginButton.setOnClickListener{
            numberTextBox.text.clear()
            passwordTextBox.text.clear()
            loginButton.background = getDrawable(R.drawable.start_button_aft)
            logoutButton.startAnimation(fadeIn)
            logoutButton.visibility = View.VISIBLE
        }

        logoutButton.setOnClickListener{
            logoutButton.startAnimation(fadeOut)
            logoutButton.visibility = View.GONE
            loginButton.background = getDrawable(R.drawable.start_button_bef)
        }

        backButton.setOnClickListener{
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }


    }

}

