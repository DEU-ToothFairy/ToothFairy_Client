package com.example.toothfairy.viewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toothfairy.dto.ResponseDto.SideFaceResDto
import com.example.toothfairy.model.repository.FaceDetectRepository
import com.example.toothfairy.model.repository.SideFaceDetectRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SideFaceViewModel: ViewModel() {
    val sideDetectResult = MutableLiveData<SideFaceResDto.DetectResult>()

    /**
     * 옆면 얼굴 인식 결과를 받아오는 메소드
     * (사진이 아니라 점수)
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun getDetectResult() {
        GlobalScope.launch(Dispatchers.IO){
            val response = SideFaceDetectRepository.getDetectResult()

            response?.let {
                if(it.isSuccessful){
                    sideDetectResult.postValue(it.body())
                } else {
                    Log.i("옆면 인식 실패","")
                }
            }
        }
    }
}