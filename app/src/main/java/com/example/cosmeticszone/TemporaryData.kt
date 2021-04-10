package com.example.cosmeticszone

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue

object TemporaryData {
    private lateinit var queue: RequestQueue
    internal lateinit var dataSet: Array<Pair<String, String>>

    fun getQueue(): RequestQueue {
        return queue
    }

    fun prepare(context: Context) {
        queue = newRequestQueue(context)
    }
}