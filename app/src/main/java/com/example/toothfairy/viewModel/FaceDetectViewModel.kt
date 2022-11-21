package com.example.toothfairy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.face.Face

class FaceDetectViewModel: ViewModel() {
    val faceDetectPath = MutableLiveData<String>()
    var face:Face? = null
}