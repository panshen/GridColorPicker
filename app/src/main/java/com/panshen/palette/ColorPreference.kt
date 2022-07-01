package com.panshen.palette

import android.content.Context

object ColorPreference {
    fun saveColor(color: String, ctx: Context) {
        ctx.getSharedPreferences("ColorPreference", Context.MODE_PRIVATE).edit()
            .putString("HexColor", color).apply()
    }

    fun getColor(ctx: Context) = ctx.getSharedPreferences("ColorPreference", Context.MODE_PRIVATE)
        .getString("HexColor", "")?:""
}