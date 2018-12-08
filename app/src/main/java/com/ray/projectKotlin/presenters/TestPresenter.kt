package com.ray.projectKotlin.presenters

import com.ray.projectKotlin.base.BasePresenter
import com.ray.projectKotlin.commons.Logger

class TestPresenter : BasePresenter() {
    override fun onCreate() {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
    }

    fun print () {
        Logger.d(rayTag, "测试打印")
    }

    fun print (value: String) {
        Logger.d(rayTag, "$value 测试打印")
    }
}