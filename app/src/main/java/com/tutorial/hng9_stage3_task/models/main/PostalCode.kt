package com.tutorial.hng9_stage3_task.models.main


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PostalCode(
    @SerializedName("format")
    val format: String?,
    @SerializedName("regex")
    val regex: String?
) : Parcelable