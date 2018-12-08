package com.ray.projectKotlin.base

/**
 * activity生命周期
 * @create by ray on 2018/12/06
 */
interface IActivityEvent {
    fun onCreate()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}