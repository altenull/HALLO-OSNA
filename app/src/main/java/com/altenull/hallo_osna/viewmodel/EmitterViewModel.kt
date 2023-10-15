package com.altenull.hallo_osna.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmitterViewModel : ViewModel() {
    private val _isEmitted = MutableLiveData(false)
    val isEmitted: LiveData<Boolean> get() = _isEmitted

    fun emit() {
        _isEmitted.value = true
    }
}