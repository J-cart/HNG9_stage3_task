package com.tutorial.hng9_stage3_task.models.main


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Maps(
    @SerializedName("googleMaps")
    val googleMaps: String?,
    @SerializedName("openStreetMaps")
    val openStreetMaps: String?
) : Parcelable