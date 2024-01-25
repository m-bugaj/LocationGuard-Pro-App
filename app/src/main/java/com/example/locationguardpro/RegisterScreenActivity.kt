package com.example.locationguardpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.locationguardpro.model.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RegisterScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
        val numberTextBox = findViewById<EditText>(R.id.name_text_box)
        val passwordTextBox = findViewById<EditText>(R.id.password_text_box)
        val emailTextBox = findViewById<EditText>(R.id.email_text_box)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val addButton = findViewById<Button>(R.id.add_button)
        val adminCheckBox = findViewById<CheckBox>(R.id.admin_check_box)
        val helpButton = findViewById<ImageButton>(R.id.help_button)

        val myApplication = application as MyApplication
        val appDatabase = myApplication.appDatabase


        addButton.setOnClickListener{
            lifecycleScope.launch {
                val username = numberTextBox.text.toString()
                val email = emailTextBox.text.toString()
                val password = passwordTextBox.text.toString()
                val isAdmin = adminCheckBox.isChecked

                // Pobierz dostęp do UserDao z AppDatabase
                val userDao = appDatabase.userDao()

                // Sprawdź, czy użytkownik istnieje w bazie danych
                val user = userDao.getUserByUsername(username)

                if(user == null){
                    val newUser = User(username = username, email = email, password = password, isAdmin = isAdmin)
                    runBlocking {
                        appDatabase.userDao().insertUser(newUser)
                    }
                    showToast("Added user")
                    numberTextBox.text.clear()
                    emailTextBox.text.clear()
                    passwordTextBox.text.clear()

                }
                else{
                    showToast("User already exists")
                }




            }

        }

        backButton.setOnClickListener{
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        helpButton.setOnClickListener {
            val intent = Intent(this, HelpScreenActivity::class.java)
            intent.putExtra("isFromHomeScreen", 1)
            startActivity(intent)
            finish()
            overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, android.R.anim.fade_in)
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}