package com.example.privatevanmanagement.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VanModel
      {

    @SerializedName("VanModel")
    @Expose
    var vanModel: String? = null

    @SerializedName("VanName")
    @Expose
    var vanName: String? = null

    @SerializedName("VanNumber")
    @Expose
    var vanNumber: String? = null



    constructor(
        vanModel: String,
        vanName: String,
        vanNumber: String
    ) {
        this.vanModel=vanModel
        this.vanName = vanName
        this.vanNumber = vanNumber
     }

}