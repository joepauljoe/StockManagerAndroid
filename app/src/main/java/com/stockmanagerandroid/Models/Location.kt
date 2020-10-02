package com.stockmanagerandroid.Models

class Location {

    var accessibility : String = "Unprocessed"
    var aisle : String = ""
    var aisleSection : String = ""
    var description : String = ""
    var spot : String = ""
    var type : String = "Unknown"

    fun json(): HashMap<String, Any> {
        var map = HashMap<String, Any>()
        map["accessibility"] = this.accessibility
        map["aisle"] = this.aisle
        map["aisleSection"] = this.aisleSection
        map["description"] = this.description
        map["spot"] = this.spot
        map["type"] = this.type
        return map
    }
}