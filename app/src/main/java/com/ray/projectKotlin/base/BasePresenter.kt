package com.ray.projectKotlin.base

import android.util.ArrayMap
import rx.Subscription
import java.lang.ref.Reference

abstract class BasePresenter: IActivityEvent, IRxAction {
    protected val rayTag:String = this.javaClass.simpleName

    protected var subscriptions: ArrayMap<Any, Subscription>? = null

    protected var viewRef: Reference<IBaseView>? = null
    protected var view: IBaseView? = null
//    protected var activityRef: Reference<T>? = null
//    protected var activity: T? = null
}