package com.tutorial.hng9_stage3_task.models.main


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CoatOfArms(
    @SerializedName("png")
    val png: String,
    @SerializedName("svg")
    val svg: String
) : Parcelable