package com.altenull.hallo_osna.adapter

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import coil.load
import com.altenull.hallo_osna.MainActivity
import com.altenull.hallo_osna.StudentSceneActivity
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Student
import com.altenull.hallo_osna.data.StudentFrame
import com.altenull.hallo_osna.databinding.ItemStudentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StudentGridAdapter(
    private val context: Context,
    private val studentsInGrid: List<Student>,
    private val allStudents: List<Student>,
    private val studentFrame: StudentFrame,
    private val startRevealSceneStudent: () -> Unit,
) :
    BaseAdapter() {
    private lateinit var binding: ItemStudentBinding

    override fun getCount(): Int = studentsInGrid.size

    override fun getItem(position: Int): Student = studentsInGrid[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = ItemStudentBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )

        val widthPixels = context.resources.displayMetrics.widthPixels
        val (frameX, frameY, frameHeight) = studentFrame
        val student = getItem(position)
        val isVisited = MainActivity.visitedStudentPrefs.isVisitedStudent(student.studentId)

        if (position == 0) {
            binding.root.updatePadding(
                0,
                context.resources.getDimension(com.altenull.hallo_osna.R.dimen.header_height)
                    .toInt(),
                0,
                0
            )
        }

        binding.studentEnglishName.text = student.englishName

        /**
         * Place triangle-backgrounded student name to left bottom corner
         */
        binding.studentEnglishName.x = frameX.toFloat()
        binding.studentEnglishName.y =
            (frameHeight - frameY - context.resources.getDimension(com.altenull.hallo_osna.R.dimen.student_english_name_label_height))

        binding.studentWallPicture.updateLayoutParams {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = frameHeight
        }
        binding.studentWallPicture.load(student.wallPictureUrl) {
            crossfade(true)
        }

        if (isVisited) {
            binding.studentWallPicture.clearColorFilter()
        } else {
            binding.studentWallPicture.colorFilter = getGrayscaleColorFilter()
        }

        binding.studentFrameLeft.updateLayoutParams {
            width = frameX
            height = frameHeight
        }

        binding.studentFrameRight.updateLayoutParams {
            width = frameX
            height = frameHeight
        }
        binding.studentFrameRight.x = (widthPixels - frameX).toFloat()

        binding.studentFrameTop.updateLayoutParams {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = frameY
        }

        binding.studentFrameBottom.updateLayoutParams {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = frameY
        }
        binding.studentFrameBottom.y = (frameHeight - frameY).toFloat()

        binding.root.setOnClickListener {
            if (!isVisited) {
                MainActivity.visitedStudentPrefs.visitStudent(student.studentId)
                it.findViewById<ImageView>(com.altenull.hallo_osna.R.id.student_wall_picture)
                    .clearColorFilter()
            }

            startRevealSceneStudent()
            CoroutineScope(Dispatchers.Default).launch {
                delay(Constants.AnimDuration.SceneReveal)

                val intent = Intent(context, StudentSceneActivity::class.java)
                intent.putParcelableArrayListExtra(
                    Constants.IntentKeys.Students,
                    ArrayList(allStudents)
                )
                intent.putExtra(Constants.IntentKeys.SelectedStudentId, student.studentId)

                startActivity(context, intent, null)
            }
        }

        return binding.root
    }


    private fun getGrayscaleColorFilter(): ColorMatrixColorFilter {
        return ColorMatrixColorFilter(ColorMatrix().apply {
            setSaturation(0F) // 0 means full grayscale, 1 means no change
        })
    }
}