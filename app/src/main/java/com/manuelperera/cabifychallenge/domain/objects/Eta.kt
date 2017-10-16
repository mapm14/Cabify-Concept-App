package com.manuelperera.cabifychallenge.domain.objects

import com.google.gson.annotations.SerializedName

open class Eta(val min: Int,
               val max: Int,
               @SerializedName("low_availability")
               val lowAvailability: Boolean,
               val formatted: String)