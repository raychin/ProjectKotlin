package com.ray.projectKotlin.config

import android.app.Activity
import android.content.Context
import java.util.*

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * Created by Ray on 2016/12/06.
 */
class AppManager {
    companion object {
        private var activityStack: Stack<Activity>? = null
        private var instance: AppManager? = null

        /**
         * 单一实例
         */
        fun getAppManager(): AppManager {
            if (instance == null) {
                instance = AppManager()
            }
            return instance as AppManager
        }
    }


    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!!.get(i)) {
                activityStack!!.get(i).finish()
            }
            i ++
        }
        activityStack!!.clear()
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}