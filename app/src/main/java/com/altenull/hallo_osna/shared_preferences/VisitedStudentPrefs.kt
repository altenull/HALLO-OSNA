package com.altenull.hallo_osna.shared_preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * `studentId` is used as key.
 */
class VisitedStudentPrefs(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("visited_student", Context.MODE_PRIVATE)

    fun isVisitedStudent(studentId: Int): Boolean {
        return prefs.getBoolean(studentId.toString(), false)
    }

    fun visitStudent(studentId: Int) {
        prefs.edit().putBoolean(studentId.toString(), true).apply()
    }
}