package com.example.guitinxis.api.composants

import com.google.gson.Gson

data class Results (
    val id: Long,
    val title: String,
    val image: String,
    val imageType: String
) {
    public fun toJson() = Gson().toJson(this)

    companion object {
        public fun fromJson(json: String) = Gson().fromJson(json, Results::class.java)
    }
}