package com.manuelperera.cabifychallenge.domain.objects

import com.google.gson.annotations.SerializedName

class VehicleType(@SerializedName("_id")
                  val id: String = "",
                  val name: String = "",
                  @SerializedName("short_name")
                  val shortName: String = "",
                  val description: String = "",
                  val icons: Icons = Icons(""),
                  val icon: String = "",
                  val eta: Eta = Eta(0, 0, false, ""))