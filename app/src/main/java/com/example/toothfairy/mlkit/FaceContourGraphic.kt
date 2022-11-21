package com.example.toothfairy.mlkit


import android.graphics.*
import androidx.annotation.ColorInt
import com.example.toothfairy.camerax.GraphicOverlay
import com.example.toothfairy.viewModel.FaceDetectViewModel
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour


class FaceContourGraphic(
    overlay: GraphicOverlay,
    private val face: Face,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    private val facePositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint
    private lateinit var faceVM:FaceDetectViewModel

    init {
        val selectedColor = Color.WHITE

        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor

        idPaint = Paint()
        idPaint.color = selectedColor
        idPaint.textSize = ID_TEXT_SIZE

        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH
    }

    private fun Canvas.drawFace(facePosition: Int, @ColorInt selectedColor: Int) {
        val contour = face.getContour(facePosition)
        val path = Path()
        contour?.points?.forEachIndexed { index, pointF ->
            if (index == 0) {
                path.moveTo(
                    translateX(pointF.x),
                    translateY(pointF.y)
                )
            }
            path.lineTo(
                translateX(pointF.x),
                translateY(pointF.y)
            )
        }
        val paint = Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
        drawPath(path, paint)
    }

    /**
     * 양쪽 눈 끝을 잇는 메소드
     */
    private fun Canvas.drawFaceEyeLine(leftEyePosition: Int, rightEyePosition:Int, @ColorInt selectedColor: Int) {
        val leftContour = face.getContour(leftEyePosition)
        val rightContour = face.getContour(rightEyePosition)
        val path = Path()

        val leftX = leftContour?.points?.get(0)?.x
        val leftY = leftContour?.points?.get(0)?.y

        val rightX = rightContour?.points?.get(8)?.x
        val rightY = rightContour?.points?.get(8)?.y

        path.moveTo(
            translateX(leftX!!),
            translateY(leftY!!)
        )

        path.lineTo(
            translateX(rightX!!),
            translateY(rightY!!)
        )

        val paint = Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
        drawPath(path, paint)
    }

    private fun Canvas.drawLipLine(lipPosition: Int, @ColorInt selectedColor: Int){
        val lip = face.getContour(lipPosition)
        val path = Path()

        val leftX = lip?.points?.get(0)?.x
        val leftY = lip?.points?.get(0)?.y

        val rightX = lip?.points?.get(10)?.x
        val rightY = lip?.points?.get(10)?.y

        path.moveTo(
            translateX(leftX!!),
            translateY(leftY!!)
        )

        path.lineTo(
            translateX(rightX!!),
            translateY(rightY!!)
        )

        val paint = Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
        drawPath(path, paint)
    }

    override fun draw(canvas: Canvas?) {

        val rect = calculateRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            face.boundingBox
        )
        canvas?.drawRect(rect, boxPaint)

        val contours = face.allContours

        contours.forEach {
            it.points.forEach { point ->
                val px = translateX(point.x)
                val py = translateY(point.y)
                canvas?.drawCircle(px, py, FACE_POSITION_RADIUS, facePositionPaint)
            }
        }

        /**
         * drawFace에서 해당 부위의 점들을 선으로 연결해줄 수있음
         * 선 색상 설정 가능
         * 처음 얼굴은 파란색 선으로 다 찍고
         * 반전 시킨 점들은 빨간색 선으로 찍거나 하면 좋을듯
         * 
         * drawFace로 비대칭 판별 라인도 그을 수 있을 듯
         * drawFace(FACE(왼쪽 눈 시작 점, 오른쪽 눈 끝 점),Color.RED) -> 양 쪽 눈 끝을 잇는 선이 그려짐
         */
        // face
        canvas?.drawFace(FaceContour.FACE, Color.BLUE)

        // left eye (왼쪽 눈 부분)
        canvas?.drawFace(FaceContour.LEFT_EYEBROW_TOP, Color.RED)       // RED
        canvas?.drawFace(FaceContour.LEFT_EYE, Color.BLACK)             // BLACK
        canvas?.drawFace(FaceContour.LEFT_EYEBROW_BOTTOM, Color.CYAN)   // CYAN

        // right eye (오른쪽 눈 부분)
        canvas?.drawFace(FaceContour.RIGHT_EYE, Color.DKGRAY)           // DKGRAY
        canvas?.drawFace(FaceContour.RIGHT_EYEBROW_BOTTOM, Color.GRAY)  // GRAY
        canvas?.drawFace(FaceContour.RIGHT_EYEBROW_TOP, Color.GREEN)    // GREEN

        // nose (코 부분)
        canvas?.drawFace(FaceContour.NOSE_BOTTOM, Color.LTGRAY)         // LTGRAY
        canvas?.drawFace(FaceContour.NOSE_BRIDGE, Color.MAGENTA)        // MAGENTA

        // rip (입술 부분)
        canvas?.drawFace(FaceContour.LOWER_LIP_BOTTOM, Color.WHITE)     // WHITE
        canvas?.drawFace(FaceContour.LOWER_LIP_TOP, Color.YELLOW)       // YELLOW
        canvas?.drawFace(FaceContour.UPPER_LIP_BOTTOM, Color.GREEN)     // GREEN
        canvas?.drawFace(FaceContour.UPPER_LIP_TOP, Color.CYAN)         // CYAN
        
        // 눈 양 끝 점 잇기
        canvas?.drawFaceEyeLine(FaceContour.LEFT_EYE, FaceContour.RIGHT_EYE, Color.RED)
        canvas?.drawFaceEyeLine(FaceContour.LEFT_EYE, FaceContour.RIGHT_EYE, Color.RED)

        // 입술 라인 그리기
        canvas?.drawLipLine(FaceContour.UPPER_LIP_TOP, Color.RED)
    }

    companion object {
        // 특징점의 크기
        private const val FACE_POSITION_RADIUS = 6.0f
        private const val ID_TEXT_SIZE = 30.0f
        private const val BOX_STROKE_WIDTH = 5.0f
    }

}