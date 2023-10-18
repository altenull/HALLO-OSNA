package com.altenull.hallo_osna

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.altenull.hallo_osna.adapter.CategoryPicturesAdapter
import com.altenull.hallo_osna.constants.Constants
import com.altenull.hallo_osna.data.Category
import com.altenull.hallo_osna.databinding.ActivityCategorySceneBinding
import com.altenull.hallo_osna.delegate.CategoryDelegate
import com.altenull.hallo_osna.delegate.CategoryDelegation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class CategorySceneActivity : AppCompatActivity(),
    CategoryDelegation by CategoryDelegate() {
    private lateinit var binding: ActivityCategorySceneBinding
    private lateinit var category: Category
    private val categoryColor: Int by lazy {
        ContextCompat.getColor(this, category.color)
    }
    private val progressBarColorFilter: PorterDuffColorFilter by lazy {
        PorterDuffColorFilter(categoryColor, PorterDuff.Mode.SRC_IN)
    }
    private val progressBarLoadingAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.loading)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategorySceneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = getCategoryFromIntent(this)

        introduceCategory()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.view_exit)
    }

    private fun introduceCategory() {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, category.lightColor))

        /**
         * Set background color of top & bottom layouts to light color in case of overflowing image to layouts.
         */
        binding.sceneCategoryTopLayout.setBackgroundColor(ContextCompat.getColor(this, category.lightColor))
        binding.sceneCategoryBottomLayout.setBackgroundColor(ContextCompat.getColor(this, category.lightColor))

        binding.sceneCategorySymbol.load(category.symbol) {
            crossfade(true)
        }
        binding.sceneCategorySymbol.setOnClickListener {
            YoYo.with(Techniques.RubberBand)
                .duration(1000L)
                .playOn(it)
        }
        binding.sceneCategoryTitle.text = category.pictures[0].title
        binding.sceneCategoryComment.text =
            getString(R.string.picture_comment, category.pictures[0].comment)
        binding.sceneCategoryTitle.setShadowLayer(0.6F, 1.0F, 1.0F, categoryColor)
        binding.sceneCategoryComment.setShadowLayer(0.6F, 1.0F, 1.0F, categoryColor)

        binding.sceneCategoryPicturesViewpager.adapter =
            CategoryPicturesAdapter(
                category.pictures,
                progressBarColorFilter,
                progressBarLoadingAnim
            )
        binding.sceneCategoryPicturesViewpager.setPageTransformer { page, position ->
            page.rotationY = position * Constants.ViewPager.RotationY
        }
        binding.sceneCategoryPicturesViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val changedPicture = category.pictures[position]

                binding.sceneCategoryTitle.text = changedPicture.title
                binding.sceneCategoryComment.text =
                    getString(R.string.picture_comment, changedPicture.comment)
            }
        })
    }

    companion object {
        const val TAG = "CategorySceneActivity"
    }
}