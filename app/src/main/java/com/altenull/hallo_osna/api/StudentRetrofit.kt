package com.altenull.hallo_osna.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StudentRetrofit {
    companion object {
        val studentService: StudentService = Retrofit.Builder()
            .baseUrl("https://hallo-osna-kotlin.s3.ap-northeast-2.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StudentService::class.java)
    }
}