package com.manuelperera.cabifychallenge.domain.objects

import com.google.gson.annotations.SerializedName

open class Contact(val name: String,
                   @SerializedName("mobile_cc")
                   val mobileCc: String,
                   @SerializedName("mobile_num")
                   val mobileNum: String)