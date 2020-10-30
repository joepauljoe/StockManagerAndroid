package com.stockmanagerandroid.Models

data class Task (
    var assignedEmployeeID: String,
    var dest: HashMap<String, String>,
    var id: String,
    var src: HashMap<String, String>,
    var timeAssigned: Int,
    var timeCompleted: Int,
    var timeApproved: Int,
    var userDesignatedID: String
)