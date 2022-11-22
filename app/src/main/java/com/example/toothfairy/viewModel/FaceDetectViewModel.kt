package com.example.toothfairy.viewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun check(){
        this.face?.let {
            // 왼쪽 눈, 오른쪽 눈 PointF() 객체
            val leftEye = it.getContour(FaceContour.LEFT_EYE)
            val rightEye = it.getContour(FaceContour.RIGHT_EYE)
            val lip = it.getContour(FaceContour.UPPER_LIP_TOP)

            if(leftEye != null && rightEye != null && lip != null){
                // Atan2(x,y)*180/PI
                val xDis = leftEye.points[0].x.minus(rightEye.points[0].x).toDouble()
                val yDis = leftEye.points[0].y.minus(rightEye.points[8].y).toDouble()
                // val result = atan2(abs(point2.y-point1.y).toDouble(), abs(point2.x-point1.x).toDouble())
                // 눈 기울기
                val eyeIncline = leftEye.points[0].y.minus(rightEye.points[8].y) / leftEye.points[0].x.minus(rightEye.points[0].x)
                // 눈 각도
                val eyeDegree = atan2(abs(yDis), abs(xDis)) * 180 / Math.PI // degree 각도

                val leftLip = lip.points[0]     // 입술 왼쪽 끝 점
                val rightLip = lip.points[10]   // 입술 오른쪽 끝 점
                val lipXdis = leftLip.x.minus(rightLip.x)
                val lipYdis = leftLip.y.minus(rightLip.y)

                // 입술 기울기
                val lipIncline = leftLip.y.minus(rightLip.y) / leftLip.x.minus(rightLip.x)
                // 입술 각도
                val lipDegree = atan2(abs(lipYdis), abs(lipXdis)) * 180 / Math.PI // degree 각도

                Log.i("양쪽 눈의 기울기","$eyeIncline") // 음수면 오른 쪽이 내려간 것
                Log.i("눈 비대칭 정도 : ", "${eyeDegree / 20 * 100}") // 20을 임의의 최댓 값으로 지정

                Log.i("양쪽 입술의 기울기","$lipIncline")
                Log.i("입술 비대칭 정도 : ", "${lipDegree / 20 * 100}")

                //setSegmentedProgress((eyeDegree / 13 * 100) + (lipDegree / 13 * 100))
            }
            //checkAsymmetry()
        }
    }

    /**
     * 옆면 얼굴 감지 메소드
     */
    fun detectSideFace(userId:String, bitmap: Bitmap){
        Log.d("비트맵 가로", "${bitmap.width}")
        Log.d("비트맵 세로", "${bitmap.height}")


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

}