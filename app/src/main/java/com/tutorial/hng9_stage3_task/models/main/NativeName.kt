package com.tutorial.hng9_stage3_task.models.main


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.tutorial.hng9_stage3_task.models.others.Spa

@Parcelize
data class NativeName(
    @SerializedName("spa")
    val spa: Spa?
) : Parcelable