package com.ray.projectKotlin.commons

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import android.view.LayoutInflater
import com.ray.projectKotlin.R


/**
 * tips类
 * @create by ray on 2018/12/06
 */
class ToastUtils {
    companion object {


        private var oldMsg: String? = null
        private var time: Long = 0

        /**
         * 显示系统样式tips消息
         * @param context
         * @param msg
         * @param duration
         */
        fun showToast(context: Context, msg: String, duration: Int) {
            if (msg != oldMsg) { // 当显示的内容不一样时，即断定为不是同一个Toast
                Toast.makeText(context, msg, duration).show()
                time = System.currentTimeMillis()
            } else {
                // 显示内容一样时，只有间隔时间大于2秒时才显示
                if (System.currentTimeMillis() - time > 2000) {
                    Toast.makeText(context, msg, duration).show()
                    time = System.currentTimeMillis()
                }
            }
            oldMsg = msg
        }

        /**
         * 显示自定义样式tips消息
         * @param context
         * @param msg
         * @param duration
         */
        fun showCustomToast(context: Context, msg: String, duration: Int) {
            // 当显示的内容不一样时，即断定为不是同一个Toast
            if (msg != oldMsg) {
                makeCustomToast(context, msg, duration).show()
                time = System.currentTimeMillis()
            } else {
                // 显示内容一样时，只有间隔时间大于2秒时才显示
                if (System.currentTimeMillis() - time > 2000) {
                    makeCustomToast(context, msg, duration).show()
                    time = System.currentTimeMillis()
                }
            }
            oldMsg = msg
        }

        /**
         * 创建自定义样式tips消息
         * @param context
         * @param msg
         * @param duration
         * @return
         */
        private fun makeCustomToast(context: Context, msg: String, duration: Int): Toast {
            val toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null)
            val toast = Toast(context)
            toast.view = toastRoot
            val tv = toastRoot.findViewById(R.id.TextViewInfo) as TextView
            tv.text = msg
            toast.duration = duration
            return toast
        }
    }
}