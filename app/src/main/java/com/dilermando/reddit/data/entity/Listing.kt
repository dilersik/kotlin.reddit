package com.dilermando.reddit.data.entity

import com.google.gson.annotations.SerializedName

data class Listing(

        @SerializedName("modhash")
        val modhash: String? = null,

        @SerializedName("children")
        val things: List<Thing>? = null,

        @SerializedName("after")
        val after: String? = null,

        @SerializedName("before")
        val before: Any? = null
)