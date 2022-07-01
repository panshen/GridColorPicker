package com.panshen.palette

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout.LayoutParams
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.panshen.gridcolorpicker.GridColorPicker
import com.panshen.gridcolorpicker.builder.colorPickerDialog
import com.panshen.gridcolorpicker.builder.colorPickerView
import com.panshen.palette.databinding.ActivityMainBinding

@SuppressLint("SetTextI18n")
class MainActivityKt : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var dialog: AlertDialog? = null
    var gridColorPicker: GridColorPicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.center.text = "DSL"
        binding.colorPicker.checkColor(ColorPreference.getColor(this))
        binding.colorPicker.onColorChanged = { color ->
            onColorChanged(color)
        }
        binding.colorPicker.afterColorChanged = { color ->
            afterColorChanged(color)
        }
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.xml -> {
                binding.colorPicker.visibility = View.VISIBLE
                gridColorPicker?.visibility = View.GONE
            }
            R.id.center -> {
                binding.colorPicker.visibility = View.GONE
                createColorPickerView()
            }
            R.id.dialog -> {
                showDialog()
            }
            R.id.checkColor -> {
                binding.colorPicker.checkColor(binding.et.text.toString())
            }
        }
    }

    private fun onColorChanged(color: String) {
        binding.colorChanged.setBackgroundColor(Color.parseColor(color))
        binding.tvOnColorChanged.text = "onColorChanged() - ${color.uppercase()}"
    }

    private fun afterColorChanged(color: String) {
        ColorPreference.saveColor(color, this)
        binding.afterColorChanged.setBackgroundColor(Color.parseColor(color))
        binding.tvAfterColorChanged.text =
            "afterColorChanged() - ${color.uppercase()}"
    }

    private fun createColorPickerView() {
        if (gridColorPicker == null) {
            gridColorPicker = colorPickerView {
                colorScheme =
                    arrayListOf(
                        R.color.C_029FD6, R.color.C_0062FD, R.color.C_5023B1, R.color.C_962BB9, R.color.C_BA2C5E, R.color.C_FB431B, R.color.C_FD6802, R.color.C_FFAB03, R.color.C_FFCB05, R.color.C_FFFD46, R.color.C_D8EB39, R.color.C_76BC40
                    )
                row = 5
                onColorChanged = { color ->
                    onColorChanged(color)
                }
                afterColorChanged = { color ->
                    afterColorChanged(color)
                }

//                alphaViewLabelText = resources.getString(R.string.opacity)
//                drawCard = true
//                cardColorRes = R.color.teal_200
//                alphaViewEnable = false
//                alphaViewLabelEnable = false
            }.build(this)

            val lp = LayoutParams(300.dp, LayoutParams.WRAP_CONTENT).also {
                it.gravity = Gravity.CENTER
            }
            binding.container.addView(gridColorPicker, lp)
        } else {
            gridColorPicker?.visibility = View.VISIBLE
        }
    }

    private fun showDialog() {
        if (dialog == null) {
            dialog = colorPickerDialog {
                cancelable = true
                positiveButtonText = resources.getString(R.string.confirm)
                negativeButtonText = resources.getString(R.string.cancel)
                onPositiveButtonClickListener = DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(this@MainActivityKt, "Click Positive Button", Toast.LENGTH_SHORT)
                        .show()
                }
                onNegativeButtonClickListener = DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(this@MainActivityKt, "Click Negative Button", Toast.LENGTH_SHORT)
                        .show()
                }
                dismissListener = DialogInterface.OnDismissListener {
                    Toast.makeText(this@MainActivityKt, "Dismiss", Toast.LENGTH_SHORT).show()
                }

                colorPicker {
                    colorScheme =
                        arrayListOf(R.color.C_30525C, R.color.C_49928A, R.color.C_E6D17E, R.color.C_E5A367, R.color.C_C66C52)
                    row = 5
                    checkedColor = ColorPreference.getColor(this@MainActivityKt)
                    selectorColorRes = R.color.teal_200
                    showAlphaView = true
                    showAlphaViewLabel = true
                    alphaViewLabelText = resources.getString(R.string.opacity)
                    alphaViewLabelColorRes = R.color.teal_200

                    onColorChanged = { color ->
                        onColorChanged(color)
                    }
                    afterColorChanged = { color ->
                        afterColorChanged(color)
                    }
                }
            }.show(this)
        } else {
            dialog?.show()
        }
    }
}

val Number.dp: Int get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()