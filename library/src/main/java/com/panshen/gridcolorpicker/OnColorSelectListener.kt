package com.panshen.gridcolorpicker

interface OnColorSelectListener {
    /**
     * Called when the color changes
     * @param color hex color string e.g. "#ff000000"
     */
    fun onColorChanged(color: String)

    /**
     * Called after the color has changed
     * @param color hex color string e.g. "#ff000000"
     */
    fun afterColorChanged(color: String)
}