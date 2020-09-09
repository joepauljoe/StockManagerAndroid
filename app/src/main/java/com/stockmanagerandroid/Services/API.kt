package com.stockmanagerandroid.Services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.databind.ObjectMapper
import com.stockmanagerandroid.Models.InventoryItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import kotlinx.coroutines.*
import java.lang.Thread.sleep


object API {

    private val url = "https://4086c4ea04bc.ngrok.io"
    val itemSearchedFor = MutableLiveData<InventoryItem>()

    fun queryItemByID(userDesignatedID: String, storeID: String) {
        var threadDone = false
        var returnItem: InventoryItem? = null

        val thread = Thread(Runnable {
            try {

                val objectMapper = ObjectMapper()
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                jsonObject.put("userDesignatedID", userDesignatedID)
                jsonObject.put("storeID", storeID)

                val client = OkHttpClient()
                val body = RequestBody.create(json, jsonObject.toString())

                val request = Request.Builder()
                    .url(url + "/item/query/udid")
                    .post(body)
                    .build();

                val call = client.newCall(request);
                val response = call.execute();
                val item = objectMapper.readValue(response.body?.string(), InventoryItem::class.java)
                itemSearchedFor.postValue(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

}