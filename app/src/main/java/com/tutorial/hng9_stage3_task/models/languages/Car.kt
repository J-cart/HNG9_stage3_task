package com.tutorial.hng9_stage3_task.models.languages


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Car(
    @SerializedName("side")
    val side: String?,
    @SerializedName("signs")
    val signs: List<String>?
) : Parcelable