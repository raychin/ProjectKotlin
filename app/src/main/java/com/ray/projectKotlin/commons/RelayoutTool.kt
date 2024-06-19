package com.ray.projectKotlin.commons

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView

class RelayoutTool {

    companion object {
        /**
         * ForW4H3 ForW16H9 将原视图 宽高，padding，margin, 及文本字体大小 按比例缩放，重新布局
         */
        fun relayoutViewHierarchy(view: View, scale: Float) {
//            if (view != null) {
//                return
//            }

            view.let {
                scaleView(view, scale)

                if (view is ViewGroup) {
                    var children: Array<View>? = null
                    try {
                        val field = ViewGroup::class.java.getDeclaredField("mChildren")
                        field.isAccessible = true
                        children = field.get(view) as Array<View>
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }

                    if (children != null) {
                        for (child in children) {
                            relayoutViewHierarchy(child, scale)
                        }
                    }
                }
            }
        }

        /**
         * 将视图按比例缩放，不考虑嵌套视图
         */
        private fun scaleView(view: View, scale: Float) {
            if (view is TextView) {
                resetTextSize(view, scale)
            }

            val pLeft = convertFloatToInt(view.paddingLeft * scale)
            val pTop = convertFloatToInt(view.paddingTop * scale)
            val pRight = convertFloatToInt(view.paddingRight * scale)
            val pBottom = convertFloatToInt(view.paddingBottom * scale)
            view.setPadding(pLeft, pTop, pRight, pBottom)
            val params = view.layoutParams ?: return
            scaleLayoutParams(params, scale)
        }

        /**
         * 将视图布局属性按比例设置
         */
        public fun scaleLayoutParams(params: LayoutParams, scale: Float) {
//            if (params == null) {
//                return
//            }
            params.let {
                if (params.width > 0) {
                    params.width = convertFloatToInt(params.width * scale)
                }
                if (params.height > 0) {
                    params.height = convertFloatToInt(params.height * scale)
                }

                if (params is ViewGroup.MarginLayoutParams) {
                    if (params.leftMargin > 0) {
                        params.leftMargin = convertFloatToInt(params.leftMargin * scale)
                    }
                    if (params.rightMargin > 0) {
                        params.rightMargin = convertFloatToInt(params.rightMargin * scale)
                    }
                    if (params.topMargin > 0) {
                        params.topMargin = convertFloatToInt(params.topMargin * scale)
                    }
                    if (params.bottomMargin > 0) {
                        params.bottomMargin = convertFloatToInt(params.bottomMargin * scale)
                    }
                }
            }
        }

        /**
         * 将 TextView（或其子类）文本大小按比例缩放
         */
        private fun resetTextSize(textView: TextView, scale: Float) {
            val size = textView.textSize
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * scale)
        }

        /**
         * float 转换至 int 小数四舍五入
         */
        private fun convertFloatToInt(sourceNum: Float): Int {
            return (sourceNum + 0.5f).toInt()
        }
    }
}