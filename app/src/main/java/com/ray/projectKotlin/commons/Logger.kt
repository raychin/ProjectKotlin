package com.ray.projectKotlin.commons

import android.util.Log
import com.ray.projectKotlin.BuildConfig

class Logger {
    companion object {
        fun i(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.i(tag, msg)
            }
        }

        fun d(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, msg)
            }
        }

        fun v(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.v(tag, msg)
            }
        }
    }
}