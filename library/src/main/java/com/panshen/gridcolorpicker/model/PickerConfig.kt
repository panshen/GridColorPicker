package com.panshen.gridcolorpicker.model

import androidx.annotation.ColorRes
import com.panshen.gridcolorpicker.GridColorPicker
import com.panshen.gridcolorpicker.R

/**
 * The default configuration of [GridColorPicker]
 *
 * @property row The number of rows in the grid, minimal is 2.
 * @property checkedColor The color of the checked cell.
 * @property colorScheme The color scheme of the grid, minimal is 2.
 * @property showAlphaView Whether to show the AlphaView.
 * @property showAlphaViewLabel Whether to show label of the AlphaView. if showAlphaView is set to false, this property will be ignored.
 * @property alphaViewLabelText The text of the AlphaView label.
 * @property drawCard Whether to draw card background of the GridColorPicker.
 * @property cardColorRes The color resource of the card.
 * @property alphaViewLabelColorRes The color resource of the label.
 * @property selectorColorRes The color resource of the selector.
 */
data class PickerConfig(
    val row: Int = 10,
    val checkedColor: String = "",
    val colorScheme: ArrayList<Int> = arrayListOf(
        R.color.C_029FD6, R.color.C_0062FD, R.color.C_5023B1, R.color.C_962BB9, R.color.C_BA2C5E, R.color.C_FB431B, R.color.C_FD6802, R.color.C_FFAB03, R.color.C_FFCB05, R.color.C_FFFD46, R.color.C_D8EB39, R.color.C_76BC40
    ),
    val showAlphaView: Boolean = true,
    val showAlphaViewLabel: Boolean = true,
    val alphaViewLabelText: String = "Opacity",
    val drawCard: Boolean = true,
    @ColorRes val cardColorRes: Int = R.color.bg_color,
    @ColorRes val alphaViewLabelColorRes: Int = R.color.alpha_label_color,
    @ColorRes val selectorColorRes: Int = R.color.selector
)