package com.altenull.hallo_osna

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.altenull.hallo_osna.adapter.IndicatorGridAdapter
import com.altenull.hallo_osna.adapter.StudentPicturesAdapter
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Student
import com.altenull.hallo_osna.databinding.ActivityStudentSceneBinding
import com.altenull.hallo_osna.delegate.StudentDelegate
import com.altenull.hallo_osna.delegate.StudentDelegation
import com.altenull.hallo_osna.utils.TouchBlockerUtil
import com.altenull.hallo_osna.viewmodel.EmitterViewModel
import com.altenull.hallo_osna.viewmodel.Zoom
import com.altenull.hallo_osna.viewmodel.ZoomViewModel
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class StudentSceneActivity : AppCompatActivity(), StudentDelegation by StudentDelegate() {
    private lateinit var binding: ActivityStudentSceneBinding
    private lateinit var students: List<Student>
    private lateinit var student: Student

    private val zoomViewModel: ZoomViewModel by viewModels()
    private val isAllPicturesBoundEmitterViewModel: EmitterViewModel by viewModels()
    private val isAllIndicatorsBoundEmitterViewModel: EmitterViewModel by viewModels()
    private val progressBarColorFilter: PorterDuffColorFilter by lazy {
        PorterDuffColorFilter(
            ContextCompat.getColor(this, R.color.osna_yellow),
            PorterDuff.Mode.SRC_IN
        )
    }
    private val progressBarLoadingAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.loading)
    }
    private val zoomedInImageScale: Float = 1.0F
    private val zoomedOutImageScale: Float by lazy {
        /**
         * The `heightPixels` doesn't return the real device height because the status bar height is excluded.
         * But it doesn't matter that the impact is too small.
         * Therefore, that kind of factors are ignored.
         */
        resources.displayMetrics.run {
            val dpHeight = (heightPixels / density)
            ((dpHeight - 116.0) / dpHeight).toFloat()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentSceneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        students = getStudentsFromIntent(this)
        student = students.find {
            it.studentId == getSelectedStudentIdFromIntent(this)
        }!!

        introduceStudent()

        zoomViewModel.zoom.observe(this) { zoom ->
            when (zoom!!) {
                Zoom.ZoomedIn -> zoomIn()
                Zoom.ZoomedOut -> zoomOut()
            }
        }
        isAllPicturesBoundEmitterViewModel.isEmitted.observe(this) {
            if (it) updateStudentPicturesScale(
                Zoom.ZoomedOut
            )
        }
        isAllIndicatorsBoundEmitterViewModel.isEmitted.observe(this) {
            if (it) updateIndicatorsAlpha(
                0
            )
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.view_exit)
    }

    private fun introduceStudent() {
        val (studentId, name, englishName, major, email, periodStart, periodEnd, wallPictureUrl, pictures) = student
        val firstPicture = pictures[0]

        binding.sceneStudentName.text = name.toCharArray().joinToString(" ")
        binding.sceneStudentEnglishName.text =
            getString(R.string.scene_student_english_name, englishName)
        binding.sceneStudentMajor.text = major
        binding.sceneStudentPeriod.text =
            getString(R.string.scene_student_period, periodStart, periodEnd)
        binding.sceneStudentTitle.text = firstPicture.title
        binding.sceneStudentComment.text = getString(R.string.picture_comment, firstPicture.comment)

        binding.logoYellow.setOnClickListener {
            YoYo.with(Techniques.Tada)
                .duration(1000L)
                .playOn(it)
        }

        binding.sceneStudentPicturesIndicator.adapter = IndicatorGridAdapter(student.pictures) {
            isAllIndicatorsBoundEmitterViewModel.emit()
        }
        /**
         * Previously, IndicatorGridAdapter was implemented by BaseAdapter().
         * But it was unable to detect when the all indicators were layouted.
         * Therefore, re-implement IndicatorGridAdapter with RecyclerView.Adapter() and need to set GridLayoutManager like below.
         */
        binding.sceneStudentPicturesIndicator.layoutManager = GridLayoutManager(this, 5)

        binding.studentPicturesViewpager.adapter = StudentPicturesAdapter(pictures, {
            zoomViewModel.switchZoom()
        }, {
            isAllPicturesBoundEmitterViewModel.emit()
        },
            progressBarColorFilter,
            progressBarLoadingAnim
        )
        binding.studentPicturesViewpager.offscreenPageLimit = Constants.Pictures.MaxSize - 1
        binding.studentPicturesViewpager.setPageTransformer { page, position ->
            page.rotationY = position * Constants.ViewPager.RotationY
        }
        binding.studentPicturesViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val currentPicture = pictures[position]

                binding.sceneStudentTitle.text = currentPicture.title
                binding.sceneStudentComment.text =
                    getString(R.string.picture_comment, currentPicture.comment)

                updateIndicatorsAlpha(position)
            }
        })

        binding.icHelpYellow.setOnClickListener {
            TouchBlockerUtil.blockTouchForHelpEnter(window)
            val intent = Intent(this@StudentSceneActivity, HelpActivity::class.java)
            intent.putParcelableArrayListExtra(Constants.IntentKeys.Students, ArrayList(students))
            startActivity(intent)
        }
    }

    private fun updateIndicatorsAlpha(position: Int) {
        for (i in student.pictures.indices) {
            val alpha: Float = when (i) {
                position -> Constants.IndicatorAlpha.Active
                else -> Constants.IndicatorAlpha.Inactive
            }

            binding.sceneStudentPicturesIndicator.getChildAt(i)?.animate()?.alpha(alpha)?.duration =
                200L
        }
    }

    private fun updateStudentPicturesScale(zoom: Zoom) {
        val scale: Float = when (zoom) {
            Zoom.ZoomedIn -> zoomedInImageScale
            Zoom.ZoomedOut -> zoomedOutImageScale
        }

        val pictureViews =
            (binding.studentPicturesViewpager.adapter as StudentPicturesAdapter).pictureViews

        for (i in pictureViews.indices) {
            pictureViews[i].findViewById<ImageView>(R.id.student_picture)?.animate()?.scaleX(scale)
                ?.scaleY(scale)
        }
    }

    private fun zoomIn() {
        /**
         * 1. Change background color (black)
         */
        window.decorView.setBackgroundColor(Color.BLACK)

        /**
         * 2. Animation (slide)
         */
        val cornerPairs = listOf(
            Pair(binding.sceneStudentLayout1, R.anim.slide_top_left),
            Pair(binding.sceneStudentLayout2, R.anim.slide_top_right),
            Pair(binding.sceneStudentLayout3, R.anim.slide_bottom_left),
            Pair(binding.sceneStudentLayout4, R.anim.slide_bottom_right),
        )
        for (i in cornerPairs.indices) {
            val (layout, animId) = cornerPairs[i]

            layout.startAnimation(AnimationUtils.loadAnimation(this, animId))
            layout.animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    layout.visibility = View.INVISIBLE
                }
            })
        }

        /**
         * 3. Zoom-in images
         */
        updateStudentPicturesScale(Zoom.ZoomedIn)

        /**
         * 4. Show title and comment
         */
        binding.sceneStudentTitleLayout.animate().y(0F).setInterpolator(
            DecelerateInterpolator(2.0F)
        ).duration = 1000L

        binding.sceneStudentCommentLayout.visibility = View.VISIBLE
        YoYo.with(Techniques.BounceIn)
            .duration(1000L)
            .playOn(binding.sceneStudentCommentLayout)
    }

    private fun zoomOut() {
        /**
         * 1. Change background color (white)
         */
        window.decorView.setBackgroundColor(Color.WHITE)

        /**
         * 2. Animation (slide back)
         */
        val cornerPairs = listOf(
            Pair(binding.sceneStudentLayout1, R.anim.slide_back_top_left),
            Pair(binding.sceneStudentLayout2, R.anim.slide_back_top_right),
            Pair(binding.sceneStudentLayout3, R.anim.slide_back_bottom_left),
            Pair(binding.sceneStudentLayout4, R.anim.slide_back_bottom_right),
        )
        for (i in cornerPairs.indices) {
            val (layout, animId) = cornerPairs[i]

            layout.startAnimation(AnimationUtils.loadAnimation(this, animId))
            layout.animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    layout.visibility = View.VISIBLE
                }
            })
        }

        /**
         * 3. Zoom-out images
         */
        updateStudentPicturesScale(Zoom.ZoomedOut)

        /**
         * 4. Hide title and comment
         */
        val topMovement: Float = resources.getDimension(R.dimen.header_height)

        binding.sceneStudentTitleLayout.animate().y(-topMovement).duration = 0L
        binding.sceneStudentCommentLayout.visibility = View.INVISIBLE
    }

    companion object {
        const val TAG = "StudentSceneActivity"
    }
}