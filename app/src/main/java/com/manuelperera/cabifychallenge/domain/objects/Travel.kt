package com.manuelperera.cabifychallenge.domain.objects

import com.google.gson.annotations.SerializedName

data class Travel(val stops: List<Stop>,
                  @SerializedName("start_at")
                  val startAt: String?)