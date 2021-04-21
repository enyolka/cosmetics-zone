package com.example.cosmeticszone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class BelovedProductsActivity : AppCompatActivity() {

    internal lateinit var productsList: RecyclerView
    internal lateinit var info: TextView
    internal lateinit var tvNoRecordsAvailable : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beloved_products)

        productsList = findViewById(R.id.belovedListRecycler)
        info = findViewById((R.id.infoTextView))
        tvNoRecordsAvailable = findViewById(R.id.tvNoRecordsAvailable)

        setupListofDataIntoRecyclerView()
    }
    private fun setupListofDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            productsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            productsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val belovedAdapter = BelovedProductsAdapter(this, getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            productsList.adapter = belovedAdapter
        } else {

            productsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<ProductDetails> {

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val prodList: ArrayList<ProductDetails> = databaseHandler.viewProducts()

        return prodList
    }

    fun deleteRecordAlertDialog(product: ProductDetails) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete from beloved")
        builder.setMessage("Are you sure you want to delete ${product.name} from list?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            val status = databaseHandler.deleteProducts(product)
            if (status > -1) {
                Toast.makeText(
                        this,
                        "Deleted product",
                        Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun goToDetails(productID: Int, productBrand: String, productType: String, productName: String) {
        val intent = Intent(this, ProductInfoActivity::class.java).apply {
            putExtra("productApiId", productID)
            putExtra("productBrand", productBrand)
            putExtra("productType", productType)
            putExtra("productName", productName)

            putExtra("listBrand", emptyArray<FilterDetails>())
            putExtra("listCategory", emptyArray<FilterDetails>())
            putExtra("listTags", emptyArray<FilterDetails>())
        }
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check that it is the SecondActivity with an OK result
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setupListofDataIntoRecyclerView()
            }
        }
    }
}