package com.panshen.gridcolorpicker.model

import androidx.annotation.ColorRes

open class PickerBuilderParams {
    var row: Int? = null

    @ColorRes
    var selectorColorRes: Int? = null
    var checkedColor: String? = null
    var showAlphaView: Boolean? = null
    var showAlphaViewLabel: Boolean? = null
    var alphaViewLabelText: String? = null

    @ColorRes
    var alphaViewLabelColorRes: Int? = null

    @ColorRes
    var colorScheme: ArrayList<Int>? = null

    var onColorChanged: ((String) -> Unit)? = null
    var afterColorChanged: ((String) -> Unit)? = null
}