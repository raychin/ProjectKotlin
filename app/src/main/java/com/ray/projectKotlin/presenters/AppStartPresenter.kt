package com.ray.projectKotlin.presenters

import com.ray.projectKotlin.base.BasePresenter
import com.ray.projectKotlin.commons.Logger

class AppStartPresenter : BasePresenter() {
    override fun onCreate() {
        Logger.d(rayTag, "onCreate")
    }

    override fun onRestart() {
        Logger.d(rayTag, "onRestart")
    }

    override fun onStart() {
        Logger.d(rayTag, "onStart")
    }

    override fun onResume() {
        Logger.d(rayTag, "onResume")
    }

    override fun onPause() {
        Logger.d(rayTag, "onPause")
    }

    override fun onStop() {
        Logger.d(rayTag, "onStop")
    }

    override fun onDestroy() {
        Logger.d(rayTag, "onDestroy")
    }

    override fun remove(tag: String) {
        TODO("Not yet implemented")
    }

    override fun removeAll() {
        TODO("Not yet implemented")
    }

    override fun cancel(tag: String) {
        TODO("Not yet implemented")
    }

    override fun cancelAll() {
        TODO("Not yet implemented")
    }

    fun print () {
        Logger.d(rayTag, "测试打印")
    }

    fun print (value: String) {
        Logger.d(rayTag, "$value 测试打印")
    }
}