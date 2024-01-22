package com.example.locationguardpro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
        val passwordTextBox = findViewById<EditText>(R.id.password_text_box)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val helpButton = findViewById<ImageButton>(R.id.help_button)

        val myApplication = application as MyApplication
        val appDatabase = myApplication.appDatabase

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        if(sharedPreferences.getLong("USER_ID", -1)>-1){
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
                    sharedPreferences.edit().putBoolean("IS_ADMIN", user.isAdmin).apply()
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
            sharedPreferences.edit().remove("IS_ADMIN").apply()
        }

        backButton.setOnClickListener{
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        helpButton.setOnClickListener {
            val intent = Intent(this, HelpScreenActivity::class.java)
            intent.putExtra("isFromHomeScreen", 1)
            startActivity(intent)
            overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, android.R.anim.fade_in)
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}

