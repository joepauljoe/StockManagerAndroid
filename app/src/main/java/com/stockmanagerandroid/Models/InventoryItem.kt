package com.stockmanagerandroid.Models

import android.util.Log

class InventoryItem {
    var name : String = ""
    var userDesignatedID : String = ""
    var backstockQuantity : Int = 0
    var customerAccessibleQuantity : Int = 0
    var locations : ArrayList<Location>? = null
    var id : String = ""
    var dateLastPurchased : Long = 0L

    fun json(): HashMap<String, Any> {
        var json = HashMap<String, Any>()
        json["name"] = this.name
        json["userDesignatedID"] = this.userDesignatedID
        json["backstockQuantity"] = this.backstockQuantity
        json["customerAccessibleQuantity"] = this.customerAccessibleQuantity
        if (this.locations != null) {
            var locationsMap = ArrayList<HashMap<String, Any>>()
            for(location in this.locations!!) {
                locationsMap.add(location.json())
            }
            json["locations"] = locationsMap
        }
        json["id"] = this.id
        json["dateLastPurchased"] = this.dateLastPurchased
        return json
    }
}