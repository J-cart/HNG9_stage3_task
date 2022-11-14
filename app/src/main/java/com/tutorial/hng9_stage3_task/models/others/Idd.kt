package com.tutorial.hng9_stage3_task.models.others


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Idd(
    @SerializedName("root")
    val root: String?,
    @SerializedName("suffixes")
    val suffixes: List<String>?
) : Parcelable