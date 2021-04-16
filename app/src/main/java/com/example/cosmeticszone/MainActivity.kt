package com.example.cosmeticszone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    internal lateinit var listButton: Button
    internal lateinit var belovedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listButton = findViewById(R.id.productListButton)
        belovedButton = findViewById(R.id.belovedProductsButton)

        listButton.setOnClickListener {
            val intent = Intent(this, ProductTypeActivity::class.java)
            this.startActivity(intent)
        }

        belovedButton.setOnClickListener() {
            val intent = Intent(this, BelovedProductsActivity::class.java)
            this.startActivity(intent)
        }


    }

}