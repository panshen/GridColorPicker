package com.panshen.gridcolorpicker.model

import android.graphics.Path
import android.graphics.RectF

/**
 * @property colorString color of this block e.g. "#ff151515"
 * @property position position of this block
 * @property cornerPath if the block is a corner block, the path will be set, otherwise it will be null
 */
data class ColorBlock(val colorString: String, val position: RectF, val cornerPath: Path? = null)