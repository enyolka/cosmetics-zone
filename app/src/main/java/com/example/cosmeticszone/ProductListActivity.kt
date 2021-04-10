package com.example.cosmeticszone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    internal var listData: Array<Pair<String, String>> = emptyArray()

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
        queue = Volley.newRequestQueue(this)

        val url = "http://makeup-api.herokuapp.com/api/v1/products.json?product_type=$productType"

        val currenciesListRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                    response ->
                loadData(response)
                adapter.dataSet = listData
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                println("Error")
            }
        )
        queue.add(currenciesListRequest)
    }

    fun loadData(response: JSONArray?) {
        response?.let {
            val respCount = response.length()
            val tmpData = arrayOfNulls<Pair<String, String>>(respCount)

            for (i in 0 until respCount) {
                val productName = response.getJSONObject(i).getString("name")
                val productImage = response.getJSONObject(i).getString("image_link")
                val currencyObject = Pair(productName, productImage)//CurrencyDetails(currencyCode, currencyRate, flag, table, rise)

                tmpData[i] = currencyObject
            }

            this.listData += tmpData as Array<Pair<String, String>>
        }
    }
}