package com.panshen.gridcolorpicker.builder

import android.content.Context
import androidx.annotation.ColorRes
import com.panshen.gridcolorpicker.GridColorPicker
import com.panshen.gridcolorpicker.OnColorSelectListener
import com.panshen.gridcolorpicker.model.PickerConfig

/**
 * A builder class for creating [GridColorPicker] instances for Java language.
 */
class ColorPickerViewBuilder(val context: Context) {
    private var defaultConfig = PickerConfig()

    private var onColorSelectListener: OnColorSelectListener? = null

    fun setRow(row: Int): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(row = row)
        return this
    }

    fun setCheckColor(colorString: String): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(checkedColor = colorString)
        return this
    }

    fun setColorScheme(colorScheme: ArrayList<Int>): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(colorScheme = colorScheme)
        return this
    }

    fun alphaViewEnable(enable: Boolean): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(showAlphaView = enable)
        return this
    }

    fun alphaViewLabelEnable(enable: Boolean): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(showAlphaViewLabel = enable)
        return this
    }

    fun alphaViewLabelText(text: String): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(alphaViewLabelText = text)
        return this
    }

    fun drawCard(draw: Boolean): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(drawCard = draw)
        return this
    }

    fun setAlphaViewLabelColorRes(@ColorRes resId: Int): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(alphaViewLabelColorRes = resId)
        return this
    }

    fun setCardColorRes(@ColorRes resId: Int): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(cardColorRes = resId)
        return this
    }

    fun setSelectorColorRes(@ColorRes resId: Int): ColorPickerViewBuilder {
        defaultConfig = defaultConfig.copy(selectorColorRes = resId)
        return this
    }

    fun setOnColorSelectListener(listener: OnColorSelectListener): ColorPickerViewBuilder {
        onColorSelectListener = listener
        return this
    }

    fun build() = GridColorPicker(context, defaultConfig).also {
        it.onColorSelectListener = onColorSelectListener
    }
}