package com.tutorial.hng9_stage3_task.models.main


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.tutorial.hng9_stage3_task.models.languages.COP

@Parcelize
data class Currencies(
    @SerializedName("COP")
    val cOP: COP?
) : Parcelable