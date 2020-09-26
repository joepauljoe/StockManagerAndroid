package com.stockmanagerandroid.Models

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
            json["locations"] = this.locations!!
        }
        json["id"] = this.id
        json["dateLastPurchased"] = this.dateLastPurchased
        return json
    }
}