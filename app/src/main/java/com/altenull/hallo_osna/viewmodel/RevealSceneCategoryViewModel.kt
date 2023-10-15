package com.altenull.hallo_osna.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.altenull.hallo_osna.data.Picture

class RevealSceneCategoryViewModel : ViewModel() {
    private val _sceneCategoryType = MutableLiveData<Picture.CategoryType?>(null)
    val sceneCategoryType: LiveData<Picture.CategoryType?> get() = _sceneCategoryType

    fun revealSceneCategory(categoryType: Picture.CategoryType) {
        _sceneCategoryType.value = categoryType
    }
}