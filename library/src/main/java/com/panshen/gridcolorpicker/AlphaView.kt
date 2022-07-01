package com.panshen.gridcolorpicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.ColorUtils
import com.panshen.gridcolorpicker.ColorUtils.toHexEncodingWithAlpha

internal class AlphaView : View {

    companion object {
        /**
         * 100% opaque
         */
        const val MAX_ALPHA = 0xFF
    }

    private val trackPaint = Paint()
    private val trackRect = RectF()
    private val trackRectRound = 10.dpf
    private val trackMovableArea = RectF()

    private val trackStrokePaint = Paint()
    private val trackStrokeRect = RectF()
    private val trackStrokeRound = trackRectRound + 1.dpf

    private lateinit var alphaTileShader: BitmapShader
    private lateinit var gradientColorShader: Shader
    private val xFerMode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    private val sliderPaint = Paint()
    private val sliderRect = RectF()
    private var sliderWidth = 0f
    private var sliderMaxLeft = 0f
    private var sliderPadding = 4.dpf
    private var sliderColor = Color.BLACK
    private val sliderRound = 8.dpf

    private var pointerX = 0
    private var defaultHeight = 35.dp

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        trackPaint.isAntiAlias = true

        val bitmap = BitmapFactory.decodeStream(resources.assets.open("opacity.png"))
        alphaTileShader = BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)

        sliderPaint.isAntiAlias = true
        sliderPaint.strokeWidth = 2.dpf

        trackStrokePaint.isAntiAlias = true
        trackStrokePaint.style = Paint.Style.STROKE
        trackStrokePaint.strokeWidth = 1.dpf
        trackStrokePaint.color = color
    }

    /**
     * [0-255]
     */
    var alphaValue = MAX_ALPHA
        private set

    var onAlphaSelectListener: OnAlphaSelectListener? = null

    var color: Int = Color.BLACK
        set(value) {
            alphaValue = Color.alpha(value)
            field = ColorUtils.setAlphaComponent(value, 255)
            if (field == Color.WHITE) {
                trackStrokePaint.color = Color.BLACK
                sliderColor = Color.BLACK
            } else {
                trackStrokePaint.color = field
                sliderColor = field
            }
            createLinearShader(width, height)
            resetSlider()
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, defaultHeight)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        pointerX = event.x.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                restrictX()
                moveSlider()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                restrictX()
                moveSlider()
                calculateAlpha {
                    val color = toHexEncodingWithAlpha(it)
                    onAlphaSelectListener?.onAlphaChanged(color)
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                restrictX()
                calculateAlpha {
                    val color = toHexEncodingWithAlpha(it)
                    onAlphaSelectListener?.afterAlphaChanged(color)
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * Restricts the maximum distance the slider can slide to the left and right sides
     */
    private fun restrictX() {
        val adjust = sliderWidth / 2
        val padding = sliderPadding / 2
        if (pointerX < sliderPadding + adjust + padding) {
            pointerX = (sliderPadding.toInt() + adjust + padding).toInt()
        } else if (pointerX > sliderMaxLeft + adjust - padding) {
            pointerX = (sliderMaxLeft.toInt() + adjust - padding).toInt()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTrack(canvas)
        drawSlider(canvas)
    }

    private fun drawTrack(canvas: Canvas) {
        trackPaint.shader = alphaTileShader
        canvas.drawRoundRect(trackRect, trackRectRound, trackRectRound, trackPaint)
        trackPaint.shader = gradientColorShader
        canvas.drawRoundRect(trackRect, trackRectRound, trackRectRound, trackPaint)

        trackPaint.xfermode = xFerMode
        canvas.drawPaint(trackPaint)
        trackPaint.xfermode = null

        canvas.drawRoundRect(trackStrokeRect, trackStrokeRound, trackStrokeRound, trackStrokePaint)
    }

    private fun drawSlider(canvas: Canvas) {
        sliderPaint.style = Paint.Style.FILL
        sliderPaint.color = sliderColor
        canvas.drawRoundRect(sliderRect, sliderRound, sliderRound, sliderPaint)

        sliderPaint.style = Paint.Style.STROKE
        sliderPaint.color = Color.WHITE
        canvas.drawRoundRect(sliderRect, sliderRound, sliderRound, sliderPaint)
    }

    private fun resetSlider() {
        if (trackMovableArea.width() == 0f) return

        val percent = (alphaValue.toFloat() / MAX_ALPHA.toFloat())
        pointerX = (trackMovableArea.width() * percent).toInt()
        sliderRect.offsetTo(pointerX.toFloat() + sliderPadding, sliderRect.top)
    }

    private fun moveSlider() {
        val adjust = sliderWidth / 2
        val padding = sliderPadding / 2
        sliderRect.set(pointerX - adjust - padding, sliderPadding, pointerX + adjust - padding, sliderWidth + sliderPadding)
        invalidate()
    }

    private fun calculateAlpha(callback: (Int) -> Unit) {
        val sliderStart = sliderRect.centerX() - trackMovableArea.left
        val alphaFactor = sliderStart / trackMovableArea.width()
        alphaValue = (MAX_ALPHA * alphaFactor).toInt()

        val colorWithAlpha = ColorUtils.setAlphaComponent(color, alphaValue)
        callback.invoke(colorWithAlpha)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        trackRect.set(0f, 0f, w.toFloat(), h.toFloat())
        trackStrokeRect.set(1.dpf, 1.dpf, w.toFloat() - 1.dpf, h.toFloat() - 1.dpf)
        sliderWidth = h - sliderPadding * 2
        sliderMaxLeft = w - sliderWidth

        sliderRect.set(
            sliderMaxLeft - sliderPadding, sliderPadding, sliderMaxLeft + sliderWidth - sliderPadding, sliderWidth + sliderPadding
        )

        trackMovableArea.set(sliderWidth / 2 + sliderPadding, 0f, w.toFloat() - sliderWidth / 2 - sliderPadding, h.toFloat())
        createLinearShader(w, h)

        resetSlider()
        invalidate()
    }

    private fun createLinearShader(w: Int, h: Int) {
        gradientColorShader = LinearGradient(
            -(w.toFloat() / 20).dpf, (h / 2).toFloat(), w.toFloat(), (h / 2).toFloat(), Color.TRANSPARENT, color, Shader.TileMode.CLAMP
        )
    }

    interface OnAlphaSelectListener {
        fun onAlphaChanged(color: String)
        fun afterAlphaChanged(color: String)
    }
}