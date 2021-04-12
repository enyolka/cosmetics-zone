package com.example.cosmeticszone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class ProductInfoActivity : AppCompatActivity() {
    private lateinit var queue: RequestQueue
    internal lateinit var productName: TextView
    internal lateinit var buttonBeloved: Button
    internal lateinit var product: ProductDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        queue = Volley.newRequestQueue(this)

        productName = findViewById(R.id.tvProductName)
        buttonBeloved = findViewById(R.id.buttonAddBeloved)

        productName.text = intent.getStringExtra("productName") ?: ""
        var productBrand = intent.getStringExtra("productBrand") ?: ""
        var productApiId = intent.getIntExtra("productApiId", 0)
        var productType = intent.getStringExtra("productType") ?: ""

        makeRequest(productType, productBrand, productApiId)
    }

    private fun addToBeloved() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (product.name.isNotEmpty()) {
            val status =
                databaseHandler.addProduct(product)
            if (status > -1) {
                Toast.makeText(applicationContext, "Zapisano do ulubionych", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Dodawanie do ulubionych nie powiodło się",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun makeRequest(productType: String, productBrand: String, productApiId: Int) {
        //queue = Volley.newRequestQueue(this)
        val url = "http://makeup-api.herokuapp.com/api/v1/products.json?brand=%s&product_type=%s".format(productBrand, productType)

        val productListRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                    response ->
                loadData(response, productApiId)
                buttonBeloved.setOnClickListener{view->
                    addToBeloved()
                }
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
    fun loadData(response: JSONArray?, productApiId: Int) {
        response?.let {
            val respCount = response.length()

            for (i in 0 until respCount) {
                val apiID = response.getJSONObject(i).getInt("id")
                val productName = response.getJSONObject(i).getString("name")
                val brandName = response.getJSONObject(i).getString("brand")
                val price = response.getJSONObject(i).getString("price")
                val productImage = response.getJSONObject(i).getString("image_link")
                val productType = response.getJSONObject(i).getString("product_type")
                if(apiID == productApiId){
                    product = ProductDetails(id=0, apiID = apiID, name = productName, brand = brandName, price=price, imageLink = productImage, type = productType)
                    return
                }
                if(i == respCount - 1)
                    product = ProductDetails(id=0, apiID = 0, name = "", brand = "", price="", imageLink = "", type = "")
                }
            }

        }

    }