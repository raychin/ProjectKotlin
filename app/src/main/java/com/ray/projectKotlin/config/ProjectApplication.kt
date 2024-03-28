package com.ray.projectKotlin.config

import android.app.Application
import android.text.TextUtils
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;


/**
 * 应用配置
 * @create by ray on 2018/12/06
 */
class ProjectApplication : Application() {
    companion object {
        var sInstance: ProjectApplication? = null
        fun get () : ProjectApplication? {
            return sInstance
        }

        fun handleSSLHandshake() {
            try {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }

                    override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

                    override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
                })

                val sc = SSLContext.getInstance("SSL")
                sc.init(null, trustAllCerts, SecureRandom())
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
                HttpsURLConnection.setDefaultHostnameVerifier { arg0, arg1 -> true }
            } catch (ignored: Exception) {
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this;
        AppConfig.getAppConfig(this);
        handleSSLHandshake()

        /**
         * TBS-webview初始化，在App启动后尽可能早地调用初始化接口，进行内核预加载
         * tbs_sdk_thirdapp_v4.3.0.316_44216_sharewithdownloadwithfile_withoutGame_obfs_20220728_101601.jar
         */
    }

    /**
     * 获取App安装包信息
     * @return
     */
    fun getPackageInfo(): PackageInfo {
        var info: PackageInfo? = null
        try {
            info = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace(System.err)
        }

        if (info == null)
            info = PackageInfo()
        return info
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    fun getAppId(): String {
        var uniqueID = getProperty(AppConfig.CONF_APP_UNIQUE_ID)
        if (TextUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString()
            setProperty(AppConfig.CONF_APP_UNIQUE_ID, uniqueID)
        }
        return uniqueID
    }

    fun setProperty(key: String, value: String) {
        AppConfig.getAppConfig(this)[key] = value
    }

    fun getProperty(key: String): String {
        return AppConfig.getAppConfig(this)[key]
    }

    fun isAppSound(): Boolean {
        return true
    }

    /**
     * 是否启动检查更新
     * @return
     */
    fun isCheckUp(): Boolean {
        return true
    }

    fun isPlayAnim(): Boolean {
        return false
    }


    /**
     * 利用反射获取状态栏高度
     * @return
     */
    fun getStatusBarHeight(): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}