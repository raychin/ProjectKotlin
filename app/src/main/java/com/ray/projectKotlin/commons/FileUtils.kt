package com.ray.projectKotlin.commons

import android.content.Context
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Double
import java.math.BigDecimal
import java.nio.file.Files.isDirectory
import java.nio.file.Files.exists
import java.nio.file.Files.delete



/**
 * 文件处理类
 * @create by ray on 2018/12/06
 */
class FileUtils {
    companion object {

        fun localFileToString(context: Context): ArrayList<String> {
            val `is`: InputStream
            var text = ""
            val array = ArrayList<String>()
            try {
                `is` = context.getAssets().open("sensitive_word.txt")
                val size = `is`.available()

                // Read the entire asset into a local byte buffer.
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()

                // Convert the buffer into a string.
                text = String(buffer, Charsets.UTF_8)
                val a = text.split("、".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in a.indices) {
                    array.add(a[i])
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return array

        }

        /**
         * 比对输入的文字是否含有敏感词，true则没有敏感词，false则有敏感词。
         * @param content   输入的文字
         * @return
         */
        fun contrast(context: Context, content: String): Boolean {
            val array = localFileToString(context)
            var result = true
            for (i in 0 until array.size) {
                if (content.contains(array[i])) {
                    result = false
                    break
                } else {
                    result = true
                }
            }
            return result
        }

        /**
         * 删除文件
         * @param f
         */
        fun DeleteFile(f: File?) {
            if (f == null) {
                return
            }
            val directory = f!!.isDirectory()
            if (directory) {
                val files = f!!.listFiles() ?: return
                for (child in files) {
                    DeleteFile(child)
                }
            }
            f!!.delete()
        }

        /**
         * 此类用于获得文件或者目录大小
         *
         * FileSize.toString()以KB，MB，GB形式返回字符串
         *
         * getLongSize()以字节形式返回文件或者目录大小
         *
         * 关键方法
         *
         * File.length返回文件的字节
         */
        //bt字节参考量
        val SIZE_BT = 1024L
        //KB字节参考量
        val SIZE_KB = SIZE_BT * 1024L
        //MB字节参考量
        val SIZE_MB = SIZE_KB * 1024L
        //GB字节参考量
        val SIZE_GB = SIZE_MB * 1024L
        //TB字节参考量
        val SIZE_TB = SIZE_GB * 1024L
        val SACLE = 2

        //文件属性
        private var file: File? = null
        //文件大小属性
        private var longSize: Long = 0

        fun FileUtils(file: File) {
            this.file = file
        }

        //返回文件大小
        @Throws(RuntimeException::class, IOException::class)
        private fun getFileSize() {
            //初始化文件大小为0；
            this.longSize = 0

            //如果文件存在而且是文件，直接返回文件大小
            if (file!!.exists() && file!!.isFile()) {
                this.longSize = file!!.length()

                //文件存在而且是目录，递归遍历文件目录计算文件大小
            } else if (file!!.exists() && file!!.isDirectory()) {
                getFileSize(file!!)//递归遍历
            } else {
                throw RuntimeException("指定文件不存在")
            }
        }

        //递归遍历文件目录计算文件大小
        @Throws(RuntimeException::class, IOException::class)
        private fun getFileSize(file: File) {
            //获得文件目录下文件对象数组
            val fileArray = file.listFiles()
            //如果文件目录数组不为空或者length!=0,即目录为空目录
            if (fileArray != null && fileArray!!.size != 0) {
                //遍历文件对象数组
                for (i in fileArray!!.indices) {
                    val fileSI = fileArray!![i]
                    //如果是目录递归遍历
                    if (fileSI.isDirectory()) {
                        //递归遍历
                        getFileSize(fileSI)
                    }
                    //如果是文件
                    if (fileSI.isFile()) {
                        this.longSize += fileSI.length()
                    }
                }
            } else {
                //如果文件目录数组为空或者length==0,即目录为空目录
                this.longSize += 0
            }
        }

        @Throws(RuntimeException::class)
        override fun toString(): String {
            try {
                //调用计算文件或目录大小方法
                getFileSize()

                if (this.longSize >= 0 && this.longSize < SIZE_BT) {
                    return this.longSize.toString() + "B"
                } else if (this.longSize >= SIZE_BT && this.longSize < SIZE_KB) {
                    return (this.longSize / SIZE_BT).toString() + "KB"
                } else if (this.longSize >= SIZE_KB && this.longSize < SIZE_MB) {
                    return (this.longSize / SIZE_KB).toString() + "MB"
                } else if (this.longSize >= SIZE_MB && this.longSize < SIZE_GB) {
                    val longs = BigDecimal(Double.valueOf(this.longSize.toString() + "").toString())
                    val sizeMB = BigDecimal(java.lang.Double.valueOf(SIZE_MB.toString() + "").toString())
                    val result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString()
                    //double result = this.longSize / (double)SIZE_MB;
                    return result + "GB"
                } else {
                    val longs = BigDecimal(java.lang.Double.valueOf(this.longSize.toString() + "").toString())
                    val sizeMB = BigDecimal(java.lang.Double.valueOf(SIZE_GB.toString() + "").toString())
                    val result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString()
                    return result + "TB"
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
                throw RuntimeException(ex.message)
            }

        }


        fun getFile(): File? {
            return file
        }

        fun setFile(file: File) {
            this.file = file
        }

        @Throws(RuntimeException::class)
        fun getLongSize(): Long {
            try {
                //调用计算文件或目录大小方法
                getFileSize()
                return longSize
            } catch (ex: IOException) {
                ex.printStackTrace()
                throw RuntimeException(ex.message)
            }

        }
    }
}