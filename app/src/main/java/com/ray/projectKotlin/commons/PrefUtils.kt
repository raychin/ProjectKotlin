package com.ray.projectKotlin.commons

import android.content.SharedPreferences
import com.ray.projectKotlin.BuildConfig
import com.ray.projectKotlin.config.ProjectApplication

/**
 * SharedPreferences帮助类
 * @Created by Ray on 2018/12/06.
 */
class PrefUtils {

    companion object {

        private const val PREF_NAME_CACHE = BuildConfig.APPLICATION_ID + ".cache"
        private const val PREF_NAME_VARS = BuildConfig.APPLICATION_ID + ".vars"

        fun cachePrefs(): SharedPreferences {
            return ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0)
        }

        fun varsPrefs(): SharedPreferences {
            return ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_VARS, 0)
        }

        fun putCacheLoginState(value: Boolean) {
            val editor = ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0).edit()
            editor.putBoolean("login_state", value).commit()
        }

        fun getCacheLoginState(): Boolean {
            return ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0)
                .getBoolean("login_state", false)
        }

        /**
         * 保存为解析的用户数据
         * @param info
         */
        fun saveUserInfo(info: String) {
            val editor = ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0).edit()
            editor.putString("user_info", info).commit()
        }

        fun getUserInfo(): String {
            return ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0)
                .getString("user_info", "")
        }

        /**
         * 保存是否接收推送通知
         * @param state
         */
        fun savePushState(state: Boolean) {
            val editor = ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0).edit()
            editor.putBoolean("msg_push", state).commit()
        }

        fun getPushState(): Boolean {
            return ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0)
                .getBoolean("msg_push", true)
        }

        /**
         * 是否第三方登录
         * @param value
         */
        fun putThirdLoginState(value: Boolean) {
            val editor = ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0).edit()
            editor.putBoolean("third_login_state", value).commit()
        }

        fun getThirdLoginState(): Boolean {
            return ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0)
                .getBoolean("third_login_state", false)
        }

        /**
         * 设置更新消息
         * @param sign
         */
        fun putUpdateMessage(sign: String) {
            val editor = ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0).edit()
            editor.putString("update_message", sign).commit()
        }

        fun getUpdateMessage(): String {
            return ProjectApplication.get()!!.getSharedPreferences(PREF_NAME_CACHE, 0)
                .getString("update_message", "")
        }

        /**
         * 设置程序第一次启动时执行
         * @param value
         */
        fun putFirstLoad(key: String, mode: Int, value: Int) {
            val editor = ProjectApplication.get()!!.getSharedPreferences(key, mode).edit()
            editor.putInt("is_first_load", value).commit()
        }

        fun getFirstLoad(key: String, mode: Int): Int {
            return ProjectApplication.get()!!.getSharedPreferences(key, mode)
                .getInt("is_first_load", 0)
        }

    }
}