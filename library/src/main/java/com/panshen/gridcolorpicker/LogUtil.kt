package com.panshen.gridcolorpicker

import android.util.Log

class LogUtil {
    companion object {
        // set true to enable log
        @JvmField
        var isDebugEnabled: Boolean = false

        @JvmStatic
        fun v(tag: String, message: String) {
            if (isDebugEnabled) {
                Log.v(tag, message)
            }
        }

        @JvmStatic
        fun i(tag: String, message: String) {
            if (isDebugEnabled) {
                Log.i(tag, message)
            }
        }

        @JvmStatic
        fun d(tag: String, message: String) {
            if (isDebugEnabled) {
                Log.d(tag, message)
            }
        }

        @JvmStatic
        fun w(tag: String, message: String) {
            if (isDebugEnabled) {
                Log.w(tag, message)
            }
        }

        @JvmStatic
        fun e(tag: String, message: String) {
            if (isDebugEnabled) {
                Log.e(tag, message)
            }
        }
    }
}