package com.altenull.hallo_osna.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentFrame(
    @SerializedName("frameX") val frameX: Int,
    @SerializedName("frameY") val frameY: Int,
    @SerializedName("frameHeight") val frameHeight: Int,
) : Parcelable