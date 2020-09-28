package com.stockmanagerandroid.Services

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.databind.ObjectMapper
import com.stockmanagerandroid.Models.InventoryItem
import com.stockmanagerandroid.Models.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import kotlinx.coroutines.*
import okhttp3.Response
import java.lang.Thread.sleep


object API {

    private val url = "https://smapi.ngrok.io"
    val itemSearchedFor = MutableLiveData<InventoryItem>()
    var currentUser = User()
    val authenticated = MutableLiveData<Boolean>()
    val accountCreated = MutableLiveData<Boolean>()
    val imageData = MutableLiveData<ByteArray>()
    var errorString = ""

    fun queryItemByID(userDesignatedID: String, storeID: String) {
        var returnItem: InventoryItem? = null

        val thread = Thread(Runnable {
            try {

                val objectMapper = ObjectMapper()
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                jsonObject.put("userDesignatedID", userDesignatedID)
                jsonObject.put("storeID", storeID)

                val client = OkHttpClient()
                val itemRequestBody = RequestBody.create(json, jsonObject.toString())

                val itemRequest = Request.Builder()
                    .url(url + "/item/query/udid")
                    .post(itemRequestBody)
                    .build()

                val itemCall = client.newCall(itemRequest)
                val itemResponse = itemCall.execute()
                val item = objectMapper.readValue(itemResponse.body?.string(), InventoryItem::class.java)
                itemSearchedFor.postValue(item)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

    fun authenticateUser(email: String, password: String) {
        var response: Response? = null
        var responseBody: String? = ""
        val thread = Thread(Runnable {
            try {

                val objectMapper = ObjectMapper()
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                jsonObject.put("email", email.toLowerCase())
                jsonObject.put("password", password)

                val itemClient = OkHttpClient()
                val body = RequestBody.create(json, jsonObject.toString())

                val request = Request.Builder()
                    .url(url + "/user/authenticate")
                    .post(body)
                    .build()

                val call = itemClient.newCall(request);
                response = call.execute();
                responseBody = response!!.body?.string()
                val user = objectMapper.readValue(responseBody, User::class.java)
                if (user != null) {
                    print("Not null user")
                    user.pk = ""
                    currentUser = user
                    authenticated.postValue(true)
                } else {
                    print("Null user")
                    print(response)
                    authenticated.postValue(false)
                }
            } catch (e: Exception) {
                if(responseBody != null) {
                    errorString = responseBody!!
                } else {
                    errorString = "Unable to complete request. Please try again later."
                }
                Log.d("errorString", errorString)
                authenticated.postValue(false)
                e.printStackTrace()
            }
        })

        thread.start()
    }

    fun imageQuery(itemID: String) {
        val thread = Thread(Runnable {
            try {
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                val imageClient = OkHttpClient()
                jsonObject.put("id", itemID)
                val imageRequestBody = RequestBody.create(json, jsonObject.toString())

                val imageRequest = Request.Builder()
                    .url(url + "/item/image")
                    .post(imageRequestBody)
                    .build()

                val imageCall = imageClient.newCall(imageRequest)
                val imageResponse = imageCall.execute()
                imageData.postValue(imageResponse.body?.bytes())

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

    fun updateItem(item: InventoryItem, storeID: String) {
        val thread = Thread(Runnable {
            try {
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                val imageClient = OkHttpClient()
                for ((key, value) in item.json()) {
                    jsonObject.put(key, value)
                }
                jsonObject.put("storeID", storeID)
                val body = RequestBody.create(json, jsonObject.toString())

                val imageRequest = Request.Builder()
                    .url(url + "/item/update")
                    .post(body)
                    .build()

                val call = imageClient.newCall(imageRequest)
                call.execute()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

    fun createAccount(invitationCode: String, fName: String, lName: String, email: String, password: String, confirmPassword: String) {
        errorString = ""
        if (password != confirmPassword) {
            errorString = "Passwords do not match"
            return
        }

        val thread = Thread(Runnable {
            try {
                val objectMapper = ObjectMapper()
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                val imageClient = OkHttpClient()
                jsonObject.put("invitationCode", invitationCode)
                jsonObject.put("firstName", fName)
                jsonObject.put("lastName", lName)
                jsonObject.put("email", email)
                jsonObject.put("password", password)
                val body = RequestBody.create(json, jsonObject.toString())

                val imageRequest = Request.Builder()
                    .url(url + "/user/create")
                    .post(body)
                    .build()

                val call = imageClient.newCall(imageRequest)
                val response = call.execute()
                val responseBody = response.body?.string()
                val user = objectMapper.readValue(responseBody, User::class.java)
                if (user != null) {
                    print("Not null user")
                    user.pk = ""
                    currentUser = user
                    accountCreated.postValue(true)
                } else {
                    print("Null user")
                    print(response)
                    accountCreated.postValue(false)
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

}