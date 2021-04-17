package com.example.cosmeticszone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.jsoup.Jsoup
import java.lang.Exception

class ProductInfoActivity : AppCompatActivity() {
    private lateinit var queue: RequestQueue
    internal lateinit var product: ProductDetails
    internal lateinit var buttonBeloved: ImageView
    internal lateinit var name: TextView
    internal lateinit var image: ImageView
    internal lateinit var brand: TextView
    internal lateinit var price: TextView
    internal lateinit var description: TextView
    internal lateinit var rate: TextView
    internal lateinit var link: Button
    internal lateinit var layout: Layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        queue = Volley.newRequestQueue(this)

        buttonBeloved = findViewById(R.id.buttonAddBeloved)
        //layout = setContentView(R.layout.linearLayout)
        name = findViewById(R.id.productNameView)
        brand = findViewById(R.id.brandNameView)
        image = findViewById(R.id.productImageView)
        price = findViewById(R.id.priceView)
        description = findViewById(R.id.descriptionView)
        description.movementMethod = ScrollingMovementMethod()
        rate = findViewById(R.id.rateView)
        link = findViewById(R.id.productLinkButton)

        name.text = intent.getStringExtra("productName") ?: ""
        var productBrand = intent.getStringExtra("productBrand") ?: ""
        var productApiId = intent.getIntExtra("productApiId", 0)
        var productType = intent.getStringExtra("productType") ?: ""

        makeRequest(productType, productBrand, productApiId)

    }

    private fun addToBeloved() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (product.name.isNotEmpty()) {
            if(databaseHandler.searchProduct(product.apiID) >= 0){
                val status =
                        databaseHandler.deleteProductsAPIID(product)
                if (status > -1) {
                    Toast.makeText(applicationContext, "Usunięto z ulubionych", Toast.LENGTH_LONG).show()
                    buttonBeloved.setImageResource(R.drawable.heart_empty)
                }
            }else{
                val status =
                        databaseHandler.addProduct(product)
                if (status > -1) {
                    Toast.makeText(applicationContext, "Zapisano do ulubionych", Toast.LENGTH_LONG).show()
                    buttonBeloved.setImageResource(R.drawable.heart_pink)
                }
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
                val brandName = response.getJSONObject(i).getString("brand")
                val productType = response.getJSONObject(i).getString("product_type")
                val productName = (response.getJSONObject(i).getString("name")).replace("\\s+".toRegex(), " ");
                val productPrice = response.getJSONObject(i).getString("price")
                var priceSign = response.getJSONObject(i).getString("price_sign")
                if (priceSign == "null") priceSign = "$"
                val productImage = response.getJSONObject(i).getString("image_link")
                val productLink = response.getJSONObject(i).getString("product_link")

                if(apiID == productApiId){
                    product = ProductDetails(id=0, apiID = apiID, name = productName, brand = brandName, price=productPrice, imageLink = productImage, type = productType)

                    description.text = Jsoup.parse(response.getJSONObject(i).getString("description").toString().repeat(1)).text()
                            ?: "no description"
                    brand.text = brandName
                    price.text = "$productPrice $priceSign"
                    Picasso.with(this).load(productImage).resize(0, image.getHeight()).into(image)
                    link.setOnClickListener {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(productLink)
                        startActivity(i)
                    }

                    try {
                       rate.text = response.getJSONObject(i).getDouble("rating").toString()
                   } catch (e: Exception) {
                       rate.text = "0.0"
                   }
                    initiateButtonBeloved()
                    return
                }
                if(i == respCount - 1)
                    product = ProductDetails(id=0, apiID = 0, name = "", brand = "", price="", imageLink = "", type = "")
                }
            }
        }

    private fun initiateButtonBeloved() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(databaseHandler.searchProduct(product.apiID) >= 0) {
            buttonBeloved.setImageResource(R.drawable.heart_pink)
        }
    }

}