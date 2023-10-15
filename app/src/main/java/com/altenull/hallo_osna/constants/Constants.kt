package com.altenull.hallo_osna.constants

class Constants {
    object IntentKeys {
        const val Students = "STUDENTS"
        const val SelectedStudentId = "SELECTED_STUDENT_ID"
        const val Category = "CATEGORY"
    }

    object BundleKeys {
        const val Students = "STUDENTS"
    }

    object ViewPager {
        const val RotationY = -30
    }

    object StudentGrid {
        const val Rows = 2
        const val Cols = 1
        const val Size = Rows * Cols
    }

    object IndicatorAlpha {
        const val Active = 1.0F
        const val Inactive = 0.3F
    }

    object Pictures {
        const val MaxSize = 10
    }

    object AnimDuration {
        const val SceneReveal = 1000L
        const val MainModeReveal = 750L
    }
}
