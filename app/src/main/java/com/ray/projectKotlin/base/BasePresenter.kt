package com.ray.projectKotlin.base

abstract class BasePresenter : IActivityEvent {
    protected val rayTag = this.javaClass.simpleName!!
}