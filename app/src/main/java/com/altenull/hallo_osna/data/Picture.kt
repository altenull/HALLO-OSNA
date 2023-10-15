package com.altenull.hallo_osna.data

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Picture(
    @SerializedName("url") val url: String,
    @SerializedName("title") val title: String,
    @SerializedName("comment") val comment: String,
    @SerializedName("location") val location: String?,
    @SerializedName("categoryType") val categoryType: CategoryType,
): Parcelable {
    enum class CategoryType {
        FOOD,
        DAILY,
        DORMITORY,
        UNIVERSITY,
        TRAVEL,
        LEISURE
    }
}
