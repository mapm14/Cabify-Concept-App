package com.manuelperera.cabifychallenge.domain.objects

import com.google.gson.annotations.SerializedName

open class Stop(
        @SerializedName("loc")
        val location: List<Double>,
        val name: String,
        val address: String,
        val num: String,
        val city: String,
        val country: String,
        @SerializedName("instr")
        val instructions: String,
        val contact: Contact)