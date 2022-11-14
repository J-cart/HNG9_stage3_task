package com.tutorial.hng9_stage3_task.models.main


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.tutorial.hng9_stage3_task.models.others.Eng
import com.tutorial.hng9_stage3_task.models.others.Fra

@Parcelize
data class Demonyms(
    @SerializedName("eng")
    val eng: Eng?,
    @SerializedName("fra")
    val fra: Fra?
) : Parcelable