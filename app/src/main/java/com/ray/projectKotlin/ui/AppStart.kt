package com.ray.projectKotlin.ui

import android.Manifest
import android.view.View
import android.widget.TextView
import com.ray.projectKotlin.R
import com.ray.projectKotlin.base.BaseActivity
import com.ray.projectKotlin.presenters.AppStartPresenter
import com.ray.projectKotlin.ui.test.AppTest

class AppStart : BaseActivity<AppStartPresenter>() {
    private var testJumpView: TextView? = null
    override fun initLayout(): Int {
        return R.layout.activity_start
    }

    override fun initView() {
        checkPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION))

        testJumpView = findViewById<TextView>(R.id.testJump);
        testJumpView!!.text = "修改组件显示"
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

    fun jumpTest(view: View) {
        this.nextActivity(AppTest::class.java, true);
    }
}