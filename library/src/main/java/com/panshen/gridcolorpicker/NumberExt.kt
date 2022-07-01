package com.panshen.gridcolorpicker

import android.content.res.Resources

internal fun Int.toColorString() = "#${Integer.toHexString(this)}"
internal val Number.dp: Int get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()
internal val Number.dpf: Float get() = (toInt() * Resources.getSystem().displayMetrics.density)