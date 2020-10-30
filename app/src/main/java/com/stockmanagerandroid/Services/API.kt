package com.stockmanagerandroid.Services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.stockmanagerandroid.Adapters.LocationsAdapter
import com.stockmanagerandroid.Models.InventoryItem
import com.stockmanagerandroid.Models.Task
import com.stockmanagerandroid.Models.User
import kotlinx.coroutines.Runnable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


object API {

    private val url = "https://smapi.ngrok.io"
    val itemSearchedFor = MutableLiveData<InventoryItem>()
    var modifiedItem = InventoryItem()
    var currentUser = User()
    val authenticated = MutableLiveData<Boolean>()
    val accountCreated = MutableLiveData<Boolean>()
    val imageData = MutableLiveData<ByteArray>()
    var errorString = ""
    var changesMade = false
    var mAdapter = LocationsAdapter()
    val nameItemsSearchedFor = MutableLiveData<ArrayList<InventoryItem>>()
    val nameToImage = MutableLiveData<HashMap<String, ByteArray?>>()
    var tasksList = ArrayList<Task>()
    val tasksFetched = MutableLiveData<Boolean>()

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

    fun queryItemByName(name: String, storeID: String) {
        var returnItem: InventoryItem? = null

        val thread = Thread(Runnable {
            try {

                val objectMapper = ObjectMapper()
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                jsonObject.put("name", name)
                jsonObject.put("storeID", storeID)

                val client = OkHttpClient()
                val itemRequestBody = RequestBody.create(json, jsonObject.toString())

                val itemRequest = Request.Builder()
                    .url(url + "/item/query/name")
                    .post(itemRequestBody)
                    .build()

                val itemCall = client.newCall(itemRequest)
                val itemResponse = itemCall.execute()
                val results : List<InventoryItem> = objectMapper.readValue(itemResponse.body?.string(), object : TypeReference<List<InventoryItem>>(){})
                nameItemsSearchedFor.postValue(results as ArrayList<InventoryItem>)

                for(result in results) {
                    val thread = Thread(Runnable {
                        try {
                            val jsonObject = JSONObject()
                            val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                            val imageClient = OkHttpClient()
                            jsonObject.put("id", result.id)
                            val imageRequestBody = RequestBody.create(json, jsonObject.toString())

                            val imageRequest = Request.Builder()
                                .url(url + "/item/image")
                                .post(imageRequestBody)
                                .build()

                            val imageCall = imageClient.newCall(imageRequest)
                            val imageResponse = imageCall.execute()
                            var nTI = HashMap<String, ByteArray?>()
                            if(nameToImage.value != null) {
                                nTI = nameToImage.value!!
                            }
                            nTI[result.name] = imageResponse.body?.bytes()
                            nameToImage.postValue(nTI)

                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    })

                    thread.start()
                }

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

    fun increment(userDesignatedID: String, value: Int, type: String) {

        val thread = Thread(Runnable {
            try {
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                val client = OkHttpClient()
                jsonObject.put("userDesignatedID", userDesignatedID)
                jsonObject.put("value", value)
                jsonObject.put("type", type)
                jsonObject.put("storeID", currentUser.storeID)
                val body = RequestBody.create(json, jsonObject.toString())

                val request = Request.Builder()
                    .url(url + "/item/increment")
                    .post(body)
                    .build()

                val call = client.newCall(request)
                call.execute()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

    fun decrement(userDesignatedID: String, value: Int, type: String) {

        val thread = Thread(Runnable {
            try {
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                val client = OkHttpClient()
                jsonObject.put("userDesignatedID", userDesignatedID)
                jsonObject.put("value", value)
                jsonObject.put("type", type)
                jsonObject.put("storeID", currentUser.storeID)
                val body = RequestBody.create(json, jsonObject.toString())

                val request = Request.Builder()
                    .url(url + "/item/decrement")
                    .post(body)
                    .build()

                val call = client.newCall(request)
                call.execute()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

    fun getTasks() {

        val thread = Thread(Runnable {
            try {

                val objectMapper = ObjectMapper()
                val module = SimpleModule()
                module.addDeserializer<Task>(Task::class.java, Deserializer())
                objectMapper.registerModule(module)
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                jsonObject.put("userID", currentUser.userID)
                jsonObject.put("storeID", currentUser.storeID)

                val client = OkHttpClient()
                val taskRequestBody = RequestBody.create(json, jsonObject.toString())

                val itemRequest = Request.Builder()
                    .url(url + "/user/tasks/get/outstanding")
                    .post(taskRequestBody)
                    .build()

                val taskCall = client.newCall(itemRequest)
                val itemResponse = taskCall.execute()
                val results : List<Task> = objectMapper.readValue(itemResponse.body?.string(), object : TypeReference<List<Task>>(){})
                tasksList = results as ArrayList<Task>
                tasksFetched.postValue(true)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

    fun completeTask(taskID: String) {

        val thread = Thread(Runnable {
            try {
                val jsonObject = JSONObject()
                val json = "application/json; charset=utf-8".toMediaTypeOrNull()
                val client = OkHttpClient()
                jsonObject.put("taskID", taskID)
                jsonObject.put("storeID", currentUser.storeID)
                val body = RequestBody.create(json, jsonObject.toString())

                val request = Request.Builder()
                    .url(url + "/task/complete")
                    .post(body)
                    .build()

                val call = client.newCall(request)
                call.execute()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

}