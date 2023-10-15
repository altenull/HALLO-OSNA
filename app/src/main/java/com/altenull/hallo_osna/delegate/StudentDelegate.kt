package com.altenull.hallo_osna.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Student

interface StudentDelegation {
    fun getStudentsFromIntent(context: Context): List<Student>

    fun getSelectedStudentIdFromIntent(context: Context): Int
}

class StudentDelegate : StudentDelegation {
    override fun getStudentsFromIntent(context: Context): List<Student> =
        obtainStudents((context as Activity).intent)

    private fun obtainStudents(intent: Intent): List<Student> {
        val fromStudents = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableArrayListExtra(Constants.IntentKeys.Students, Student::class.java)
        } else {
            intent.getParcelableArrayListExtra<Student>(Constants.IntentKeys.Students)
        }

        return fromStudents?.toList() ?: emptyList()
    }

    override fun getSelectedStudentIdFromIntent(context: Context): Int =
        obtainSelectedStudentId((context as Activity).intent)

    private fun obtainSelectedStudentId(intent: Intent): Int {
        return intent.getIntExtra(Constants.IntentKeys.SelectedStudentId, 0)
    }
}
