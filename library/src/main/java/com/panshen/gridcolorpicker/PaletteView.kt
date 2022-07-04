package com.panshen.gridcolorpicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.panshen.gridcolorpicker.ColorUtils.removeAlphaFromColor
import com.panshen.gridcolorpicker.ColorUtils.setColorLightness
import com.panshen.gridcolorpicker.model.ColorBlock
import com.panshen.gridcolorpicker.model.PickerConfig
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
internal class PaletteView(context: Context, paletteConfig: PickerConfig) : View(context) {
    companion object {
        const val TAG: String = "PaletteView"
    }

    private val colorList = LinkedHashMap<Int, ArrayList<ColorBlock>>()
    private val colorScheme = arrayListOf<Int>()
    private val colorBlockPaint = Paint()
    private var colorUnitWidth = 0
    private val colorBlocksArea = Rect()

    private val selectorPaint = Paint()
    private val selectorWidth = 3.dp
    private var selectorColor = -1

    private val headColor = Color.parseColor("#ffffff")
    private var checkedColorUnit: ColorBlock? = null
    private var checkedColor = ""

    private val cornerRadius = 8.dpf
    private val innerPadding = 10.dp
    private var offset = 0
    private var row = 0

    private var leftTopBlockPath: Path? = null

    private val strokePaint = Paint()
    private val strokeColor = Color.parseColor("#4DBFBFBF")

    private var mPointerX = 0
    private var mPointerY = 0
    private val minSize = 2

    var onColorSelectListener: OnColorSelectListener? = null
    var colorBlockCreated: (() -> Unit)? = null

    init {
        paletteConfig.also {
            row = it.row
            selectorColor = ContextCompat.getColor(context, it.selectorColorRes)
            checkedColor = it.checkedColor
            colorScheme.addAll(it.colorScheme)
        }
        verifyConfigs()
        init()
    }

    @Throws(IllegalArgumentException::class)
    private fun verifyConfigs() {
        if (colorScheme.size < minSize) {
            throw IllegalArgumentException("colorScheme must be at least has $minSize elements")
        }
        if (row < minSize) {
            throw IllegalArgumentException("row must be at least $minSize")
        }
    }

