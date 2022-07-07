package com.panshen.gridcolorpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.panshen.gridcolorpicker.ColorUtils.retrieveColorsFromColorIds
import com.panshen.gridcolorpicker.ColorUtils.setAlphaComponent
import com.panshen.gridcolorpicker.model.PickerConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GridColorPicker : LinearLayout, AlphaView.OnAlphaSelectListener {

    private val shadowPadding = 5.dp
    private val shadowRadius = 5.dpf
    private var shadowColor = Color.parseColor("#33000000")
    private val roundRadius = 10.dpf
    private lateinit var paletteView: PaletteView
    private lateinit var alphaView: AlphaView
    private lateinit var cardPaint: Paint
    private var cardRect = RectF()
    private val bottomPadding = 15.dp
    private lateinit var alphaViewLabel: AppCompatTextView
    private val paletteSpacing = 10.dp
    private var pickerConfig = PickerConfig()

    var onColorChanged: ((String) -> Unit)? = null
    var afterColorChanged: ((String) -> Unit)? = null
    var onColorSelectListener: OnColorSelectListener? = null
    var color: String? = null
        private set

    private var _onColorChangedFlow: MutableStateFlow<String>? = null
    val onColorChangedFlow by lazy {
        _onColorChangedFlow = MutableStateFlow(color ?: "")
        _onColorChangedFlow!!.asStateFlow()
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        val attrs = context.obtainStyledAttributes(attributes, R.styleable.GridColorPicker)
        val row = attrs.getInteger(R.styleable.GridColorPicker_gcp_row, -1)
        if (row != -1) {
            pickerConfig = pickerConfig.copy(row = row)
        }

        val colorSchemeId = attrs.getResourceId(R.styleable.GridColorPicker_gcp_colorScheme, -1)
        val colorSchemeArray =
            if (colorSchemeId != -1) resources.getIntArray(colorSchemeId) else null
        pickerConfig = if (colorSchemeArray != null) {
            pickerConfig.copy(colorScheme = colorSchemeArray.toList() as ArrayList<Int>)
        } else {
            pickerConfig.copy(colorScheme = retrieveColorsFromColorIds(context, pickerConfig.colorScheme))
        }

        val showAlphaView = attrs.getBoolean(R.styleable.GridColorPicker_gcp_showAlphaView, true)
        if (!showAlphaView) pickerConfig = pickerConfig.copy(showAlphaView = showAlphaView)

        val drawCard = attrs.getBoolean(R.styleable.GridColorPicker_gcp_drawCard, true)
        if (!drawCard) pickerConfig = pickerConfig.copy(drawCard = drawCard)

        val showAlphaViewLabel =
            attrs.getBoolean(R.styleable.GridColorPicker_gcp_showAlphaViewLabel, true)
        if (!showAlphaViewLabel) pickerConfig =
            pickerConfig.copy(showAlphaViewLabel = showAlphaViewLabel)

        val alphaViewLabelText = attrs.getString(R.styleable.GridColorPicker_gcp_alphaViewLabelText)
        if (alphaViewLabelText != null) pickerConfig =
            pickerConfig.copy(alphaViewLabelText = alphaViewLabelText)

        val selectorColorRes =
            attrs.getResourceId(R.styleable.GridColorPicker_gcp_selectorColor, R.color.selector)
        if (selectorColorRes != R.color.selector) pickerConfig =
            pickerConfig.copy(selectorColorRes = selectorColorRes)

        val backgroundColorRes =
            attrs.getResourceId(R.styleable.GridColorPicker_gcp_cardColor, R.color.bg_color)
        if (backgroundColorRes != R.color.bg_color) pickerConfig =
            pickerConfig.copy(cardColorRes = backgroundColorRes)

        val alphaViewLabelColorRes =
            attrs.getResourceId(R.styleable.GridColorPicker_gcp_alphaViewLabelColor, R.color.alpha_label_color)
        if (alphaViewLabelColorRes != R.color.alpha_label_color) pickerConfig =
            pickerConfig.copy(alphaViewLabelColorRes = alphaViewLabelColorRes)

        attrs.recycle()
        correctParams()
        init()
    }

    constructor(context: Context, config: PickerConfig) : super(context) {
        pickerConfig =
            config.copy(colorScheme = retrieveColorsFromColorIds(context, config.colorScheme))
        correctParams()
        init()
    }

    private fun correctParams() {
        if (!pickerConfig.showAlphaView && pickerConfig.showAlphaViewLabel) {
            pickerConfig = pickerConfig.copy(showAlphaViewLabel = false)
        }
    }

    fun checkColor(hexColor: String): Boolean {
        pickerConfig = pickerConfig.copy(checkedColor = hexColor)
        val success = paletteView.checkColor(hexColor)
        if (success && pickerConfig.showAlphaView) {
            alphaView.color = Color.parseColor(hexColor)
        }
        color = hexColor
        return success
    }

    private fun init() {
        orientation = VERTICAL
        setWillNotDraw(!pickerConfig.drawCard)
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        paletteView = PaletteView(context, pickerConfig).also {
            addView(it)
            it.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also { ii ->
                    ii.gravity = Gravity.CENTER_HORIZONTAL
                }
            it.onColorSelectListener = listener
            it.colorBlockCreated = {
                checkColor(pickerConfig.checkedColor)
            }
        }

        if (pickerConfig.showAlphaViewLabel) {
            alphaViewLabel = AppCompatTextView(context).also {
                addView(it)
                it.layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also { ii ->
                        ii.gravity = Gravity.START
                    }

                it.text = pickerConfig.alphaViewLabelText
                it.textSize = 15f
                it.setTextColor(ContextCompat.getColor(context, pickerConfig.alphaViewLabelColorRes))
            }
        }

        if (pickerConfig.showAlphaView) {
            alphaView = AlphaView(context).also {
                addView(it)
                it.layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also { ii ->
                        ii.gravity = Gravity.CENTER_HORIZONTAL
                    }
                it.onAlphaSelectListener = this
            }
        }

        if (pickerConfig.drawCard) {
            cardPaint = Paint()
            cardPaint.strokeWidth = shadowPadding.toFloat()
            cardPaint.color = ContextCompat.getColor(context, pickerConfig.cardColorRes)
            cardPaint.isAntiAlias = true
        }

    }

    private val listener = object : OnColorSelectListener {
        override fun onColorChanged(color: String) {
            var alpha = AlphaView.MAX_ALPHA
            if (this@GridColorPicker::alphaView.isInitialized) {
                alphaView.color = Color.parseColor(color)
                alpha = alphaView.alphaValue
            }
            val colorWithAlpha = setAlphaComponent(alpha, color)
            onColorSelectListener?.onColorChanged(colorWithAlpha)
            onColorChanged?.invoke(colorWithAlpha)
            _onColorChangedFlow?.update { colorWithAlpha }
            this@GridColorPicker.color = colorWithAlpha
        }

        override fun afterColorChanged(color: String) {
            var alpha = AlphaView.MAX_ALPHA
            if (this@GridColorPicker::alphaView.isInitialized) {
                alphaView.color = Color.parseColor(color)
                alpha = alphaView.alphaValue
            }
            val colorWithAlpha = setAlphaComponent(alpha, color)
            onColorSelectListener?.afterColorChanged(colorWithAlpha)
            afterColorChanged?.invoke(colorWithAlpha)
            this@GridColorPicker.color = colorWithAlpha
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpacing = shadowPadding * 4

        paletteView.updateLayoutParams<LayoutParams> {
            this.width = width - widthSpacing
            this.setMargins(0, shadowPadding * 2, 0, 0)
            paletteView.autoMeasure()
        }

        if (pickerConfig.showAlphaView) {
            alphaView.updateLayoutParams<LayoutParams> {
                this.width = width - widthSpacing - paletteSpacing
                this.height = 33.dp
                this.setMargins(0, 5.dp, 0, 0)
                alphaView.autoMeasure()
            }
        }

        if (pickerConfig.showAlphaViewLabel) {
            alphaViewLabel.updateLayoutParams<LayoutParams> {
                this.setMargins(shadowPadding * 3, 5.dp, 0, 0)
                alphaViewLabel.autoMeasure()
            }
        }

        val alphaViewLabelHeight = if (pickerConfig.showAlphaViewLabel) {
            alphaViewLabel.measuredHeight
        } else {
            0
        }

        val alphaViewHeight = if (pickerConfig.showAlphaView) {
            alphaView.measuredHeight
        } else {
            0
        }

        val bottomSpacing = if (pickerConfig.showAlphaView) bottomPadding else 0

        val height =
            paletteView.measuredHeight + alphaViewLabelHeight + alphaViewHeight + bottomSpacing
        setMeasuredDimension(measuredWidth, height + shadowPadding * 4)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cardRect.set(shadowPadding.toFloat(), shadowPadding.toFloat(), w - shadowPadding.toFloat(), h - shadowPadding.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        cardPaint.setShadowLayer(shadowRadius, 0f, 0f, shadowColor)
        canvas.drawRoundRect(cardRect, roundRadius, roundRadius, cardPaint)
        cardPaint.clearShadowLayer()
    }

    override fun onAlphaChanged(color: String) {
        onColorSelectListener?.onColorChanged(color)
        onColorChanged?.invoke(color)
        this.color = color
    }

    override fun afterAlphaChanged(color: String) {
        onColorSelectListener?.afterColorChanged(color)
        afterColorChanged?.invoke(color)
        this.color = color
    }

    fun View.autoMeasure() {
        measure(
            this.defaultWidthMeasureSpec(parentView = this@GridColorPicker), this.defaultHeightMeasureSpec(parentView = this@GridColorPicker)
        )
    }
}