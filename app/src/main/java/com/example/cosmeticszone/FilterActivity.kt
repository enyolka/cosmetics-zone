package com.example.cosmeticszone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
        priceFrom.hint = "from"
        priceTo.hint = "to"
        ratingFrom.hint = "from"
        ratingTo.hint = "to"

        listBrand = intent.getSerializableExtra("listBrand") as Array<FilterDetails>
        listCategories = intent.getSerializableExtra("listCategory") as Array<FilterDetails>
        listTags = intent.getSerializableExtra("listTags") as Array<FilterDetails>

//        if(intent.getStringExtra("priceFrom") != "0") {
//            priceFrom.setText(intent.getStringExtra("priceFrom").toString())
//        }
//        if(intent.getStringExtra("priceTo") != "0") {
//            priceFrom.setText( intent.getStringExtra("priceTo").toString())
//        }
//        if(intent.getStringExtra("ratingFrom") != "0") {
//            priceFrom.setText( intent.getStringExtra("ratingFrom").toString())
//        }
//        if(intent.getStringExtra("ratingTo") != "0") {
//            priceFrom.setText( intent.getStringExtra("ratingTo").toString())
//        }

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