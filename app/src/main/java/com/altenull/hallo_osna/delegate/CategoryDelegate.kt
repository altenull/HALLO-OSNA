package com.altenull.hallo_osna.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Category

interface CategoryDelegation {
    fun getCategoryFromIntent(context: Context): Category
}

class CategoryDelegate : CategoryDelegation {
    override fun getCategoryFromIntent(context: Context): Category =
        obtainCategory((context as Activity).intent)

    private fun obtainCategory(intent: Intent): Category {
        val fromCategory = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(Constants.IntentKeys.Category, Category::class.java)
        } else {
            intent.getParcelableExtra<Category>(Constants.IntentKeys.Category)
        }

        return fromCategory!!
    }
}
