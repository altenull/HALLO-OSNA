package com.altenull.hallo_osna

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Category
import com.altenull.hallo_osna.data.Picture
import com.altenull.hallo_osna.data.Student
import com.altenull.hallo_osna.databinding.FragmentMainCategoryBinding
import com.altenull.hallo_osna.delegate.CategoryTypeDelegate
import com.altenull.hallo_osna.delegate.CategoryTypeDelegation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainCategoryFragment : Fragment(), CategoryTypeDelegation by CategoryTypeDelegate() {
    private lateinit var binding: FragmentMainCategoryBinding
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
    private val picturesMap: Map<Picture.CategoryType, List<Picture>> by lazy {
        students.flatMap { it.pictures }.groupBy { it.categoryType }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        organizeCircleLayout(view)
        startCircleLayoutAnim()
    }

    /**
     * [Circular Positioning in ConstraintLayout]
     * @reference https://kalpeshchandora12.medium.com/circular-positioning-in-constraintlayout-9c4214889b35
     */
    private fun organizeCircleLayout(view: View) {
        val categoryTypes = Picture.CategoryType.values()
        val angle = 360 / categoryTypes.size

        for (i in categoryTypes.indices) {
            val categoryType = categoryTypes[i]
            val color = getColorByCategoryType(categoryType)
            val lightColor = getLightColorByCategoryType(categoryType)
            val symbol = getSymbolByCategoryType(categoryType)
            val pictures = picturesMap.getValue(categoryType)

            val imageButton = ImageButton(view.context)
            imageButton.id = View.generateViewId()
            imageButton.setBackgroundResource(symbol)

            val layoutParams = ConstraintLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.category_symbol_width),
                resources.getDimensionPixelSize(R.dimen.category_symbol_height)
            )
            layoutParams.circleRadius =
                resources.getDimensionPixelSize(R.dimen.category_circle_layout_radius)
            layoutParams.circleConstraint = R.id.category_invisible_button
            layoutParams.circleAngle = (i * angle).toFloat()

            imageButton.layoutParams = layoutParams
            imageButton.setOnClickListener {
                (requireActivity() as MainActivity).startRevealSceneCategory(categoryType)

                CoroutineScope(Dispatchers.Default).launch {
                    delay(Constants.AnimDuration.SceneReveal)

                    val intent = Intent(view.context, CategorySceneActivity::class.java)
                    intent.putExtra(
                        Constants.IntentKeys.Category,
                        Category(categoryType, color, lightColor, symbol, pictures)
                    )
                    startActivity(intent)
                }
            }

            binding.categoryCircleLayout.addView(imageButton)
        }
    }

    private fun startCircleLayoutAnim() {
        YoYo.with(Techniques.RollIn)
            .duration(Constants.AnimDuration.MainModeReveal)
            .interpolate(DecelerateInterpolator(2.0F))
            .playOn(binding.categoryCircleLayout)
    }

    companion object {
        const val TAG = "MainCategoryFragment"
    }
}