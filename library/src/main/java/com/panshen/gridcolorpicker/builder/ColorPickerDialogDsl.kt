package com.panshen.gridcolorpicker.builder

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.panshen.gridcolorpicker.GridColorPicker
import com.panshen.gridcolorpicker.OnColorSelectListener
import com.panshen.gridcolorpicker.model.PickerBuilderParams
import com.panshen.gridcolorpicker.model.PickerConfig

/**
 * Construct an [AlertDialog] in DSL style with a [GridColorPicker] inside.
 */
class ColorPickerDialogDsl : PickerBuilderParams() {
    private lateinit var dialog: AlertDialog
    lateinit var gridColorPicker: GridColorPicker
    var cancelable = true
    var positiveButtonText: String? = null
    var negativeButtonText: String? = null
    var onPositiveButtonClickListener: DialogInterface.OnClickListener? = null
    var onNegativeButtonClickListener: DialogInterface.OnClickListener? = null
    var dismissListener: DialogInterface.OnDismissListener? = null
    var onColorSelectListener: OnColorSelectListener? = null

    fun show(context: Context): AlertDialog {
        var config = PickerConfig()
        row?.apply { config = config.copy(row = this) }
        checkedColor?.apply { config = config.copy(checkedColor = this) }
        selectorColorRes?.apply { config = config.copy(selectorColorRes = this) }
        showAlphaView?.apply { config = config.copy(showAlphaView = this) }
        showAlphaViewLabel?.apply { config = config.copy(showAlphaViewLabel = this) }
        alphaViewLabelText?.apply { config = config.copy(alphaViewLabelText = this) }
        alphaViewLabelColorRes?.apply { config = config.copy(alphaViewLabelColorRes = this) }
        colorScheme?.apply { config = config.copy(colorScheme = this) }
        config = config.copy(drawCard = false)

        gridColorPicker = GridColorPicker(context, config)
        gridColorPicker.onColorSelectListener = onColorSelectListener
        gridColorPicker.onColorChanged = onColorChanged
        gridColorPicker.afterColorChanged = afterColorChanged

        dialog = MaterialAlertDialogBuilder(
            context
        ).setCancelable(cancelable).setView(gridColorPicker).setOnDismissListener(dismissListener)
            .setPositiveButton(positiveButtonText, onPositiveButtonClickListener)
            .setNegativeButton(negativeButtonText, onNegativeButtonClickListener).show()
        return dialog
    }

}

@JvmSynthetic
fun colorPickerDialog(init: Dialog.() -> Unit): ColorPickerDialogDsl = Dialog().apply(init).create()

class Dialog {
    var cancelable = true
    var positiveButtonText: String? = null
    var negativeButtonText: String? = null
    private var pickerBuilderParams: PickerBuilderParams? = null

    var onPositiveButtonClickListener: DialogInterface.OnClickListener? = null
    var onNegativeButtonClickListener: DialogInterface.OnClickListener? = null
    var dismissListener: DialogInterface.OnDismissListener? = null
    var onColorSelectListener: OnColorSelectListener? = null

    fun colorPicker(palette: PickerBuilderParams.() -> Unit) {
        pickerBuilderParams = PickerBuilderParams().apply(palette)
    }

    fun create(): ColorPickerDialogDsl {
        return ColorPickerDialogDsl().also { builder ->
            builder.cancelable = cancelable
            builder.negativeButtonText = negativeButtonText
            builder.positiveButtonText = positiveButtonText
            builder.onNegativeButtonClickListener = onNegativeButtonClickListener
            builder.onPositiveButtonClickListener = onPositiveButtonClickListener
            builder.dismissListener = dismissListener
            builder.onColorSelectListener = onColorSelectListener

            builder.row = pickerBuilderParams?.row
            builder.colorScheme = pickerBuilderParams?.colorScheme
            builder.checkedColor = pickerBuilderParams?.checkedColor
            builder.selectorColorRes = pickerBuilderParams?.selectorColorRes
            builder.showAlphaView = pickerBuilderParams?.showAlphaView
            builder.showAlphaViewLabel = pickerBuilderParams?.showAlphaViewLabel
            builder.alphaViewLabelText = pickerBuilderParams?.alphaViewLabelText
            builder.alphaViewLabelColorRes = pickerBuilderParams?.alphaViewLabelColorRes
            builder.onColorChanged = pickerBuilderParams?.onColorChanged
            builder.afterColorChanged = pickerBuilderParams?.afterColorChanged
        }
    }

}