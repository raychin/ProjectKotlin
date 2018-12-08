package com.ray.projectKotlin.base

/**
 * 管理网络请求
 * @created by ray on 2018/12/06
 */
interface IRxAction {
    //fun add(tag: String, subscription: Subscription)
    fun remove(tag: String)
    fun removeAll()
    fun cancel(tag: String)
    fun cancelAll()
}