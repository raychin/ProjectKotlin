package com.ray.projectKotlin.config

import android.content.Context
import android.os.Environment
import com.ray.projectKotlin.BuildConfig
import com.ray.projectKotlin.bean.UserBean
import java.io.File
import com.ray.projectKotlin.commons.*
import java.io.FileInputStream
import java.util.*
import java.io.FileOutputStream
import com.ray.projectKotlin.commons.PrefUtils

/**
 * 应用配置类：用于保存用户相关信息及设置
 * @create by ray on 2018/12/06
 */
class AppConfig {
    private var mContext: Context? = null

    companion object {
        private val rayTag = this.javaClass.simpleName!!

        private var appConfig: AppConfig? = null

        public const val CONF_APP_UNIQUE_ID = BuildConfig.APPLICATION_ID + ".APP_UNIQUE_ID"
        private const val APP_CONFIG = BuildConfig.APPLICATION_ID + ".config"
        /**
         * 缓存相关保存文件的名称
         */
        private const val APP_CACHE_DIR_NAME = BuildConfig.APPLICATION_ID
        /**
         * 错误日志保存路径
         */
        private const val DIR_LOG = "$APP_CACHE_DIR_NAME/log"

        var HEAD: String = ""
        // 图标缓存
        var ICON_CACHE_DIR: String = ""
        // 上传图片缓存
        var CACHE_UPLOAD_IMAGES_DIR: String = ""
        // 下载文件存放
        var DOWNLOAD_DIR: String = ""
        // 音频文件
        var CACHE_VOICE_DIR: String = ""
        // 视频文件
        var CACHE_AUDIO_DIR: String = ""


        fun getAppConfig(context: Context): AppConfig {
            if (appConfig == null) {
                appConfig = AppConfig()
                appConfig!!.mContext = context
                init(context)
            }
            return appConfig as AppConfig
        }

        // 本方法只在程序第一次启动时执行
        private fun init(context: Context) {
            var isCreated = false;
            val code = PrefUtils.getFirstLoad(APP_CONFIG, Context.MODE_PRIVATE)
            Logger.i(rayTag, code.toString())

            val state = Environment.getExternalStorageState()
            // 存在SD卡
            HEAD = if (state == Environment.MEDIA_MOUNTED) {
                Environment.getExternalStorageDirectory().absolutePath + "/$APP_CACHE_DIR_NAME"
            } else {
                context.cacheDir.absolutePath + "/$APP_CACHE_DIR_NAME"
            }
            ICON_CACHE_DIR = "$HEAD/icons"    // 缓存图片路径
            CACHE_UPLOAD_IMAGES_DIR = "$HEAD/cache_upload_images"    // 上传图片路径
            DOWNLOAD_DIR = "$HEAD/downloads"    // 下载文件路径
            CACHE_VOICE_DIR = "$HEAD/cache_voices"    // 视频缓存路径
            CACHE_AUDIO_DIR = "$HEAD/cache_audios"    // 音频缓存路径
            if (code === 0) {
                doClearCache()
                val head = File(HEAD)
                if (!head.exists()) {
                    isCreated = head.mkdir()
                    Logger.i(rayTag, "$HEAD = $isCreated")
                }
                val cacheUploadImage = File(CACHE_UPLOAD_IMAGES_DIR)
                if (!cacheUploadImage.exists()) {
                    isCreated = cacheUploadImage.mkdirs()
                    Logger.i(rayTag, "$CACHE_UPLOAD_IMAGES_DIR = $isCreated")
                }
                val downloadDir = File(DOWNLOAD_DIR)
                if (!downloadDir.exists()) {
                    isCreated = downloadDir.mkdirs()
                    Logger.i(rayTag, "$DOWNLOAD_DIR = $isCreated")
                }
                val iconCacheDir = File(ICON_CACHE_DIR)
                if (!iconCacheDir.exists()) {
                    isCreated = iconCacheDir.mkdirs()
                    Logger.i(rayTag, "$ICON_CACHE_DIR = $isCreated")
                }
                val voiceDir = File(CACHE_VOICE_DIR)
                if (!voiceDir.exists()) {
                    isCreated = voiceDir.mkdirs()
                    Logger.i(rayTag, "$CACHE_VOICE_DIR = $isCreated")
                }
                val audioDir = File(CACHE_AUDIO_DIR)
                if (!audioDir.exists()) {
                    isCreated = audioDir.mkdirs()
                    Logger.i(rayTag, "$CACHE_AUDIO_DIR = $isCreated")
                }

                if (isCreated) {
                    PrefUtils.putFirstLoad(APP_CONFIG, Context.MODE_PRIVATE, 1)
                }
            }
        }

        private fun doClearCache() {
            val file = File(HEAD)
            FileUtils.DeleteFile(file)
            PrefUtils.putFirstLoad(APP_CONFIG, Context.MODE_PRIVATE, 0)
        }

        /**
         * 获取SD卡根目录
         * @return
         */
        fun getSdCard(): String {
            var sdDir: File? = null
            // 判断sd卡是否存在
            val sdCardExist = Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
            if (sdCardExist) {
                // 获取跟目录
                sdDir = Environment.getExternalStorageDirectory()
            }
            return sdDir.toString()
        }


        fun doSaveUser(mContext: Context, user: UserBean) {
        }

        fun doGetUser(mContext: Context): UserBean {
            val user = UserBean()
            return user
        }

        fun getUser(mContext: Context): UserBean? {
            val user = UserBean()
            return if (PrefUtils.getUserInfo() == ("")) null else user
        }

        fun doDeleteUser(mContext: Context) {
            val sp = mContext.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE)
            val edit = sp.edit()
            // edit.move
            edit.commit()
        }
    }

    fun get(): Properties {
        var fis: FileInputStream? = null
        val props = Properties()
        try {
            val dirConf = mContext!!.getDir(APP_CONFIG, Context.MODE_PRIVATE)
            fis = FileInputStream(dirConf.path + File.separator  + APP_CONFIG)
            props.load(fis)
        } catch (e:Exception) {}
        finally {
            try
            {
                fis!!.close()
            } catch (e:Exception) {}

            }
        return props
    }

    operator fun get(key: String): String {
        val props = get()
        return props?.getProperty(key)
    }

    operator fun set(key: String, value: String) {
        val props = get()
        props.setProperty(key, value)
        setProps(props)
    }

    private fun setProps(p: Properties) {
        var fos: FileOutputStream? = null
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            val dirConf = mContext!!.getDir(APP_CONFIG, Context.MODE_PRIVATE)
            val conf = File(dirConf, APP_CONFIG)
            fos = FileOutputStream(conf)

            p.store(fos, null)
            fos!!.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: Exception) {
            }

        }
    }
}