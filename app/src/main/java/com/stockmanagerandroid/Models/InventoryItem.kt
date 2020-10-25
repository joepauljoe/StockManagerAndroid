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

    fun getConfirmationString(newItem: InventoryItem): String {
        var returnString = ""

        if (newItem.name != this.name) {
            returnString += "Changed item name to " + newItem.name + "\n"
        }

        if(this.backstockQuantity < newItem.backstockQuantity) {
            returnString += "Removed " + (newItem.backstockQuantity - this.backstockQuantity) + " units from backstock\n"
        } else if(this.backstockQuantity > newItem.backstockQuantity) {
            returnString += "Added " + (this.backstockQuantity - newItem.backstockQuantity) + " units to backstock\n"
        }

        if(this.customerAccessibleQuantity < newItem.customerAccessibleQuantity) {
            returnString += "Removed " + (newItem.customerAccessibleQuantity - this.customerAccessibleQuantity) + " units from customer accessible locations\n"
        } else if(this.customerAccessibleQuantity > newItem.customerAccessibleQuantity) {
            returnString += "Added " + (this.customerAccessibleQuantity - newItem.customerAccessibleQuantity) + " units to customer accessible locations\n"
        }

        if(this.locations == newItem.locations) {
            return returnString
        } else if ((this.locations == null || this.locations!!.size == 0) && (newItem.locations != null && newItem.locations!!.size > 0)) {
            returnString += "Added " + newItem.locations!!.size + " new locations for this item\n"
        } else if ((newItem.locations == null || newItem.locations!!.size == 0) && (this.locations != null && this.locations!!.size > 0)) {
            returnString += "Removed " + this.locations!!.size + " locations for this item\n"
        } else if ((this.locations != null && this.locations!!.size != 0) && (newItem.locations != null && newItem.locations!!.size != 0) && this.locations!!.size > newItem.locations!!.size) {
            returnString += "Removed " + (this.locations!!.size - newItem.locations!!.size) + " locations for this item\n"
        } else if ((this.locations != null && this.locations!!.size != 0) && (newItem.locations != null && newItem.locations!!.size != 0) && newItem.locations!!.size > this.locations!!.size) {
            returnString += "Added " + (newItem.locations!!.size - this.locations!!.size) + " locations for this item\n"
        }

        if ((this.locations != null && this.locations!!.size != 0) && (newItem.locations != null && newItem.locations!!.size != 0)) {
            var modifiedLocations = 0
            if(this.locations!!.size > newItem.locations!!.size) {
                for(location in newItem.locations!!) {
                    if(!this.locations!!.contains(location)) {
                        modifiedLocations++
                    }
                }
            } else if (newItem.locations!!.size > this.locations!!.size) {
                for(location in this.locations!!) {
                    if(!newItem.locations!!.contains(location)) {
                        modifiedLocations++
                    }
                }
            }
            if(modifiedLocations > 0) {
                returnString += "Modified " + modifiedLocations + " locations\n"
            }
        }

        return returnString
    }
}