package com.altenull.hallo_osna.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class Zoom {
    ZoomedIn,
    ZoomedOut
}

class ZoomViewModel : ViewModel() {
    private val _zoom = MutableLiveData(Zoom.ZoomedOut)
    val zoom: LiveData<Zoom> get() = _zoom

    fun switchZoom() {
        _zoom.value = when (_zoom.value) {
            Zoom.ZoomedIn -> Zoom.ZoomedOut
            Zoom.ZoomedOut -> Zoom.ZoomedIn
            else -> Zoom.ZoomedOut
        }
    }
}