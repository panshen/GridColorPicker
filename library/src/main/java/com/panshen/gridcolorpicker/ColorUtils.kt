package com.panshen.gridcolorpicker

import android.content.Context
import android.graphics.Color
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils

object ColorUtils {

    private val colorRegex by lazy {
        Regex("^#[0-9a-fA-F]{6,8}")
    }

    /**
     * Split the given colorScheme by ',' and returns as ArrayList
     * @param colorScheme color scheme like "#536DFE,#FFDDEE,#55FFDDEE,#FFFFFFFF,#000000"
     * @return ArrayList containing integer colors
     */
    fun colorSchemeString2List(colorScheme: String?): ArrayList<Int> {
        if (colorScheme.isNullOrEmpty()) {
            return arrayListOf()
        }

        val colors = ArrayList<Int>()
        val arr = colorScheme.split(",")
        arr.forEach { color ->
            val isColor = color.matches(colorRegex)
            if (!isColor) {
                LogUtil.e("ColorUtils", "$color is not a color string")
            } else {
                colors.add(Color.parseColor(color))
            }
        }
        return colors
    }

    fun retrieveColorsFromColorIds(context: Context, resIds: ArrayList<Int>): ArrayList<Int> {
        return resIds.map { ContextCompat.getColor(context, it) } as ArrayList<Int>
    }

    /**
     * Change the brightness of color
     * @param color the integer color value
     * @param lightness 0.0-1.0
     * @return Color after changing brightness
     */
    fun setColorLightness(color: Int, @FloatRange(from = 0.0, to = 1.0) lightness: Float): String {
        val hslArray = floatArrayOf(0f, 0f, 0f)
        ColorUtils.colorToHSL(color, hslArray)
        hslArray[2] = lightness
        return toHexEncoding(ColorUtils.HSLToColor(hslArray))
    }

    /**
     * Convert int color to hexadecimal encoding
     * @return the converted color e.g. "#000000"
     */
    private fun toHexEncoding(color: Int): String {
        val sb = StringBuilder()
        var r = Integer.toHexString(Color.red(color))
        var g = Integer.toHexString(Color.green(color))
        var b = Integer.toHexString(Color.blue(color))

        r = if (r.length == 1) "0$r" else r
        g = if (g.length == 1) "0$g" else g
        b = if (b.length == 1) "0$b" else b
        sb.append("#")
        sb.append(r)
        sb.append(g)
        sb.append(b)
        return sb.toString()
    }

    /**
     * Convert int color to hexadecimal encoding
     * @return the converted color e.g. "#FF000000"
     */
    fun toHexEncodingWithAlpha(color: Int): String {
        val sb = StringBuilder()
        var a = Integer.toHexString(Color.alpha(color))
        var r = Integer.toHexString(Color.red(color))
        var g = Integer.toHexString(Color.green(color))
        var b = Integer.toHexString(Color.blue(color))

        a = if (a.length == 1) "0$a" else a
        r = if (r.length == 1) "0$r" else r
        g = if (g.length == 1) "0$g" else g
        b = if (b.length == 1) "0$b" else b
        sb.append("#")
        sb.append(a)
        sb.append(r)
        sb.append(g)
        sb.append(b)
        return sb.toString()
    }

    /**
     * Remove the alpha component from color
     */
    fun removeAlphaFromColor(color: String): String? {
        return try {
            val intColor = ColorUtils.setAlphaComponent(Color.parseColor(color), 0)
            intColor.toColorString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    /**
     * Remove the alpha component from color
     */
    fun removeAlphaFromColor(color: Int): String? {
        return try {
            val intColor = ColorUtils.setAlphaComponent(color, 0)
            intColor.toColorString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     * @param alpha 0-255
     * @param color hex color string
     */
    fun setAlphaComponent(alpha: Int, color: String): String {
        val colorWithAlpha = ColorUtils.setAlphaComponent(Color.parseColor(color), alpha)
        return colorWithAlpha.toColorString()
    }

}