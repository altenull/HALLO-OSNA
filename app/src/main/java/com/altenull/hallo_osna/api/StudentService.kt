package com.altenull.hallo_osna.api

import com.altenull.hallo_osna.data.Student
import retrofit2.Response
import retrofit2.http.GET

interface StudentService {
    @GET("students.json")
    suspend fun getStudents(): Response<List<Student>>
}
