package com.altenull.hallo_osna.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    @SerializedName("studentId") val studentId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("englishName") val englishName: String,
    @SerializedName("major") val major: String,
    @SerializedName("email") val email: String?,
    @SerializedName("periodStart") val periodStart: String,
    @SerializedName("periodEnd") val periodEnd: String,
    @SerializedName("wallPictureUrl") val wallPictureUrl: String,
    @SerializedName("pictures") val pictures: List<Picture>,
): Parcelable