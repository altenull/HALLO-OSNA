package com.altenull.hallo_osna

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Picture
import com.altenull.hallo_osna.data.Student
import com.altenull.hallo_osna.databinding.ActivityMainBinding
import com.altenull.hallo_osna.delegate.CategoryTypeDelegate
import com.altenull.hallo_osna.delegate.CategoryTypeDelegation
import com.altenull.hallo_osna.delegate.StudentDelegate
import com.altenull.hallo_osna.delegate.StudentDelegation
import com.altenull.hallo_osna.shared_preferences.VisitedStudentPrefs
import com.altenull.hallo_osna.utils.TouchBlockerUtil
import com.altenull.hallo_osna.viewmodel.EmitterViewModel
import com.altenull.hallo_osna.viewmodel.MainMode
import com.altenull.hallo_osna.viewmodel.MainModeViewModel
import com.altenull.hallo_osna.viewmodel.RevealSceneCategoryViewModel
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), StudentDelegation by StudentDelegate(),
    CategoryTypeDelegation by CategoryTypeDelegate() {
    private val mainModeViewModel: MainModeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var students: List<Student>

    private val revealSceneStudentViewModel: EmitterViewModel by viewModels()
    private val revealSceneCategoryViewModel: RevealSceneCategoryViewModel by viewModels()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        var backButtonPressedTime: Long = 0L

        override fun handleOnBackPressed() {
            val currentTime: Long = System.currentTimeMillis()
            val gapTime: Long = currentTime - backButtonPressedTime

            if (gapTime in 0..2000) {
                finishAndRemoveTask()
            } else {
                backButtonPressedTime = currentTime
                showBackButtonPressedToast()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        visitedStudentPrefs = VisitedStudentPrefs(this)
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            binding.mainMode = mainModeViewModel.mainMode.value
            binding.switchMainMode = { mainModeViewModel.switchMainMode() }
        }

        students = getStudentsFromIntent(this)

        binding.layoutMainHeader.logoBlack.setOnClickListener {
            YoYo.with(Techniques.Tada).duration(1000L).playOn(it)
        }
        binding.layoutMainHeader.icHelpBlack.setOnClickListener {
            TouchBlockerUtil.blockTouchForHelpEnter(window)
            val intent = Intent(this@MainActivity, HelpActivity::class.java)
            intent.putParcelableArrayListExtra(Constants.IntentKeys.Students, ArrayList(students))
            startActivity(intent)
        }

        binding.layoutRevealSceneCategory.root.hideBeforeReveal = false
        binding.layoutRevealSceneCategory.root.animationDuration =
            Constants.AnimDuration.SceneReveal.toInt()

        mainModeViewModel.mainMode.observe(this) {
            when (it!!) {
                MainMode.STORY -> {
                    TouchBlockerUtil.blockTouchForMainModeReveal(window)
                    toStoryMode()
                }
                MainMode.CATEGORY -> {
                    TouchBlockerUtil.blockTouchForMainModeReveal(window)
                    toCategoryMode()
                }
            }
        }
        revealSceneStudentViewModel.isEmitted.observe(this) {
            if (it) {
                TouchBlockerUtil.blockTouchForMainSceneReveal(window)
                startRevealSceneStudentAnim()
            }
        }
        revealSceneCategoryViewModel.sceneCategoryType.observe(this) {
            if (it is Picture.CategoryType) {
                TouchBlockerUtil.blockTouchForMainSceneReveal(window)
                startRevealSceneCategoryAnim(it)
            }
        }
    }

    private fun showBackButtonPressedToast() {
        Toast.makeText(this, "\'이전\' 버튼을 한 번 더 누르면 앱이 종료 됩니다.", Toast.LENGTH_SHORT).show()
    }

    private fun toStoryMode() {
        supportFragmentManager.commit {
            replace(R.id.fragment_main_content, MainStoryFragment().apply {
                val studentsArrayList = arrayListOf<Student>()
                studentsArrayList.addAll(students)

                arguments = Bundle().apply {
                    putParcelableArrayList(Constants.BundleKeys.Students, studentsArrayList)
                }
            })
            setReorderingAllowed(true)
        }
        binding.mainMode = MainMode.STORY
    }

    private fun toCategoryMode() {
        supportFragmentManager.commit {
            replace(R.id.fragment_main_content, MainCategoryFragment().apply {
                val studentsArrayList = arrayListOf<Student>()
                studentsArrayList.addAll(students)

                arguments = Bundle().apply {
                    putParcelableArrayList(Constants.BundleKeys.Students, studentsArrayList)
                }
            })
            setReorderingAllowed(true)
        }
        binding.mainMode = MainMode.CATEGORY
    }

    private fun startRevealSceneStudentAnim() {
        binding.layoutMainHeader.logoBlack.animate().alpha(0.0F).duration = 200L
        binding.layoutMainHeader.icHelpBlack.animate().alpha(0.0F).duration = 200L
        binding.layoutMainHeader.mainModeButtonLayout.visibility = View.INVISIBLE

        binding.layoutRevealSceneStudent.root.visibility = View.VISIBLE
        binding.layoutRevealSceneStudent.root.animate().alpha(1.0F)
            .setInterpolator(DecelerateInterpolator(2.0F)).duration = 500L
    }

    private fun resetRevealSceneStudentAnim() {
        binding.layoutMainHeader.logoBlack.alpha = 1.0F
        binding.layoutMainHeader.icHelpBlack.alpha = 1.0F
        binding.layoutMainHeader.mainModeButtonLayout.visibility = View.VISIBLE

        binding.layoutRevealSceneStudent.root.visibility = View.GONE
        binding.layoutRevealSceneStudent.root.alpha = 0.0F
    }

    private fun startRevealSceneCategoryAnim(sceneCategoryType: Picture.CategoryType) {
        val centerPoint = Point(
            (resources.displayMetrics.widthPixels / 2.0).roundToInt(),
            (resources.displayMetrics.heightPixels / 2.0).roundToInt()
        )
        val color = getColorByCategoryType(sceneCategoryType)
        val lightColor = getLightColorByCategoryType(sceneCategoryType)

        binding.layoutRevealSceneCategory.revealSceneCategoryBackground.setBackgroundColor(
            ContextCompat.getColor(this, color)
        )
        binding.layoutRevealSceneCategory.root.setBackgroundColor(
            ContextCompat.getColor(this, lightColor)
        )
        binding.layoutRevealSceneCategory.root.visibility = View.VISIBLE
        binding.layoutRevealSceneCategory.root.setDisplayedChild(
            1, true, centerPoint
        )
    }

    private fun resetRevealSceneCategoryAnim() {
        binding.layoutRevealSceneCategory.root.visibility = View.GONE
        binding.layoutRevealSceneCategory.root.displayedChild = 0
    }

    fun startRevealSceneStudent() {
        revealSceneStudentViewModel.emit()
    }

    fun startRevealSceneCategory(categoryType: Picture.CategoryType) {
        revealSceneCategoryViewModel.revealSceneCategory(categoryType)
    }

    override fun onResume() {
        super.onResume()
        resetRevealSceneStudentAnim()
        resetRevealSceneCategoryAnim()
    }

    companion object {
        const val TAG = "MainActivity"
        lateinit var visitedStudentPrefs: VisitedStudentPrefs
    }
}