package com.example.cosmeticszone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
    internal lateinit var productType: String
    internal lateinit var info: TextView
    internal lateinit var filterView: ImageView
    internal lateinit var sortSpinner: Spinner
    internal lateinit var nothingLoaded : TextView

    internal var listData: Array<ProductDetails> = emptyArray()
    internal var listBrands: Array<FilterDetails> = emptyArray()
    internal var listCategories: MutableList<String> = ArrayList()
    internal var listCategory: Array<FilterDetails> = emptyArray()
    internal var listTags: MutableList<String> = ArrayList()
    internal var listTag: Array<FilterDetails> = emptyArray()
    internal var priceFrom: String = "0"
    internal var priceTo: String = "0"
    internal var ratingFrom: String = "0"
    internal var ratingTo: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        queue = Volley.newRequestQueue(this)

        productsList = findViewById(R.id.productListRecycler)
        info = findViewById((R.id.infoTextView))
        nothingLoaded = findViewById(R.id.nothingLoadedView)
        nothingLoaded.text = "No data to display"
        sortSpinner = findViewById(R.id.sortSpinner)
        sortSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayOf(" - ",  "price  ↑", "price  ↓", "name  ↑", "name  ↓", "brand  ↑", "brand  ↓"))

        filterView = findViewById(R.id.goFilterProducts)

        adapter = ProductListAdapter(listData, this)
        productsList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        productsList.adapter = adapter

        productType = intent.getStringExtra("productType") ?: ""
        makeRequest(false)

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                if (sortSpinner.selectedItem == "name  ↑")
                    adapter.dataSet =
                            listData.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.name }))
                                    .toTypedArray()
                if (sortSpinner.selectedItem == "name  ↓")
                    adapter.dataSet =
                            listData.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.name })).asReversed()
                                    .toTypedArray()
                else if (sortSpinner.selectedItem == "brand  ↑")
                    adapter.dataSet =
                            listData.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.brand }))
                                    .toTypedArray()
                else if (sortSpinner.selectedItem == "brand  ↓")
                    adapter.dataSet =
                            listData.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.brand })).asReversed()
                                    .toTypedArray()
                else if (sortSpinner.selectedItem == "price  ↑")
                    adapter.dataSet =
                            listData.sortedBy { when(it.price) {
                                "null" -> 0.0
                                else -> it.price?.toDouble()
                            }}.toTypedArray()
                else if (sortSpinner.selectedItem == "price  ↓")
                    adapter.dataSet =
                            listData.sortedBy { when(it.price) {
                                "null" -> 0.0
                                else -> it.price?.toDouble()
                            }}.asReversed().toTypedArray()
                else
                    adapter.dataSet = listData

                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

    }


    private fun makeRequest(filter: Boolean) {
        var filterPostfix = "product_type=$productType"

        if(listTag.any { it -> it.choose }){
            filterPostfix += "&product_tags="
            listTag.forEach { it -> if(it.choose) filterPostfix+="${it.name}," }
            filterPostfix = filterPostfix.substring(0, filterPostfix.length - 1)
        }
        if(priceFrom!="null" && (priceFrom.matches("[1-9]+\\d*\\.?\\d*$".toRegex()) || priceFrom.matches("0\\.?[1-9]\\d*".toRegex()))){
            filterPostfix += "&price_greater_than=${priceFrom}"
        }
        if(priceTo!="null" && (priceTo.matches("[1-9]+\\d*\\.?\\d*$".toRegex()) || priceTo.matches("0\\.?[1-9]\\d*".toRegex()))){
            filterPostfix += "&price_less_than=${priceTo}"
        }
        if(ratingFrom!="null" && (ratingFrom.matches("[1-9]+\\d*\\.?\\d*$".toRegex()) || ratingFrom.matches("0\\.?[1-9]\\d*".toRegex()))){
            filterPostfix += "&rating_greater_than=${ratingFrom}"
        }
        if(ratingTo!="null" && (ratingTo.matches("[1-9]+\\d*\\.?\\d*$".toRegex()) || ratingTo.matches("0\\.?[1-9]\\d*".toRegex()))){
            filterPostfix += "&rating_less_than=${ratingTo}"
        }

        val url = "http://makeup-api.herokuapp.com/api/v1/products.json?$filterPostfix"

        val productListRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                Response.Listener {
                    response ->
                    loadData(response, filter)
                    adapter.dataSet = listData
                    adapter.notifyDataSetChanged()
                    nothingLoaded.isVisible = listData.isEmpty()
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


    fun loadData(response: JSONArray?, filter: Boolean) {
        response?.let {
            val respCount = response.length()
            val tmpData = arrayOfNulls<ProductDetails>(respCount)
            var brandsOn = true
            var categoryOn = true
            var inCategory: MutableList<Int> = ArrayList()
            var inBrand: MutableList<Int> = ArrayList()

            if(filter){
                if(listBrands.all { it -> !it.choose }){
                    brandsOn = false
                }
                if(listCategory.all { it -> !it.choose }){
                    categoryOn = false
                }
            }

            for (i in 0 until respCount) {
                val apiID = response.getJSONObject(i).getInt("id")
                val productName = response.getJSONObject(i).getString("name").replace("\\s+".toRegex(), " ");
                val brandName = response.getJSONObject(i).getString("brand")
                val price = response.getJSONObject(i).getString("price")
                val productImage = response.getJSONObject(i).getString("image_link")

                val productObject = ProductDetails(id=0, apiID = apiID, name = productName, brand = brandName, price=price, imageLink = productImage, type = this.productType)//, description = description, rate = rate, product_link = product_link, website_link = website_link)

                tmpData[i] = productObject

                val category = response.getJSONObject(i).getString("category")

                if(!filter){
                    if(!category.isNullOrEmpty() && category != "null" && !listCategories.contains(category)){
                        listCategories.add(category)
                    }
                    val tags = response.getJSONObject(i).getJSONArray("tag_list")
                    val tagArray = Array(tags.length()){
                        tags.getString(it)
                    }
                    tagArray.forEach { item -> if(!listTags.contains(item) && !item.isNullOrEmpty()) listTags.add(item) }

                } else {
                    if(brandsOn && listBrands.any{it -> it.choose && it.name == brandName }){
                        inBrand.add(apiID)
                    }
                    if(categoryOn && listCategory.any{it -> it.choose && it.name == category }){
                        inCategory.add(apiID)
                    }
                }
            }
            if(filter){
                var tmpFiltered: Array<ProductDetails?> = tmpData
                if(brandsOn){
                    tmpFiltered = tmpData.filter { it -> it!!.apiID in inBrand }.toTypedArray()
                }
                if(categoryOn){
                    tmpFiltered = tmpFiltered.filter { it -> it!!.apiID in inCategory }.toTypedArray()
                }
                this.listData = tmpFiltered as Array<ProductDetails>
            }

            if(!filter){
                this.listData += tmpData as Array<ProductDetails>
                chooseFilters()
            }
        }
    }


    private fun chooseFilters() {
        val brandsList = listData.map { it.brand }.distinct()
        val brands = arrayOfNulls<FilterDetails>(brandsList.size)

        for (i in 0 until brandsList.size) {
            val brand =  FilterDetails("brand", brandsList[i], false)
            brands[i] = brand
        }
        this.listBrands = brands as Array<FilterDetails>

        val categories = arrayOfNulls<FilterDetails>(listCategories.size)
        for (i in 0 until listCategories.size) {
            val category =  FilterDetails("category", listCategories[i], false)
            categories[i] = category
        }
        this.listCategory = categories as Array<FilterDetails>

        val tags = arrayOfNulls<FilterDetails>(listTags.size)
        for (i in 0 until listTags.size) {
            val tag =  FilterDetails("tag", listTags[i], false)
            tags[i] = tag
        }
        this.listTag = tags as Array<FilterDetails>

        filterView.setOnClickListener{
            val intent = Intent(this, FilterActivity::class.java).apply {
                putExtra("listBrand", listBrands)
                putExtra("listCategory", listCategory)
                putExtra("listTags", listTag)
                putExtra("priceFrom", priceFrom)
                putExtra("priceTo", priceTo)
                putExtra("ratingFrom", ratingFrom)
                putExtra("ratingTo", ratingTo)
            }
            this.startActivityForResult(intent, 1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 || requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                this.listBrands = data!!.getSerializableExtra("filterBrand") as Array<FilterDetails>
                this.listCategory = data!!.getSerializableExtra("filterCategories") as Array<FilterDetails>
                this.listTag = data!!.getSerializableExtra("filterTags") as Array<FilterDetails>
                this.priceFrom = data!!.getStringExtra("priceFrom").toString()
                this.priceTo = data!!.getStringExtra("priceTo").toString()
                this.ratingFrom = data!!.getStringExtra("ratingFrom").toString()
                this.ratingTo = data!!.getStringExtra("ratingTo").toString()

                makeRequest(true)
            }
        }
    }


    fun goToDetails(productID: Int, productBrand: String, productType: String, productName: String) {
        val intent = Intent(this, ProductInfoActivity::class.java).apply {
            putExtra("productApiId", productID)
            putExtra("productBrand", productBrand)
            putExtra("productType", productType)
            putExtra("productName", productName)

            putExtra("listBrand", listBrands)
            putExtra("listCategory", listCategory)
            putExtra("listTags", listTag)
            putExtra("priceFrom", priceFrom)
            putExtra("priceTo", priceTo)
            putExtra("ratingFrom", ratingFrom)
            putExtra("ratingTo", ratingTo)
        }
        startActivityForResult(intent,2)
    }
}