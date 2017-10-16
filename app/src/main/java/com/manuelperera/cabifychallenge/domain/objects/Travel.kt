package com.manuelperera.cabifychallenge.domain.objects

import com.google.gson.annotations.SerializedName

open class Travel(val stops: List<Stop>,
                  @SerializedName("start_at")
                  val startAt: String?) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Travel

        if (stops != other.stops) return false
        if (startAt != other.startAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stops.hashCode()
        result = 31 * result + (startAt?.hashCode() ?: 0)
        return result
    }
}