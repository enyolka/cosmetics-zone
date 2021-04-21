package com.example.cosmeticszone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class FilterActivity : AppCompatActivity() {
    lateinit var filterButton: Button
    lateinit var brandRecyclerView: RecyclerView
    lateinit var categoryRecyclerView: RecyclerView
    lateinit var tagsRecyclerView: RecyclerView
    lateinit var priceFrom: EditText
    lateinit var priceTo: EditText
    lateinit var ratingFrom: EditText
    lateinit var ratingTo: EditText
    internal lateinit var adapterBrand: FilterActivityAdapter
    internal lateinit var adapterCategory: FilterActivityAdapter
    internal lateinit var adapterTags: FilterActivityAdapter
    internal var listBrand: Array<FilterDetails> = emptyArray()
    internal var listCategories: Array<FilterDetails> = emptyArray()
    internal var listTags: Array<FilterDetails> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        filterButton = findViewById(R.id.filterButton)
        brandRecyclerView = findViewById(R.id.brandRecyclerView)
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)
        tagsRecyclerView = findViewById(R.id.tagsRecyclerView)
        priceFrom = findViewById(R.id.priceFrom)
        priceTo = findViewById(R.id.priceTo)
        ratingFrom = findViewById(R.id.ratingFrom)
        ratingTo = findViewById(R.id.ratingTo)

        listBrand = intent.getSerializableExtra("listBrand") as Array<FilterDetails>
        listCategories = intent.getSerializableExtra("listCategory") as Array<FilterDetails>
        listTags = intent.getSerializableExtra("listTags") as Array<FilterDetails>

        var priceF = intent.getStringExtra("priceFrom").toString()
        var priceT = intent.getStringExtra("priceTo").toString()
        var ratingF = intent.getStringExtra("ratingFrom").toString()
        var ratingT = intent.getStringExtra("ratingTo").toString()

        if(priceF != "0" && !priceF.isNullOrEmpty()) {
            priceFrom.setText(priceF)
        } else {
            priceFrom.hint = "from"
        }
        if(priceT != "0" && !priceT.isNullOrEmpty()) {
            priceTo.setText( priceT)
        } else {
            priceTo.hint = "to"
        }
        if(ratingF != "0" && !ratingF.isNullOrEmpty()) {
            ratingFrom.setText( ratingF)
        } else {
            ratingFrom.hint = "from"
        }
        if(ratingT != "0" && !ratingT.isNullOrEmpty()) {
            ratingTo.setText( ratingT)
        } else {
            ratingTo.hint = "to"
        }

        adapterBrand = FilterActivityAdapter(listBrand, this)
        brandRecyclerView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        brandRecyclerView.adapter = adapterBrand

        adapterCategory = FilterActivityAdapter(listCategories, this)
        categoryRecyclerView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.adapter = adapterCategory

        adapterTags = FilterActivityAdapter(listTags, this)
        tagsRecyclerView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        tagsRecyclerView.adapter = adapterTags

        filterButton.setOnClickListener{ filterProducts()}
    }

    private fun filterProducts() {
        var priceFilterOk = true
        var ratingFilterOk = true
        if(!priceFrom.text.toString().isNullOrEmpty() && !priceTo.text.toString().isNullOrEmpty() && priceFrom.text.toString().toDouble() > priceTo.text.toString().toDouble()){
            priceFilterOk = false
        }
        if(!ratingFrom.text.toString().isNullOrEmpty() && !ratingTo.text.toString().isNullOrEmpty() && ratingFrom.text.toString().toDouble() > ratingTo.text.toString().toDouble()){
            ratingFilterOk = false
        }
        if(ratingFilterOk && priceFilterOk){
            val intent = Intent()
            intent.putExtra("filterBrand", listBrand)
            intent.putExtra("filterCategories", listCategories)
            intent.putExtra("filterTags", listTags)
            intent.putExtra("priceFrom", priceFrom.text.toString())
            intent.putExtra("priceTo", priceTo.text.toString())
            intent.putExtra("ratingFrom", ratingFrom.text.toString())
            intent.putExtra("ratingTo", ratingTo.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(applicationContext, "Wrong price or rating", Toast.LENGTH_LONG).show()
        }
    }

    fun changeChoice(product: FilterDetails, position: Int){
        if(product.type == "brand"){
            listBrand[position].choose = !product.choose
            adapterBrand.dataSet = listBrand
            adapterBrand.notifyDataSetChanged()
        }else if(product.type == "category"){
            listCategories[position].choose = !product.choose
            adapterCategory.dataSet = listCategories
            adapterCategory.notifyDataSetChanged()
        }
        else{
            listTags[position].choose = !product.choose
            adapterTags.dataSet = listTags
            adapterTags.notifyDataSetChanged()
        }
    }
}