package com.altenull.hallo_osna.delegate

import com.altenull.hallo_osna.R
import com.altenull.hallo_osna.data.Picture

interface CategoryTypeDelegation {
    fun getColorByCategoryType(categoryType: Picture.CategoryType): Int

    fun getLightColorByCategoryType(categoryType: Picture.CategoryType): Int

    fun getSymbolByCategoryType(categoryType: Picture.CategoryType): Int
}

class CategoryTypeDelegate : CategoryTypeDelegation {
    override fun getColorByCategoryType(categoryType: Picture.CategoryType): Int =
        colorMap.getValue(categoryType)

    override fun getLightColorByCategoryType(categoryType: Picture.CategoryType): Int =
        lightColorMap.getValue(categoryType)

    override fun getSymbolByCategoryType(categoryType: Picture.CategoryType): Int =
        symbolMap.getValue(categoryType)

    companion object {
        val colorMap: Map<Picture.CategoryType, Int> = mapOf(
            Picture.CategoryType.FOOD to R.color.category_food,
            Picture.CategoryType.DAILY to R.color.category_daily,
            Picture.CategoryType.DORMITORY to R.color.category_dormitory,
            Picture.CategoryType.UNIVERSITY to R.color.category_university,
            Picture.CategoryType.TRAVEL to R.color.category_travel,
            Picture.CategoryType.LEISURE to R.color.category_leisure
        )
        val lightColorMap: Map<Picture.CategoryType, Int> = mapOf(
            Picture.CategoryType.FOOD to R.color.category_food_light,
            Picture.CategoryType.DAILY to R.color.category_daily_light,
            Picture.CategoryType.DORMITORY to R.color.category_dormitory_light,
            Picture.CategoryType.UNIVERSITY to R.color.category_university_light,
            Picture.CategoryType.TRAVEL to R.color.category_travel_light,
            Picture.CategoryType.LEISURE to R.color.category_leisure_light
        )
        val symbolMap: Map<Picture.CategoryType, Int> = mapOf(
            Picture.CategoryType.FOOD to R.drawable.category_food,
            Picture.CategoryType.DAILY to R.drawable.category_daily,
            Picture.CategoryType.DORMITORY to R.drawable.category_dormitory,
            Picture.CategoryType.UNIVERSITY to R.drawable.category_university,
            Picture.CategoryType.TRAVEL to R.drawable.category_travel,
            Picture.CategoryType.LEISURE to R.drawable.category_leisure
        )
    }
}
