package com.manuelperera.cabifychallenge.domain.objects

import co.develoop.androidcleanarchitecture.screen.presenter.recyclerview.RecyclerViewAdapterItem
import com.google.gson.annotations.SerializedName

class Estimate(@SerializedName("vehicle_type")
               val vehicleType: VehicleType,
               @SerializedName("total_price")
               val totalPrice: Int,
               @SerializedName("price_formatted")
               val priceFormatted: String,
               val currency: String,
               @SerializedName("currency_symbol")
               val currencySymbol: String): RecyclerViewAdapterItem {

    constructor(type: RecyclerViewAdapterItem.Type) : this(VehicleType(), 0, "", "", "") {
        this.type = type
    }

    private var type: RecyclerViewAdapterItem.Type = RecyclerViewAdapterItem.Type.ITEM

    override fun setType(type: RecyclerViewAdapterItem.Type) {
        this.type = type
    }

    override fun getType(): RecyclerViewAdapterItem.Type = type
}