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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_type)

        val productTypes = arrayOf<Pair<String, Int>>(Pair("Blush", R.drawable.blush), Pair("Bronzer", R.drawable.bronzer), Pair("Eyebrow", R.drawable.eyebrow),
                Pair("Eyeliner", R.drawable.eyeliner), Pair("Eyeshadow", R.drawable.eyeshadow), Pair("Foundation", R.drawable.foundation), Pair("Lip liner", R.drawable.lipliner),
                Pair("Lipstick", R.drawable.lipstick), Pair("Mascara", R.drawable.mascara), Pair("Nail Polish", R.drawable.nailpolish))
        queue = Volley.newRequestQueue(this)

        productsTypeList = findViewById(R.id.productTypesRecycler)

        adapter = ProductTypeAdapter(productTypes, this)
        productsTypeList.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        productsTypeList.adapter = adapter

    }
}