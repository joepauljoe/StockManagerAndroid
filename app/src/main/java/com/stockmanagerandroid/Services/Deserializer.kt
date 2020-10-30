package com.stockmanagerandroid.Services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.stockmanagerandroid.Models.Task
import java.io.IOException


class Deserializer @JvmOverloads constructor(vc: Class<*>? = null) :
        StdDeserializer<Task?>(vc) {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): Task {
            val taskNode: JsonNode = jp.getCodec().readTree(jp)
            val task = Task("",HashMap<String, String>(), "", HashMap<String, String>(), 0, 0, 0, "")
            task.assignedEmployeeID = taskNode["assignedEmployeeID"].textValue()
            task.id = taskNode["id"].textValue()
            task.timeAssigned = taskNode["timeAssigned"].intValue()
            task.userDesignatedID = taskNode["userDesignatedID"].textValue()
            task.src["aisle"] = taskNode["src"]["aisle"].textValue()
            task.src["aisleSection"] = taskNode["src"]["aisleSection"].textValue()
            task.src["spot"] = taskNode["src"]["spot"].textValue()
            task.src["type"] = taskNode["src"]["type"].textValue()
            task.dest["aisle"] = taskNode["dest"]["aisle"].textValue()
            task.dest["aisleSection"] = taskNode["dest"]["aisleSection"].textValue()
            task.dest["spot"] = taskNode["dest"]["spot"].textValue()
            task.dest["type"] = taskNode["dest"]["type"].textValue()

            return task
        }
    }