package com.ray.projectKotlin.ui

import android.Manifest
import com.ray.projectKotlin.R
import com.ray.projectKotlin.base.BaseActivity
import com.ray.projectKotlin.presenters.TestPresenter

class AppStart : BaseActivity<TestPresenter>() {
    override fun initLayout(): Int {
        return R.layout.activity_start
    }

    override fun initView() {
        checkPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION))
    }

    override fun initData() {
        presenter?.print()
        presenter?.print(rayTag)
    }

    override fun isMvp(): Boolean {
        return true
    }

    override fun isExit(): Boolean {
        return true
    }

    override fun isImmersiveStatus(): Boolean {
        return true
    }

    override fun isImmersiveStatusHeight(): Boolean {
        return true
    }

    override fun isConvertStatusBarColor(): Boolean {
        return true
    }
}