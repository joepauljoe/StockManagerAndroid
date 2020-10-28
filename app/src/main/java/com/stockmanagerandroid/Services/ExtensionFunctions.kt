package com.stockmanagerandroid.Services

import com.stockmanagerandroid.Models.InventoryItem
import com.stockmanagerandroid.Models.Location

object ExtensionFunctions {

    fun locationJson(location: Location): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["accessibility"] = location.accessibility
        map["aisle"] = location.aisle
        map["aisleSection"] = location.aisleSection
        map["description"] = location.description
        map["spot"] = location.spot
        map["type"] = location.type
        return map
    }

    fun itemJson(item: InventoryItem): HashMap<String, Any> {
        val json = HashMap<String, Any>()
        json["name"] = item.name
        json["userDesignatedID"] = item.userDesignatedID
        json["backstockQuantity"] = item.backstockQuantity
        json["customerAccessibleQuantity"] = item.customerAccessibleQuantity
        if (item.locations != null) {
            val locationsMap = ArrayList<HashMap<String, Any>>()
            for(location in item.locations!!) {
                locationsMap.add(locationJson(location))
            }
            json["locations"] = locationsMap
        }
        json["id"] = item.id
        json["dateLastPurchased"] = item.dateLastPurchased
        return json
    }

    fun getConfirmationString(oldItem: InventoryItem, newItem: InventoryItem): String {
        var returnString = ""

        if (newItem.name != oldItem.name) {
            returnString += "Changed item name to " + newItem.name + "\n"
        }

        if(oldItem.backstockQuantity < newItem.backstockQuantity) {
            returnString += "Added " + (newItem.backstockQuantity - oldItem.backstockQuantity) + " units to backstock quantity\n\n"
        } else if(oldItem.backstockQuantity > newItem.backstockQuantity) {
            returnString += "Removed " + (oldItem.backstockQuantity - newItem.backstockQuantity) + " units from backstock quantity\n\n"
        }

        if(oldItem.customerAccessibleQuantity < newItem.customerAccessibleQuantity) {
            returnString += "Added " + (newItem.customerAccessibleQuantity - oldItem.customerAccessibleQuantity) + " unit(s) to customer accessible locations\n"
        } else if(oldItem.customerAccessibleQuantity > newItem.customerAccessibleQuantity) {
            returnString += "Removed " + (oldItem.customerAccessibleQuantity - newItem.customerAccessibleQuantity) + " unit(s) from customer accessible locations\n"
        }

        if(oldItem.locations == newItem.locations) {
            return returnString
        } else if ((oldItem.locations == null || oldItem.locations!!.size == 0) && (newItem.locations != null && newItem.locations!!.size > 0)) {
            returnString += "Added " + newItem.locations!!.size + " new locations for this item\n\n"
        } else if ((newItem.locations == null || newItem.locations!!.size == 0) && (oldItem.locations != null && oldItem.locations!!.size > 0)) {
            returnString += "Removed " + oldItem.locations!!.size + " locations for this item\n\n"
        } else if ((oldItem.locations != null && oldItem.locations!!.size != 0) && (newItem.locations != null && newItem.locations!!.size != 0) && oldItem.locations!!.size > newItem.locations!!.size) {
            returnString += "Removed " + (oldItem.locations!!.size - newItem.locations!!.size) + " locations for this item\n\n"
        } else if ((oldItem.locations != null && oldItem.locations!!.size != 0) && (newItem.locations != null && newItem.locations!!.size != 0) && newItem.locations!!.size > oldItem.locations!!.size) {
            returnString += "Added " + (newItem.locations!!.size - oldItem.locations!!.size) + " locations for this item\n\n"
        }

        if ((oldItem.locations != null && oldItem.locations!!.size != 0) && (newItem.locations != null && newItem.locations!!.size != 0)) {
            var modifiedLocations = 0
            if(oldItem.locations!!.size > newItem.locations!!.size) {
                for(location in newItem.locations!!) {
                    if(!oldItem.locations!!.contains(location)) {
                        modifiedLocations++
                    }
                }
            } else if (newItem.locations!!.size > oldItem.locations!!.size) {
                for(location in oldItem.locations!!) {
                    if(!newItem.locations!!.contains(location)) {
                        modifiedLocations++
                    }
                }
            }
            if(modifiedLocations > 0) {
                returnString += "Modified " + modifiedLocations + " locations\n\n"
            }
        }

        return returnString
    }

    fun copyItem(item: InventoryItem): InventoryItem {
        var newItem = item.copy()

        newItem.locations = ArrayList<Location>()
        if(item.locations != null && item.locations!!.size > 0){
            for(location in item.locations!!) {
                newItem.locations!!.add(location.copy())
            }
        }

        return newItem
    }
}