package com.example.toothfairy.viewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toothfairy.dto.ResponseDto.FaceResDto
import com.example.toothfairy.model.repository.FaceDetectRepository
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs
import kotlin.math.atan2

class FaceDetectViewModel: ViewModel() {
    val faceDetectPath = MutableLiveData<String>()
    var face:Face? = null

    val sideDetectResult = MutableLiveData<Bitmap>()
    val toothBrushResult = MutableLiveData<Bitmap>()

    /**
     *
     * Atan2(x,y)*180/PI
     * 라인의 각도를 구하는 것이 핵심
     *
     * 1. 하악형
     *   - 눈은 일자, 아래턱(하악)이 좌측 혹은 우측으로 틀어진 경우
     *   - 아래턱 이외의 부위에서는 아직 비대칭이 뚜렷하게 나타나지 않은 상태
     *   - 타입 1이 방치되는 경우, 하악의 틀어짐이 더 심해지면서 타입 2로 심화됨
     *
     *   기울기 : 음수이면 오른쪽이 내려간 것(좌측이 올라감), 양수이면 오른쪽이 올라간 것(좌측이 내려감)
     *         : 눈의 기울기 소숫점 2번 째 자리까지 해서 백분율로 표현하기 (0.5를 최대치로 지정하여 비율 계산)
     *         : 후면 카메라로 하면 부호가 반대가 되므로 조심 (전면 카메라로 찍을 것을 권장)
     *
     * 2. 측두골형
     *   - 가장 흔한 안면 비대칭 유형
     *   - 위턱(상악)과 아래턱(하악)이 같은 방향으로 틀어짐
     *   - 아래턱이 틀어진 방향으로 눈과 광대가 주저앉으면서 얼굴이 찌그러져 보이는 유형
     *   - 아래턱이 틀어진 방향으로 코가 휘는 경우가 많음
     *   - 노화가 진행되면 찌그러진 방향으로 눈가 주름, 팔자 주름 등도 더 깊이 유발 됨
     *
     * 3. 접형골형
     *   - 가장 심한 안면 비대칭 유형
     *   - 타입2에서 비대칭이 더욱 진행되어 측두골과 접형골 등 두개골의 변위 진행
     *   - 아래턱과 반대방향으로 위턱이 틀어지고 코가 휨
     *   - 안면뼈와 두개골의 벼위가 가장 많이 진행된 유형으로 치료기간이 길어짐
     */

    fun detectAsymmetry(): FaceResDto.DetectResult?{
        return this.face?.let {
            // 왼쪽 눈, 오른쪽 눈 PointF() 객체
            val leftEye = it.getContour(FaceContour.LEFT_EYE)
            val rightEye = it.getContour(FaceContour.RIGHT_EYE)
            val lip = it.getContour(FaceContour.UPPER_LIP_TOP)

            // 해당 if문의 결과를 리턴(FaceResDto.DetectResult)
            if(leftEye != null && rightEye != null && lip != null){
                // Atan2(x,y) * 180 / PI
                val xDis = leftEye.points[0].x.minus(rightEye.points[8].x).toDouble() // 양쪽 눈의 x 차이
                val yDis = leftEye.points[0].y.minus(rightEye.points[8].y).toDouble() // 양쪽 눈의 y 차이
                
                val eyeIncline = yDis / xDis // 눈 기울기
                var eyeDegree = (atan2(abs(yDis), abs(xDis)) * 180 / Math.PI)
                // 20 * 100 // 눈 각도(degree)

                val lipXdis = lip.points[0].x.minus(lip.points[10].x).toDouble() // 양쪽 입술의 x 차이
                val lipYdis = lip.points[0].y.minus(lip.points[10].y).toDouble() // 양쪽 입술의 y 차이

                val lipIncline = lipYdis / lipXdis // 입술 기울기
                var lipDegree = (atan2(abs(lipYdis), abs(lipXdis)) * 180 / Math.PI)
                /// 20 * 100 // 입술 각도(degree)

                Log.i("눈 결과","기울기 : $eyeIncline, 각도(비대칭 정도) : $eyeDegree") // 음수면 오른 쪽이 내려간 것
                Log.i("입 결과","기울기 : $lipIncline, 각도(비대칭 정도) : $lipDegree") // 음수면 오른 쪽이 내려간 것

                // 기울기가 양수(왼쪽으로 기운 경우)인 경우 -1, 직선인 경우 0, 음수(오른쪽으로 기운 경우)인 경우 1
                val eyeSlope = if(eyeIncline > 0) -1 else if (eyeIncline == 0.0) 0 else 1
                val lipSlope = if(lipIncline > 0) -1 else if (lipIncline == 0.0) 0 else 1

                // 3도 이하로 조금 비틀어진건 직선으로 취급
                //eyeDegree = if(abs(eyeDegree) <= 3) 0.0 else eyeDegree
                //lipDegree = if(abs(lipDegree) <= 3) 0.0 else lipDegree

                Log.d("프로그레스", "eye = ${(eyeDegree / 13 * 100)} lip = ${(lipDegree / 13 * 100)} 합 = ${(eyeDegree / 13 * 100) + (lipDegree / 13 * 100)}")
                val result = (eyeDegree / 30 * 100) + (lipDegree / 30 * 100) // 측정 결과 수치
                
                // 서로 같은 방향(직선 or 사선)으로 틀어진 경우 (Type 3. 접형골형)
                val type =
                    if(result.toInt() / 10 <= 2) "정상"
                    else if(eyeSlope == lipSlope){
                    if(eyeDegree == 0.0 && lipDegree == 0.0){ // 직선이라서 같은 방향인 경우
                        "정상"
                    } else if(eyeDegree == 0.0 && lipDegree != 0.0){ // 입술이 비틀어진 경우
                        "하악형 비대칭"
                    } else if(eyeDegree != 0.0 && lipDegree == 0.0){ // 눈이 비틀어진 경우
                        // Type 1. 하악형 의심 (눈이 비대칭이거나 턱이 비틀어졌을 경우임)
                        "하악형 비대칭 의심"
                    } else{ // 둘 다 같은 방향으로 비틀어진 경우
                        "접형골형 비대칭"
                    }
                } else { //서로 다른 방향으로 비틀어진 경우
                    // Type 2. 측두골형
                    "측두골형 비대칭"
                }

                FaceResDto.DetectResult(
                    result,
                    type,
                    eyeSlope,
                    eyeDegree,
                    lipSlope,
                    lipDegree
                )
            } else null
        }
    }

    /**
     * 옆면 얼굴 감지 메소드
     */
    fun detectSideFace(userId:String, bitmap: Bitmap){
        val faceCall = FaceDetectRepository.sendBitmap(userId, bitmap)
        faceCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.body()?.let {
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
                    sideDetectResult.postValue(bitmap)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Log.d("이미지 인식 실패","$t")
            }
        })
    }

    /**
     * 칫솔모 교체 판별 메소드
     */
    fun detectToothBrush(userId:String, bitmap: Bitmap){
        val faceCall = FaceDetectRepository.sendToothBrush(userId, bitmap)
        faceCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.body()?.let {
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
                    toothBrushResult.postValue(bitmap)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Log.d("이미지 인식 실패","$t")
            }
        })
    }
}