    private fun init() {
        offset = (innerPadding / 2f).roundToInt()
        colorBlockPaint.isAntiAlias = true

        selectorPaint.strokeWidth = selectorWidth.toFloat()
        selectorPaint.color = selectorColor
        selectorPaint.style = Paint.Style.STROKE
        selectorPaint.isAntiAlias = true

        strokePaint.isAntiAlias = true
        strokePaint.strokeWidth = 1.dpf / 3
        strokePaint.color = strokeColor
        strokePaint.style = Paint.Style.STROKE

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        colorUnitWidth = (width / colorScheme.size)
        val contentHeight = colorUnitWidth * row + innerPadding / 2
        setMeasuredDimension(width, contentHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        colorUnitWidth = ((w - innerPadding) / colorScheme.size)
        generateColorBlocks()
        checkColor(checkedColor)
        colorBlockCreated?.invoke()
    }

    private fun generateColorBlocks() {

        fun makePath(rect: RectF, corners: FloatArray) = Path().also {
            it.addRoundRect(rect, corners, Path.Direction.CW)
        }

        colorScheme.forEachIndexed { columnIndex, baseColor ->
            val accLeft = columnIndex * colorUnitWidth
            val gradientColor = ArrayList<ColorBlock>()

            repeat(row) { i ->
                val color = if (i == 0) {
                    val l = 1.0f - columnIndex.toFloat() / colorScheme.size.toFloat()
                    setColorLightness(headColor, l)
                } else {
                    val light = i / row.toFloat()
                    setColorLightness(baseColor, light)
                }

                val accTop = i * colorUnitWidth
                var path: Path? = null

                val position = RectF(
                    (offset + accLeft.toFloat()), (offset + accTop.toFloat()), (offset + accLeft + colorUnitWidth.toFloat()), (offset + accTop + colorUnitWidth.toFloat())
                )

                if (columnIndex == 0) {
                    val corners = floatArrayOf(
                        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
                    )
                    if (i == 0) {//left top corner block
                        corners[0] = cornerRadius
                        corners[1] = cornerRadius
                        path = makePath(position, corners)
                        makeColorBlocksArea(position)
                        leftTopBlockPath = path
                    } else if (i == row - 1) {//left bottom corner block
                        corners[6] = cornerRadius
                        corners[7] = cornerRadius
                        path = makePath(position, corners)
                    }

                } else if (columnIndex == colorScheme.size - 1) {
                    val corners = floatArrayOf(
                        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
                    )
                    if (i == 0) {//right top corner block
                        corners[2] = cornerRadius
                        corners[3] = cornerRadius
                        path = makePath(position, corners)
                    } else if (i == row - 1) {//right bottom corner block
                        corners[4] = cornerRadius
                        corners[5] = cornerRadius
                        path = makePath(position, corners)
                    }

                }

                gradientColor.add(ColorBlock(color, position, path))
            }
            colorList[baseColor] = gradientColor
        }
    }

    private fun makeColorBlocksArea(position: RectF) {
        val width = colorScheme.size * colorUnitWidth
        val height = row * colorUnitWidth
        colorBlocksArea.set(position.left.toInt(), position.top.toInt(), position.left.toInt() + width, position.left.toInt() + height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawColorBlocks(canvas)
        drawSelection(canvas)
        drawStroke(canvas)
    }

    private fun drawStroke(canvas: Canvas) {
        if (leftTopBlockPath != null) canvas.drawPath(leftTopBlockPath!!, strokePaint)
    }

    private fun findSelection(x: Int, y: Int): ColorBlock? {
        colorList.entries
            .flatMap { it.value }
            .forEach { colorBlock ->
                if (colorBlock.position.contains(x.toFloat(), y.toFloat())) {
                    return colorBlock
                }
            }
        return null
    }

    private fun drawSelection(canvas: Canvas) {
        checkedColorUnit?.apply {
            if (this.cornerPath != null) {
                canvas.drawPath(this.cornerPath, selectorPaint)
            } else {
                canvas.drawRect(this.position, selectorPaint)
            }
        }

    }

    private fun drawColorBlocks(canvas: Canvas) {
        colorList.entries
            .flatMap { it.value }
            .forEachIndexed { _, colorUnit ->
                colorBlockPaint.color = Color.parseColor(colorUnit.colorString)
                if (colorUnit.cornerPath != null) {
                    canvas.drawPath(colorUnit.cornerPath, colorBlockPaint)
                } else {
                    canvas.drawRect(colorUnit.position, colorBlockPaint)
                }
            }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mPointerX = event.x.toInt()
        mPointerY = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val entry = findSelection(mPointerX, mPointerY)
                if (entry != null) {
                    checkedColorUnit = entry
                    dispatchColorChanged(entry)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                restrictPointers()
                val entry = findSelection(mPointerX, mPointerY)
                if (entry != null && checkedColorUnit != entry) {
                    checkedColorUnit = entry
                    dispatchColorChanged(entry)
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                checkedColorUnit?.apply {
                    onColorSelectListener?.afterColorChanged(this.colorString)
                }

                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun dispatchColorChanged(entry: ColorBlock) {
        onColorSelectListener?.onColorChanged(entry.colorString)
        invalidate()
    }

    /**
     * Restricts the pointer to the color block area
     */
    private fun restrictPointers() {
        if (mPointerX < colorBlocksArea.left) {
            mPointerX = colorBlocksArea.left
        }

        if (mPointerX > colorBlocksArea.right) {
            mPointerX = colorBlocksArea.right - offset
        }

        if (mPointerY < colorBlocksArea.top) {
            mPointerY = colorBlocksArea.top
        }

        if (mPointerY > colorBlocksArea.bottom) {
            mPointerY = colorBlocksArea.bottom - offset
        }

    }

    /**
     * @param color Hex color string e.g. "#ff151515"
     * @return true if the selection was successful, false otherwise
     */
    fun checkColor(color: String): Boolean {
        if (color.isEmpty()) {
            LogUtil.e(TAG, "color can't be empty")
            return false
        }

        if (colorList.entries.isEmpty()) {
            checkedColor = color
            return false
        } else {
            val colorNoAlpha = removeAlphaFromColor(color)
            colorList.entries
                .flatMap { it.value }
                .find { colorUnit -> colorUnit.colorString == colorNoAlpha }
                ?.also {
                    checkedColorUnit = it
                    invalidate()
                    return true
                }
        }

        return false
    }

}