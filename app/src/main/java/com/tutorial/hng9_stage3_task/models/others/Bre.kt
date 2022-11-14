package com.tutorial.hng9_stage3_task.models.others


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Bre(
    @SerializedName("common")
    val common: String?,
    @SerializedName("official")
    val official: String?
) : Parcelable