package com.example.cosmeticszone

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


class ProductListActivity : AppCompatActivity() {
    private lateinit var queue: RequestQueue
    internal lateinit var productsList: RecyclerView
    internal lateinit var adapter: ProductListAdapter
    internal lateinit var belovedButton: ImageView
    internal lateinit var productType: String
    internal lateinit var info: TextView
    internal var listData: Array<ProductDetails> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        queue = Volley.newRequestQueue(this)

        productsList = findViewById(R.id.productListRecycler)
        belovedButton = findViewById(R.id.belovedButton)
        info = findViewById((R.id.infoTextView))

        adapter = ProductListAdapter(listData, this)
        productsList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        productsList.adapter = adapter

        belovedButton.setOnClickListener() {
            val intent = Intent(this, BelovedProductsActivity::class.java)
            this.startActivity(intent)
        }

        productType = intent.getStringExtra("productType") ?: ""
        makeRequest(productType)
    }

    fun makeRequest(productType: String) {
        //queue = Volley.newRequestQueue(this)
        val url = "http://makeup-api.herokuapp.com/api/v1/products.json?product_type=$productType"

        val productListRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                    response ->
                loadData(response)
                adapter.dataSet = listData
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                println(it)
                println("Error")
            }
        )
        productListRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(productListRequest)
    }

    fun loadData(response: JSONArray?) {
        response?.let {
            val respCount = response.length()
            val tmpData = arrayOfNulls<ProductDetails>(respCount)

            for (i in 0 until respCount) {
                val apiID = response.getJSONObject(i).getInt("id")
                val productName = response.getJSONObject(i).getString("name")
                val brandName = response.getJSONObject(i).getString("brand")
                val price = response.getJSONObject(i).getString("price")
                val productImage = response.getJSONObject(i).getString("image_link")

                val productObject = ProductDetails(id=0, apiID = apiID, name = productName, brand = brandName, price=price, imageLink = productImage, type = this.productType)//, description = description, rate = rate, product_link = product_link, website_link = website_link)

                tmpData[i] = productObject
            }

//            this.listData += tmpData as Array<Triple<String, String, String>>
            this.listData += tmpData as Array<ProductDetails>
        }
    }
}