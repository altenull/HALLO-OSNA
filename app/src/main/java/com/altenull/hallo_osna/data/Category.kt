package com.altenull.hallo_osna.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("categoryType") val categoryType: Picture.CategoryType,
    @SerializedName("color") val color: Int,
    @SerializedName("lightColor") val lightColor: Int,
    @SerializedName("symbol") val symbol: Int,
    @SerializedName("pictures") val pictures: List<Picture>,
): Parcelable