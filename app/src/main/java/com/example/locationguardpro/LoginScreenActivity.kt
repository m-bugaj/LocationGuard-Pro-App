package com.example.locationguardpro

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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
import androidx.lifecycle.lifecycleScope
import com.example.locationguardpro.model.User
import com.example.locationguardpro.ui.theme.LocationGuardProTheme
import kotlinx.coroutines.launch

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

        val myApplication = application as MyApplication
        val appDatabase = myApplication.appDatabase

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        if(!sharedPreferences.getLong("USER_ID", -1).equals(-1)){
            loginButton.background = getDrawable(R.drawable.start_button_aft)
            logoutButton.visibility = View.VISIBLE
        }



        loginButton.setOnClickListener{
            lifecycleScope.launch {
                val username = numberTextBox.text.toString()
                val password = passwordTextBox.text.toString()
                // Pobierz dostęp do UserDao z AppDatabase
                val userDao = appDatabase.userDao()

                // Sprawdź, czy użytkownik istnieje w bazie danych
                val user = userDao.getUserByUsernameAndPassword(username, password)


                if(user != null){
                    showToast("Login successful")
                    sharedPreferences.edit().putLong("USER_ID", user.userId).apply()
                    numberTextBox.text.clear()
                    passwordTextBox.text.clear()
                    loginButton.background = getDrawable(R.drawable.start_button_aft)
                    logoutButton.startAnimation(fadeIn)
                    logoutButton.visibility = View.VISIBLE

                }
                else{
                    showToast("Incorrect login or password")
                }

            }

        }

        logoutButton.setOnClickListener{
            logoutButton.startAnimation(fadeOut)
            logoutButton.visibility = View.GONE
            loginButton.background = getDrawable(R.drawable.start_button_bef)

            // Pobierz referencję do SharedPreferences
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

            // Usuń wartość z SharedPreferences
            sharedPreferences.edit().remove("USER_ID").apply()
        }

        backButton.setOnClickListener{
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}

