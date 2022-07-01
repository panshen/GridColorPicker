package com.panshen.gridcolorpicker.builder

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.panshen.gridcolorpicker.GridColorPicker
import com.panshen.gridcolorpicker.OnColorSelectListener
import com.panshen.gridcolorpicker.model.PickerConfig

/**
 * A builder for creating [GridColorPicker] instances for Java language.
 */
class ColorPickerDialogBuilder(val context: Context) {
    lateinit var dialog: AlertDialog
    private var config = PickerConfig()

    private var onColorSelectListener: OnColorSelectListener? = null

    private var cancelable = true
    private var positiveButtonText: String? = null
    private var negativeButtonText: String? = null
    private var onPositiveButtonClickListener: DialogInterface.OnClickListener? = null
    private var onNegativeButtonClickListener: DialogInterface.OnClickListener? = null
    private var dismissListener: DialogInterface.OnDismissListener? = null

    fun setCancelable(cancelable: Boolean): ColorPickerDialogBuilder {
        this.cancelable = cancelable
        return this
    }

    fun setPositiveButton(text: String, listener: DialogInterface.OnClickListener): ColorPickerDialogBuilder {
        positiveButtonText = text
        onPositiveButtonClickListener = listener
        return this
    }

    fun setNegativeButton(text: String, listener: DialogInterface.OnClickListener): ColorPickerDialogBuilder {
        negativeButtonText = text
        onNegativeButtonClickListener = listener
        return this
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener): ColorPickerDialogBuilder {
        dismissListener = listener
        return this
    }

    fun setRow(row: Int): ColorPickerDialogBuilder {
        config = config.copy(row = row)
        return this
    }

    fun setCheckColor(colorString: String): ColorPickerDialogBuilder {
        config = config.copy(checkedColor = colorString)
        return this
    }

    fun setColorScheme(colorScheme: ArrayList<Int>): ColorPickerDialogBuilder {
        config = config.copy(colorScheme = colorScheme)
        return this
    }

    fun alphaViewEnable(enable: Boolean): ColorPickerDialogBuilder {
        config = config.copy(showAlphaView = enable)
        return this
    }

    fun alphaViewLabelEnable(enable: Boolean): ColorPickerDialogBuilder {
        config = config.copy(showAlphaViewLabel = enable)
        return this
    }

    fun alphaViewLabelText(text: String): ColorPickerDialogBuilder {
        config = config.copy(alphaViewLabelText = text)
        return this
    }

    fun setAlphaViewLabelColorRes(@ColorRes resId: Int): ColorPickerDialogBuilder {
        config = config.copy(alphaViewLabelColorRes = resId)
        return this
    }

    fun setBackgroundColorRes(@ColorRes resId: Int): ColorPickerDialogBuilder {
        config = config.copy(cardColorRes = resId)
        return this
    }

    fun setSelectorColorRes(@ColorRes resId: Int): ColorPickerDialogBuilder {
        config = config.copy(selectorColorRes = resId)
        return this
    }

    fun setOnColorSelectListener(listener: OnColorSelectListener): ColorPickerDialogBuilder {
        onColorSelectListener = listener
        return this
    }

    fun show(): AlertDialog {
        config = config.copy(drawCard = false)
        val gridColorPicker = GridColorPicker(context, config).also {
            it.onColorSelectListener = onColorSelectListener
        }

        dialog = MaterialAlertDialogBuilder(
            context
        ).setCancelable(cancelable).setView(gridColorPicker).setOnDismissListener(dismissListener)
            .setPositiveButton(positiveButtonText, onPositiveButtonClickListener)
            .setNegativeButton(negativeButtonText, onNegativeButtonClickListener).show()
        return dialog
    }
}