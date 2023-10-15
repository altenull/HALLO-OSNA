package com.altenull.hallo_osna.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class MainMode {
    STORY,
    CATEGORY
}

class MainModeViewModel : ViewModel() {
    private val _mainMode = MutableLiveData(MainMode.STORY)
    val mainMode: LiveData<MainMode> get() = _mainMode

    fun switchMainMode() {
        _mainMode.value = when (_mainMode.value) {
            MainMode.STORY -> MainMode.CATEGORY
            MainMode.CATEGORY -> MainMode.STORY
            else -> MainMode.STORY
        }
    }
}