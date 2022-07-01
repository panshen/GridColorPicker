package com.panshen.gridcolorpicker.builder

import android.content.Context
import androidx.annotation.ColorRes
import com.panshen.gridcolorpicker.GridColorPicker
import com.panshen.gridcolorpicker.OnColorSelectListener
import com.panshen.gridcolorpicker.model.PickerBuilderParams
import com.panshen.gridcolorpicker.model.PickerConfig

/**
 * Construct a [GridColorPicker] in DSL style
 */
class ColorPickerViewDsl : PickerBuilderParams() {
    lateinit var gridColorPicker: GridColorPicker

    var onColorSelectListener: OnColorSelectListener? = null
    var drawCard: Boolean? = null

    @ColorRes
    var cardColorRes: Int? = null

    fun build(context: Context): GridColorPicker {
        var config = PickerConfig()
        row?.apply { config = config.copy(row = this) }
        checkedColor?.apply { config = config.copy(checkedColor = this) }
        selectorColorRes?.apply { config = config.copy(selectorColorRes = this) }
        showAlphaView?.apply { config = config.copy(showAlphaView = this) }
        showAlphaViewLabel?.apply { config = config.copy(showAlphaViewLabel = this) }
        alphaViewLabelText?.apply { config = config.copy(alphaViewLabelText = this) }
        alphaViewLabelColorRes?.apply { config = config.copy(alphaViewLabelColorRes = this) }
        colorScheme?.apply { config = config.copy(colorScheme = this) }
        drawCard?.apply { config = config.copy(drawCard = this) }
        cardColorRes?.apply { config = config.copy(cardColorRes = this) }

        gridColorPicker = GridColorPicker(context, config)
        gridColorPicker.onColorChanged = onColorChanged
        gridColorPicker.afterColorChanged = afterColorChanged
        gridColorPicker.onColorSelectListener = onColorSelectListener

        return gridColorPicker
    }

}

fun colorPickerView(init: View.() -> Unit): ColorPickerViewDsl = View().apply(init).create()

class View : PickerBuilderParams() {
    var drawCard: Boolean? = null

    @ColorRes
    var cardColorRes: Int? = null

    var onColorSelectListener: OnColorSelectListener? = null

    fun create(): ColorPickerViewDsl {
        return ColorPickerViewDsl().also { builder ->
            builder.onColorSelectListener = onColorSelectListener
            builder.row = row
            builder.colorScheme = colorScheme
            builder.checkedColor = checkedColor
            builder.selectorColorRes = selectorColorRes
            builder.showAlphaView = showAlphaView
            builder.showAlphaViewLabel = showAlphaViewLabel
            builder.alphaViewLabelText = alphaViewLabelText
            builder.alphaViewLabelColorRes = alphaViewLabelColorRes
            builder.onColorChanged = onColorChanged
            builder.afterColorChanged = afterColorChanged
            builder.drawCard = drawCard
            builder.cardColorRes = cardColorRes
        }
    }

}
