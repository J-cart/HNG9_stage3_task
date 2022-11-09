package com.tutorial.hng9_stage3_task.models.languages


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Gini(
    @SerializedName("2019")
    val x2019: Double?
) : Parcelable