package com.altenull.hallo_osna

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.altenull.hallo_osna.adapter.StudentsAdapter
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Student
import com.altenull.hallo_osna.data.StudentFrame
import com.altenull.hallo_osna.databinding.FragmentMainStoryBinding
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class MainStoryFragment : Fragment() {
    private lateinit var binding: FragmentMainStoryBinding
    private val students by lazy {
        val studentsFromBundle = if (Build.VERSION.SDK_INT >= 33) {
            requireArguments().getParcelableArrayList(
                Constants.BundleKeys.Students,
                Student::class.java
            )
        } else {
            requireArguments().getParcelableArrayList<Student>(Constants.BundleKeys.Students)
        }

        studentsFromBundle?.toList() ?: emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        organizeStudentViewpager(view)
        startStudentViewpagerAnim()
    }


    private fun organizeStudentViewpager(view: View) {
        /**
         * MainStoryFragment > StudentsAdapter > StudentGridAdapter
         */
        binding.studentViewpager.adapter = StudentsAdapter(
            view.context,
            students.chunked(Constants.StudentGrid.Size),
            students,
            getStudentFrame()
        ) {
            (requireActivity() as MainActivity).startRevealSceneStudent()
        }
    }

    private fun startStudentViewpagerAnim() {
        YoYo.with(Techniques.FadeIn)
            .duration(Constants.AnimDuration.MainModeReveal)
            .playOn(binding.studentViewpager)
    }

    private fun getStudentFrame(): StudentFrame {
        val (widthPixels, realHeightPixels) = resources.displayMetrics.run {
            /**
             * heightPixels value is not real screen height.
             * It excludes the height of navigation bar
             */
            val realHeightPixels = heightPixels - getStatusBarHeight()

            Pair(widthPixels, realHeightPixels)
        }

        val frameY = resources.getDimension(R.dimen.student_frame_y).toInt()
        val frameHeight =
            resources.displayMetrics.run {
                ((realHeightPixels - resources.getDimension(R.dimen.header_height)) / Constants.StudentGrid.Rows).toInt()
            }

        /**
         * Expected image aspect-ratio is 1 x 1.4
         * A minimum value has been set, just in case calculating 'frameX' is too small.
         */
        val innerImageHeightPx = frameHeight - (frameY * 2)
        val innerImageWidthPx = innerImageHeightPx * 1.4
        val frameX = ((widthPixels - innerImageWidthPx) / 2).toInt()
            .coerceAtLeast(resources.getDimensionPixelSize(R.dimen.student_frame_x))

        return StudentFrame(
            frameX,
            frameY,
            frameHeight,
        )
    }

    private fun getStatusBarHeight(): Int {
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resId > 0) resources.getDimensionPixelSize(resId) else 0
    }

    companion object {
        const val TAG = "MainStoryFragment"
    }
}