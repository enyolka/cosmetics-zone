package com.example.cosmeticszone

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private val DATABASE_VERSION = 1
            private val DATABASE_NAME = "FavouriteProducts"

            private val TABLE_PRODUCTS = "ProductsTable"

            private val KEY_ID = "_id"
            private val KEY_APIID = "apiID"
            private val KEY_NAME = "name"
            private var KEY_TYPE = "type"
            private val KEY_BRAND = "brand"
            private var KEY_PRICE = "price"
            private var KEY_IMAGELINK = "imageLink"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            //creating table with fields
            val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_PRODUCTS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_APIID + " INTEGER," + KEY_NAME
                    + " TEXT," + KEY_TYPE + " TEXT," + KEY_BRAND + " TEXT," + KEY_PRICE + " TEXT,"
                    + KEY_IMAGELINK + " TEXT" + ")")
            db?.execSQL(CREATE_CONTACTS_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
            onCreate(db)
        }

        /**
         * Function to insert data
         */
        fun addProduct(product: ProductDetails): Long {
            val db = this.writableDatabase

            val contentValues = ContentValues()

            contentValues.put(KEY_APIID, product.apiID)
            contentValues.put(KEY_NAME, product.name)
            contentValues.put(KEY_TYPE, product.type)
            contentValues.put(KEY_BRAND, product.brand)
            contentValues.put(KEY_PRICE, product.price)
            contentValues.put(KEY_IMAGELINK, product.imageLink)

            val success = db.insert(TABLE_PRODUCTS, null, contentValues)


            db.close()
            return success
        }

        fun viewProducts(): ArrayList<ProductDetails> {

            val prodList: ArrayList<ProductDetails> = ArrayList<ProductDetails>()

            val selectQuery = "SELECT  * FROM $TABLE_PRODUCTS"

            val db = this.readableDatabase

            var cursor: Cursor? = null

            try {
                cursor = db.rawQuery(selectQuery, null)

            } catch (e: SQLiteException) {
                db.execSQL(selectQuery)
                return ArrayList()
            }

            var id: Int
//            var name: String
//            var email: String
            var apiID: Int
            var name: String
            var type: String
            var brand: String
            var price: String
            var imageLink: String

            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                    apiID = cursor.getInt(cursor.getColumnIndex(KEY_APIID))
                    name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                    type = cursor.getString(cursor.getColumnIndex(KEY_TYPE))
                    brand = cursor.getString(cursor.getColumnIndex(KEY_BRAND))
                    price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                    imageLink = cursor.getString(cursor.getColumnIndex(KEY_IMAGELINK))

                    val prod = ProductDetails(id=id, apiID = apiID, name = name, type = type, brand = brand, price = price, imageLink = imageLink)
                    prodList.add(prod)

                } while (cursor.moveToNext())
            }
            return prodList
        }

        /**
         * Function to delete record
         */
        fun deleteProducts(product: ProductDetails): Int {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_ID, product.id)

            val success = db.delete(TABLE_PRODUCTS, KEY_ID + "=" + product.id, null)

            db.close()
            return success
        }

        fun deleteProductsAPIID(product: ProductDetails): Int {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_APIID, product.apiID)

            val success = db.delete(TABLE_PRODUCTS, KEY_APIID + "=" + product.apiID, null)

            db.close()
            return success
        }

        fun searchProduct(apiId: Int): Int {

            val selectQuery = "SELECT  * FROM $TABLE_PRODUCTS WHERE $KEY_APIID=$apiId"

            val db = this.readableDatabase

            var cursor: Cursor? = null

            try {
                cursor = db.rawQuery(selectQuery, null)

            } catch (e: SQLiteException) {
                db.execSQL(selectQuery)
                return -1
            }

            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(KEY_ID))
            }else{
                return -1
            }
        }
}