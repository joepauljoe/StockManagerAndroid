package com.stockmanagerandroid.Models

import android.util.Log

data class InventoryItem (
    var name : String = "",
    var userDesignatedID : String = "",
    var backstockQuantity : Int = 0,
    var customerAccessibleQuantity : Int = 0,
    var locations : ArrayList<Location>? = null,
    var id : String = "",
    var dateLastPurchased : Long = 0L
)