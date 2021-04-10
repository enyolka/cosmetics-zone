package com.example.cosmeticszone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class ProductTypeActivity : AppCompatActivity() {
    private lateinit var queue: RequestQueue
    internal lateinit var productsTypeList: RecyclerView
    internal lateinit var adapter: ProductTypeAdapter
    internal lateinit var belovedButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_type)

        val productTypes = arrayOf<Pair<String, String>>(Pair("Blush", ""), Pair("Bronzer", ""), Pair("Eyebrow", ""), Pair("Eyeliner", ""), Pair("Eyeshadow", ""),
            Pair("Foundation", ""), Pair("Lip liner", ""), Pair("Lipstick", ""), Pair("Mascara", ""), Pair("Nail Polish", ""))
        queue = Volley.newRequestQueue(this)

        productsTypeList = findViewById(R.id.productTypesRecycler)
        belovedButton = findViewById(R.id.belovedButton)

        adapter = ProductTypeAdapter(productTypes, this)
        productsTypeList.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        productsTypeList.adapter = adapter

        belovedButton.setOnClickListener() {
            val intent = Intent(this, BelovedProductsActivity::class.java)
            this.startActivity(intent)
        }
    }
}