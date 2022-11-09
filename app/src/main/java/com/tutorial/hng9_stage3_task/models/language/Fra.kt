package com.tutorial.hng9_stage3_task.models.language


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Fra(
    @SerializedName("f")
    val f: String,
    @SerializedName("m")
    val m: String
) : Parcelable