package com.example.cosmeticszone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    internal lateinit var signInButton: Button
    internal lateinit var username: EditText
    internal lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInButton = findViewById(R.id.signinButton)
        username = findViewById(R.id.usernameEditText)
        password = findViewById(R.id.passwordEditText)

        signInButton.setOnClickListener {
//                if (username.text.isNotEmpty() && password.text.isNotEmpty()) {
                    val intent = Intent(this, ProductTypeActivity::class.java)
                this.startActivity(intent)
//            }
        }

    }

}